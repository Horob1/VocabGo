package com.acteam.vocago.utils

import android.content.Context
import android.util.Log
import org.webrtc.AudioTrack
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.DataChannel
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpReceiver
import org.webrtc.RtpTransceiver
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoTrack
import java.util.concurrent.CopyOnWriteArrayList

class WebRTCManager(private val context: Context) {

    private val TAG = "WebRTCManager"

    val eglBaseLocal = EglBase.create()
    val eglBaseRemote = EglBase.create()

    private val peerConnectionFactory: PeerConnectionFactory

    private var peerConnection: PeerConnection? = null
    private var videoCapturer: CameraVideoCapturer? = null

    private var localVideoTrack: VideoTrack? = null
    private var localAudioTrack: AudioTrack? = null
    private var localMediaStream: MediaStream? = null

    @Volatile
    private var remoteVideoTrack: VideoTrack? = null

    private var localRenderer: SurfaceViewRenderer? = null
    private var remoteRenderer: SurfaceViewRenderer? = null

    private val pendingIceCandidates = CopyOnWriteArrayList<IceCandidate>()

    init {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(true)
                .createInitializationOptions()
        )

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(
                DefaultVideoEncoderFactory(eglBaseLocal.eglBaseContext, true, true)
            )
            .setVideoDecoderFactory(
                DefaultVideoDecoderFactory(eglBaseLocal.eglBaseContext)
            )
            .createPeerConnectionFactory()

