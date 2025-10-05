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

/**
 * WebRTCManager: quản lý logic WebRTC cơ bản.
 * - Không gọi surfaceView.init(...) ở đây. UI (AndroidView.factory) phải init renderer.
 * - Manager lưu tham chiếu renderer (localRenderer / remoteRenderer) để addSink khi track có mặt.
 * - Thêm log chi tiết để debug.
 */
class WebRTCManager(private val context: Context) {

    private val TAG = "WebRTCManager"

    val eglBase: EglBase = EglBase.create()
    private val peerConnectionFactory: PeerConnectionFactory

    private var peerConnection: PeerConnection? = null
    private var videoCapturer: CameraVideoCapturer? = null

    private var localVideoTrack: VideoTrack? = null
    private var localAudioTrack: AudioTrack? = null
    private var localMediaStream: MediaStream? = null

    // remote video
    @Volatile
    private var remoteVideoTrack: VideoTrack? = null

    // UI renderers (gắn bởi UI bằng gọi setLocalVideoSink / setRemoteVideoSink)
    private var localRenderer: SurfaceViewRenderer? = null
    private var remoteRenderer: SurfaceViewRenderer? = null

    // pending ICE candidates nếu đến trước khi remoteDescription set
    private val pendingIceCandidates = CopyOnWriteArrayList<IceCandidate>()

