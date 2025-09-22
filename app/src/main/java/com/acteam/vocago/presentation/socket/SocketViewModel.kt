package com.acteam.vocago.presentation.socket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.BuildConfig
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.usecase.DeleteInfoUseCase
import com.acteam.vocago.domain.usecase.GetCredentialIdUseCase
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SocketViewModel(
    getCredentialIdUseCase: GetCredentialIdUseCase,
    getLoginStateUseCase: GetLoginStateUseCase,
    getLocalUserProfileUseCase: GetLocalUserProfileUseCase,
    private val deleteInfoUseCase: DeleteInfoUseCase,
) : ViewModel() {
    private var socket: Socket? = null
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
                    // connect socket here
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
        socket?.connect()

        socket?.on(Socket.EVENT_CONNECT) {
            println("✅ Socket connected with clientId=$clientId")
        }

        socket?.on("logout") {
            logout()
        }

        socket?.on("message") { args ->
            println("📩 Message from server: ${args.joinToString()}")
        }
    }

    private fun disconnectSocket() {
        socket?.disconnect()
        socket?.off()
        socket = null
        println("❌ Socket disconnected")
    }
}