        Log.i(TAG, "PeerConnectionFactory initialized")
    }

    fun createLocalMediaStream(): MediaStream {
        if (localMediaStream != null) return localMediaStream!!

        videoCapturer = createCameraCapturer()
            ?: throw IllegalStateException("No camera found")

        val surfaceHelper =
            SurfaceTextureHelper.create("CaptureThread", eglBaseLocal.eglBaseContext)
        val videoSource = peerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        videoCapturer?.initialize(surfaceHelper, context, videoSource.capturerObserver)
        videoCapturer?.startCapture(640, 480, 30)

        localVideoTrack = peerConnectionFactory.createVideoTrack("LOCAL_VIDEO", videoSource)
        localAudioTrack =
            peerConnectionFactory.createAudioTrack(
                "LOCAL_AUDIO",
                peerConnectionFactory.createAudioSource(MediaConstraints())
            )

        localMediaStream =
            peerConnectionFactory.createLocalMediaStream("LOCAL_STREAM").apply {
                addTrack(localVideoTrack)
                addTrack(localAudioTrack)
            }

        localRenderer?.let {
            try {
                localVideoTrack?.addSink(it)
                Log.d(TAG, "Attached local video to renderer")
            } catch (t: Throwable) {
                Log.w(TAG, "Attach local renderer failed", t)
            }
        }

        return localMediaStream!!
    }

    fun initPeerConnection(
        iceServers: List<PeerConnection.IceServer>,
        onIceCandidate: (IceCandidate) -> Unit,
        onAddRemoteStream: (MediaStream) -> Unit,
        onConnectionClosed: () -> Unit
    ) {
        peerConnection?.dispose()
        peerConnection = null
        pendingIceCandidates.clear()
        remoteVideoTrack = null

        val rtcConfig = PeerConnection.RTCConfiguration(iceServers).apply {
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
        }

        peerConnection = peerConnectionFactory.createPeerConnection(
            rtcConfig,
            object : PeerConnection.Observer {
                override fun onIceCandidate(candidate: IceCandidate?) {
                    candidate?.let {
                        Log.d(TAG, "onIceCandidate => ${it.sdpMid}:${it.sdpMLineIndex}")
                        onIceCandidate(it)
                    }
                }

                override fun onAddTrack(
                    receiver: RtpReceiver?,
                    mediaStreams: Array<out MediaStream>?
                ) {
                    Log.d(TAG, "onAddTrack called receiver=$receiver streams=${mediaStreams?.size}")
                    val track = receiver?.track() as? VideoTrack
                    track?.let {
                        remoteVideoTrack = it
                        Log.d(TAG, "Remote video track set from onAddTrack")
                        remoteRenderer?.let { r ->
                            try {
                                it.addSink(r)
                                Log.d(TAG, "Remote track -> addSink succeeded")
                            } catch (t: Throwable) {
                                Log.w(TAG, "Attach remote renderer failed", t)
                            }
                        }
                    }
                    mediaStreams?.firstOrNull()?.let(onAddRemoteStream)
                }

                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                    Log.d(TAG, "onIceConnectionChange: $state")
                }

                override fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {
                    Log.d(TAG, "onConnectionChange: $newState")
                    if (newState == PeerConnection.PeerConnectionState.CLOSED ||
                        newState == PeerConnection.PeerConnectionState.DISCONNECTED ||
                        newState == PeerConnection.PeerConnectionState.FAILED
                    ) {
                        onConnectionClosed()
                    }
                }


                override fun onIceConnectionReceivingChange(p0: Boolean) {
                    Log.d(TAG, "onConnectionChange:")
                }

                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {
                    Log.d(TAG, "onIceGatheringChange: $state")
                }

                override fun onSignalingChange(newState: PeerConnection.SignalingState?) {
                    Log.d(TAG, "onSignalingChange: $newState")
                }

                override fun onDataChannel(dc: DataChannel?) {}
                override fun onRemoveStream(p0: MediaStream?) {}
                override fun onRenegotiationNeeded() {}
                override fun onAddStream(p0: MediaStream?) {}
                override fun onTrack(p0: RtpTransceiver?) {}
                override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
                override fun onStandardizedIceConnectionChange(newState: PeerConnection.IceConnectionState?) {}
            }
        )

        localVideoTrack?.let {
            peerConnection?.addTrack(it)
            Log.d(TAG, "Added local video track to PeerConnection")
        }
        localAudioTrack?.let {
            peerConnection?.addTrack(it)
            Log.d(TAG, "Added local audio track to PeerConnection")
        }
    }

    fun setLocalVideoSink(renderer: SurfaceViewRenderer) {
        localRenderer = renderer
        localVideoTrack?.let {
            it.removeSink(renderer)
            it.addSink(renderer)
        }
    }

    fun setRemoteVideoSink(renderer: SurfaceViewRenderer) {
        remoteRenderer = renderer
        remoteVideoTrack?.let {
            it.removeSink(renderer)
            it.addSink(renderer)
            Log.d(TAG, "Remote track -> addSink succeeded (manual reattach)")
        }
    }

    fun createOffer(onOfferCreated: (SessionDescription) -> Unit) {
        peerConnection?.createOffer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription?) {
                if (desc != null) {
                    peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)
                    onOfferCreated(desc)
                }
            }
        }, MediaConstraints())
    }

    fun createAnswer(onAnswerCreated: (SessionDescription) -> Unit) {
        peerConnection?.createAnswer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription?) {
                if (desc != null) {
                    peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)
                    onAnswerCreated(desc)
                }
            }
        }, MediaConstraints())
    }

    fun setRemoteDescription(desc: SessionDescription) {
        peerConnection?.setRemoteDescription(object : SdpObserverAdapter() {
            override fun onSetSuccess() {
                Log.d(TAG, "Remote description set. Applying pending ICE")
                pendingIceCandidates.forEach { peerConnection?.addIceCandidate(it) }
                pendingIceCandidates.clear()
            }
        }, desc)
    }

    fun addIceCandidate(candidate: IceCandidate) {
        if (peerConnection?.remoteDescription == null) {
            pendingIceCandidates.add(candidate)
        } else {
            peerConnection?.addIceCandidate(candidate)
        }
    }

    fun close() {
        try {
            videoCapturer?.stopCapture()
        } catch (_: Exception) {
        }
        videoCapturer?.dispose()
        videoCapturer = null

        peerConnection?.close()
        peerConnection = null

        localVideoTrack = null
        localAudioTrack = null
        localMediaStream = null
        remoteVideoTrack = null
        localRenderer = null
        remoteRenderer = null
        pendingIceCandidates.clear()
        Log.d(TAG, "close(): peerConnection + tracks released, factory still alive")
    }

    fun releaseAll() {
        close()
        try {
            peerConnectionFactory.dispose()
        } catch (_: Exception) {
        }
        try {
            eglBaseLocal.release()
        } catch (_: Exception) {
        }
        try {
            eglBaseRemote.release()
        } catch (_: Exception) {
        }
    }

    private fun createCameraCapturer(): CameraVideoCapturer? {
        val enumerator = Camera2Enumerator(context)
        val deviceNames = enumerator.deviceNames
        val front = deviceNames.firstOrNull { enumerator.isFrontFacing(it) }
        val back = deviceNames.firstOrNull { enumerator.isBackFacing(it) }
        return when {
            front != null -> enumerator.createCapturer(front, null)
            back != null -> enumerator.createCapturer(back, null)
            else -> null
        }
    }
}

open class SdpObserverAdapter : SdpObserver {
    override fun onCreateSuccess(p0: SessionDescription?) {}
    override fun onSetSuccess() {}
    override fun onCreateFailure(error: String?) {
        Log.w("SdpObserverAdapter", "create failure: $error")
    }

    override fun onSetFailure(error: String?) {
        Log.w("SdpObserverAdapter", "set failure: $error")
    }
}