    init {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .setEnableInternalTracer(true)
                .createInitializationOptions()
        )

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(DefaultVideoEncoderFactory(eglBase.eglBaseContext, true, true))
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglBase.eglBaseContext))
            .createPeerConnectionFactory()

        Log.i(TAG, "PeerConnectionFactory initialized")
    }

    /**
     * Tạo local media stream (video + audio).
     * Caller phải đảm bảo đã cấp CAMERA + RECORD_AUDIO permission.
     */
    fun createLocalMediaStream(): MediaStream {
        if (localVideoTrack != null && localAudioTrack != null && localMediaStream != null) {
            Log.d(TAG, "createLocalMediaStream: reuse existing tracks")
            return localMediaStream!!
        }

        videoCapturer = createCameraCapturer()
            ?: throw IllegalStateException("No camera found on device")

        val surfaceHelper = SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext)
        val videoSource = peerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        videoCapturer?.initialize(surfaceHelper, context, videoSource.capturerObserver)

        try {
            videoCapturer?.startCapture(640, 480, 30)
            Log.d(TAG, "Camera capture started (640x480@30)")
        } catch (t: Throwable) {
            Log.e(TAG, "startCapture failed", t)
        }

        localVideoTrack = peerConnectionFactory.createVideoTrack("LOCAL_VIDEO", videoSource)
        Log.d(TAG, "Local video track created")

        val audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        localAudioTrack = peerConnectionFactory.createAudioTrack("LOCAL_AUDIO", audioSource)
        Log.d(TAG, "Local audio track created")

        localMediaStream = peerConnectionFactory.createLocalMediaStream("LOCAL_STREAM").apply {
            addTrack(localVideoTrack)
            addTrack(localAudioTrack)
        }

        // attach to renderer if UI đã set renderer trước đó
        localRenderer?.let { renderer ->
            try {
                Log.d(TAG, "Attaching localVideoTrack -> localRenderer")
                localVideoTrack?.addSink(renderer)
            } catch (t: Throwable) {
                Log.w(TAG, "attach local renderer failed", t)
            }
        }

        return localMediaStream!!
    }

    /**
     * Khởi tạo PeerConnection. Caller cung cấp callbacks để gửi ICE / xử lý remote stream.
     */
    fun initPeerConnection(
        iceServers: List<PeerConnection.IceServer>,
        onIceCandidate: (IceCandidate) -> Unit,
        onAddRemoteStream: (MediaStream) -> Unit
    ) {
        // dispose previous PC nếu có
        try {
            peerConnection?.dispose()
        } catch (_: Exception) {
        }
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
                    if (candidate != null) {
                        Log.d(
                            TAG,
                            "onIceCandidate => ${candidate.sdpMid}:${candidate.sdpMLineIndex}"
                        )
                        onIceCandidate(candidate)
                    }
                }

                override fun onSignalingChange(newState: PeerConnection.SignalingState?) {
                    Log.d(TAG, "onSignalingChange: $newState")
                }

                override fun onIceConnectionChange(newState: PeerConnection.IceConnectionState?) {
                    Log.d(TAG, "onIceConnectionChange: $newState")
                }

                override fun onIceConnectionReceivingChange(receiving: Boolean) {
                    Log.d(TAG, "onIceConnectionReceivingChange: $receiving")
                }

                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {
                    Log.d(TAG, "onIceGatheringChange: $state")
                }

                override fun onAddStream(stream: MediaStream?) {
                    Log.d(TAG, "onAddStream called (deprecated API). stream=$stream")
                    stream?.let { s ->
                        val track = s.videoTracks.firstOrNull()
                        if (track != null) {
                            remoteVideoTrack = track
                            Log.d(TAG, "Remote video track set from onAddStream")
                            // attach to remote renderer nếu đã có
                            remoteRenderer?.let { r ->
                                try {
                                    Log.d(
                                        TAG,
                                        "Attaching remoteVideoTrack -> remoteRenderer (onAddStream)"
                                    )
                                    track.addSink(r)
                                } catch (t: Throwable) {
                                    Log.w(TAG, "attach remote renderer failed", t)
                                }
                            }
                        }
                        onAddRemoteStream(s)
                    }
                }

                // Modern callback for remote tracks
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
                                Log.d(
                                    TAG,
                                    "Attaching remoteVideoTrack -> remoteRenderer (onAddTrack)"
                                )
                                it.addSink(r)
                            } catch (t: Throwable) {
                                Log.w(TAG, "attach remote renderer failed", t)
                            }
                        }
                    }
                    // if streams provided, call callback with first stream for legacy handling
                    mediaStreams?.firstOrNull()?.let { s -> onAddRemoteStream(s) }
                }

                override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
                override fun onRemoveStream(p0: MediaStream?) {}
                override fun onDataChannel(p0: DataChannel?) {}
                override fun onRenegotiationNeeded() {}
                override fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {
                    Log.d(TAG, "onConnectionChange: $newState")
                }

                override fun onStandardizedIceConnectionChange(newState: PeerConnection.IceConnectionState?) {
                    Log.d(TAG, "onStandardizedIceConnectionChange: $newState")
                }

                override fun onTrack(transceiver: RtpTransceiver?) {}
            }
        )

        // nếu local track đã có, add vào peerConnection (Unified Plan: addTrack)
        localVideoTrack?.let {
            try {
                peerConnection?.addTrack(it)
                Log.d(TAG, "Added local video track to PeerConnection")
            } catch (t: Throwable) {
                Log.w(TAG, "Failed to add local video track to PeerConnection", t)
            }
        }
        localAudioTrack?.let {
            try {
                peerConnection?.addTrack(it)
                Log.d(TAG, "Added local audio track to PeerConnection")
            } catch (t: Throwable) {
                Log.w(TAG, "Failed to add local audio track to PeerConnection", t)
            }
        }
    }

    /**
     * UI gọi để gán SurfaceViewRenderer cho local preview.
     * NOTE: UI phải init(renderer) trước khi gọi.
     */
    fun setLocalVideoSink(renderer: SurfaceViewRenderer) {
        localRenderer = renderer
        Log.d(TAG, "setLocalVideoSink called. localVideoTrack exists=${localVideoTrack != null}")
        localVideoTrack?.let { track ->
            try {
                // remove then add to avoid duplicates
                try {
                    track.removeSink(renderer)
                } catch (_: Exception) {
                }
                track.addSink(renderer)
                Log.d(TAG, "Local track -> addSink succeeded")
            } catch (t: Throwable) {
                Log.w(TAG, "Local track addSink failed", t)
            }
        }
    }

    /**
     * UI gọi để gán SurfaceViewRenderer cho remote video.
     * NOTE: UI phải init(renderer) trước khi gọi.
     */
    fun setRemoteVideoSink(renderer: SurfaceViewRenderer) {
        remoteRenderer = renderer
        Log.d(TAG, "setRemoteVideoSink called. remoteVideoTrack exists=${remoteVideoTrack != null}")
        remoteVideoTrack?.let { track ->
            try {
                try {
                    track.removeSink(renderer)
                } catch (_: Exception) {
                }
                track.addSink(renderer)
                Log.d(TAG, "Remote track -> addSink succeeded")
            } catch (t: Throwable) {
                Log.w(TAG, "Remote track addSink failed", t)
            }
        }
    }

    fun createOffer(onOfferCreated: (SessionDescription) -> Unit) {
        Log.d(TAG, "createOffer()")
        peerConnection?.createOffer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription?) {
                if (desc == null) return
                Log.d(TAG, "createOffer.onCreateSuccess: sdp length=${desc.description.length}")
                peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)
                onOfferCreated(desc)
            }
        }, MediaConstraints())
    }

    fun createAnswer(onAnswerCreated: (SessionDescription) -> Unit) {
        Log.d(TAG, "createAnswer()")
        peerConnection?.createAnswer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription?) {
                if (desc == null) return
                Log.d(TAG, "createAnswer.onCreateSuccess: sdp length=${desc.description.length}")
                peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)
                onAnswerCreated(desc)
            }
        }, MediaConstraints())
    }

    fun setRemoteDescription(desc: SessionDescription) {
        Log.d(TAG, "setRemoteDescription type=${desc.type} len=${desc.description.length}")
        peerConnection?.setRemoteDescription(object : SdpObserverAdapter() {
            override fun onSetSuccess() {
                Log.d(TAG, "setRemoteDescription: onSetSuccess")
                // apply pending ICE if any
                if (pendingIceCandidates.isNotEmpty()) {
                    for (c in pendingIceCandidates) {
                        try {
                            peerConnection?.addIceCandidate(c)
                        } catch (t: Throwable) {
                            Log.w(TAG, "Failed adding pending ICE", t)
                        }
                    }
                    pendingIceCandidates.clear()
                }
            }

            override fun onSetFailure(error: String?) {
                Log.w(TAG, "setRemoteDescription onSetFailure: $error")
            }
        }, desc)
    }

    fun addIceCandidate(candidate: IceCandidate) {
        if (peerConnection?.remoteDescription == null) {
            pendingIceCandidates.add(candidate)
            Log.d(
                TAG,
                "Queued ICE candidate (pending remoteDescription). total pending=${pendingIceCandidates.size}"
            )
            return
        }
        try {
            peerConnection?.addIceCandidate(candidate)
            Log.d(TAG, "addIceCandidate applied: ${candidate.sdpMid}:${candidate.sdpMLineIndex}")
        } catch (t: Throwable) {
            Log.w(TAG, "addIceCandidate failed", t)
        }
    }

    fun close() {
        try {
            videoCapturer?.stopCapture()
        } catch (t: Throwable) {
            Log.w(TAG, "stop capture error", t)
        }
        try {
            videoCapturer?.dispose()
        } catch (_: Exception) {
        }
        videoCapturer = null

        try {
            peerConnection?.close()
        } catch (_: Exception) {
        }
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
            eglBase.release()
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

/**
 * Light SdpObserver adapter for logging.
 */
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
