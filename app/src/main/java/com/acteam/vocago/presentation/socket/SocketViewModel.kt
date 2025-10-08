package com.acteam.vocago.presentation.socket

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.BuildConfig
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.usecase.DeleteInfoUseCase
import com.acteam.vocago.domain.usecase.GetCredentialIdUseCase
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import com.acteam.vocago.utils.WebRTCManager
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.SessionDescription

sealed class CallUiState {
    object Idle : CallUiState()
    object Waiting : CallUiState()
    data class Connected(val roomId: String, val isInitiator: Boolean) : CallUiState()
    data class LocalStream(val stream: MediaStream) : CallUiState()
    data class RemoteStream(val stream: MediaStream) : CallUiState()
    object StrangerLeft : CallUiState()
}

class SocketViewModel(
    getCredentialIdUseCase: GetCredentialIdUseCase,
    getLoginStateUseCase: GetLoginStateUseCase,
    getLocalUserProfileUseCase: GetLocalUserProfileUseCase,
    private val deleteInfoUseCase: DeleteInfoUseCase,
) : ViewModel() {

    private var socket: Socket? = null
    private val _callUiState = MutableStateFlow<CallUiState>(CallUiState.Idle)
    val callUiState: StateFlow<CallUiState> = _callUiState.asStateFlow()

    private var roomId: String? = null

    var webRTCManager: WebRTCManager? = null
        private set

    fun initWebRTC(context: Context) {
        webRTCManager?.releaseAll()
        webRTCManager = WebRTCManager(context)
    }

    fun clearWebRTC() {
        webRTCManager?.releaseAll()
        webRTCManager = null
    }

    val socketData = combine(
        getCredentialIdUseCase(),
        getLoginStateUseCase(),
        getLocalUserProfileUseCase()
    ) { clientId, loginState, userProfile ->
        Triple(clientId, loginState, userProfile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        observeSocketData()
    }

    private fun observeSocketData() {
        viewModelScope.launch {
            socketData.filterNotNull().collect { (clientId, loginState, user) ->
                if (loginState && user != null && clientId != null) {
                    disconnectSocket()
                    connectSocket(clientId, user)
                } else {
                    disconnectSocket()
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            deleteInfoUseCase()
            disconnectSocket()
        }
    }

    private fun connectSocket(clientId: String, userDto: UserDto) {
        if (socket?.connected() == true) return

        val opts = IO.Options().apply {
            query = "userId=${userDto._id}&credentialId=$clientId&client=MOBILE"
            reconnection = true
            reconnectionAttempts = 5
            reconnectionDelay = 1000
        }

        socket = IO.socket(BuildConfig.BASE_URL, opts)

        socket?.apply {
            on(Socket.EVENT_CONNECT) {
                println("Socket connected with clientId=$clientId")
            }

            on("logout") { logout() }

            fun parseArgToJson(arg: Any?): JSONObject? {
                return when (arg) {
                    is JSONObject -> arg
                    is String -> try {
                        JSONObject(arg)
                    } catch (_: Exception) {
                        null
                    }

                    is Map<*, *> -> try {
                        JSONObject(arg as Map<String, Any>)
                    } catch (_: Exception) {
                        null
                    }

                    else -> null
                }
            }

            on("waiting-for-stranger") {
                _callUiState.value = CallUiState.Waiting
            }

            on("stranger-found") { args ->
                val data = parseArgToJson(args.getOrNull(0))
                if (data == null) {
                    return@on
                }
                roomId = data.optString("roomId", null)
                val isInitiator = data.optBoolean("isInitiator", false)
                if (roomId == null) {
                    return@on
                }
                _callUiState.value = CallUiState.Connected(roomId!!, isInitiator)

                // --- Local stream ---
                val localStream = try {
                    webRTCManager?.createLocalMediaStream()
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                localStream?.let { _callUiState.value = CallUiState.LocalStream(it) }

                // --- PeerConnection ---
                webRTCManager?.initPeerConnection(
                    iceServers = listOf(
                        org.webrtc.PeerConnection.IceServer.builder("stun:stun.l.google.com:19302")
                            .createIceServer()
                    ),
                    onIceCandidate = { candidate -> sendIceCandidate(candidate) },
                    onAddRemoteStream = { remoteStream ->
                        _callUiState.value = CallUiState.RemoteStream(remoteStream)
                    },
                    onConnectionClosed = {
                        _callUiState.value = CallUiState.Idle
                    }
                )

                if (isInitiator) {
                    webRTCManager?.createOffer { desc -> sendOffer(desc) }
                }
            }

            on("webrtc-offer") { args ->
                val data = parseArgToJson(args.getOrNull(0)) ?: return@on
                val offerObj = data.optJSONObject("offer") ?: return@on
                val sdp = offerObj.optString("sdp", null) ?: return@on
                val desc = SessionDescription(SessionDescription.Type.OFFER, sdp)
                webRTCManager?.setRemoteDescription(desc)
                webRTCManager?.createAnswer { answer -> sendAnswer(answer) }
            }

            on("webrtc-answer") { args ->
                val data = parseArgToJson(args.getOrNull(0)) ?: return@on
                val answerObj = data.optJSONObject("answer") ?: return@on
                val sdp = answerObj.optString("sdp", null) ?: return@on
                val desc = SessionDescription(SessionDescription.Type.ANSWER, sdp)
                webRTCManager?.setRemoteDescription(desc)
            }

            on("webrtc-ice-candidate") { args ->
                val data = parseArgToJson(args.getOrNull(0)) ?: return@on
                val candidateObj = data.optJSONObject("candidate") ?: return@on
                val sdpMid = candidateObj.optString("sdpMid", null) ?: return@on
                val sdpMLineIndex = candidateObj.optInt("sdpMLineIndex", -1)
                val candidateStr = candidateObj.optString("candidate", null) ?: return@on
                val candidate = IceCandidate(sdpMid, sdpMLineIndex, candidateStr)
                webRTCManager?.addIceCandidate(candidate)
            }

            on("stranger-left") {
                _callUiState.value = CallUiState.StrangerLeft
                closeConnection()
            }

            connect()
        }
    }

    fun findStranger() {
        socket?.emit("find-stranger")
    }

    fun leaveCall() {
        roomId?.let {
            socket?.emit("leave-call", JSONObject().apply { put("roomId", it) })
        }
        closeConnection()
    }

    private fun sendOffer(desc: SessionDescription) {
        socket?.emit("webrtc-offer", JSONObject().apply {
            put("roomId", roomId)
            put("offer", JSONObject().apply {
                put("type", desc.type.toString().lowercase())
                put("sdp", desc.description)
            })
        })
    }

    private fun sendAnswer(desc: SessionDescription) {
        socket?.emit("webrtc-answer", JSONObject().apply {
            put("roomId", roomId)
            put("answer", JSONObject().apply {
                put("type", desc.type.toString().lowercase())
                put("sdp", desc.description)
            })
        })
    }

    private fun sendIceCandidate(candidate: IceCandidate) {
        val candidateJson = JSONObject().apply {
            put("sdpMid", candidate.sdpMid)
            put("sdpMLineIndex", candidate.sdpMLineIndex)
            put("candidate", candidate.sdp)
        }
        socket?.emit("webrtc-ice-candidate", JSONObject().apply {
            put("roomId", roomId)
            put("candidate", candidateJson)
        })
    }

    private fun closeConnection() {
        webRTCManager?.close()
        _callUiState.value = CallUiState.Idle
        roomId = null
    }

    private fun disconnectSocket() {
        socket?.disconnect()
        socket?.off()
        socket = null
        println("❌ Socket disconnected")
    }

    override fun onCleared() {
        super.onCleared()
        if (roomId != null) leaveCall()
        disconnectSocket()
    }
}
