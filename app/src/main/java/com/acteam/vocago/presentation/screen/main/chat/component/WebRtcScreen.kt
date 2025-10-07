package com.acteam.vocago.presentation.screen.main.chat.component

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.socket.CallUiState
import com.acteam.vocago.presentation.socket.SocketViewModel
import com.acteam.vocago.utils.WebRTCManager
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer

@Composable
fun WebRtcScreen(
    viewModel: SocketViewModel,
    navController: NavController
) {
    val uiState by viewModel.callUiState.collectAsState()
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val context = LocalContext.current

    var parentSize by remember { mutableStateOf(IntSize(0, 0)) }
    var remoteRendererRef by remember { mutableStateOf<SurfaceViewRenderer?>(null) }
    var localRendererRef by remember { mutableStateOf<SurfaceViewRenderer?>(null) }
    var isFirstClick by remember { mutableStateOf(true) }

    // --- Init / clear WebRTC ---
    LaunchedEffect(Unit) { viewModel.initWebRTC(context) }
    DisposableEffect(Unit) { onDispose { viewModel.clearWebRTC() } }

    // --- Release renderers on lifecycle destroy ---
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                remoteRendererRef?.release()
                localRendererRef?.release()
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(obs)
            remoteRendererRef?.release()
            localRendererRef?.release()
        }
    }

    val webRTCManager = viewModel.webRTCManager

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { parentSize = it }
    ) {
        if (webRTCManager != null) {
            AndroidView(
                factory = { ctx ->
                    SurfaceViewRenderer(ctx).apply {
                        init(webRTCManager.eglBaseLocal.eglBaseContext, null)
                        setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL)
                        setMirror(false)
                        setEnableHardwareScaler(true)
                        setZOrderMediaOverlay(false)
                        remoteRendererRef = this
                        webRTCManager.setRemoteVideoSink(this)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (uiState is CallUiState.Idle || uiState is CallUiState.Waiting)
                            Modifier.alpha(0f) // ẩn tạm thời
                        else Modifier
                    ),
                update = { renderer ->
                    webRTCManager.setRemoteVideoSink(renderer)
                }
            )
        }

        // --- Noise background khi chưa có remote stream ---
        if (uiState is CallUiState.Idle) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                TvNoise(modifier = Modifier.fillMaxSize())
            }
        } else if (uiState is CallUiState.Waiting) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                TvNoise(modifier = Modifier.fillMaxSize())
                Text(
                    text = stringResource(R.string.finding_user),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
            }
        }

        // --- Local preview (floating) ---
        if (webRTCManager != null &&
            (uiState !is CallUiState.Idle && uiState !is CallUiState.Waiting)
        ) {
            FloatingLocalVideo(
                modifier = Modifier
                    .size(width = 120.dp, height = 160.dp)
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(12.dp)),
                webRTCManager = webRTCManager,
                parentBounds = parentSize,
                setRendererRef = { localRendererRef = it }
            )
        }

        // --- Buttons ---
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Button(onClick = {
                viewModel.findStranger()
                isFirstClick = false
            }) {
                Text(
                    if (isFirstClick)
                        stringResource(R.string.text_btn_start)
                    else
                        stringResource(R.string.text_next)
                )
            }

            Button(
                onClick = {
                    viewModel.leaveCall()
                    isFirstClick = true
                },
                enabled = !isFirstClick
            ) {
                Text(stringResource(R.string.text_stop))
            }

            Button(onClick = {
                viewModel.leaveCall()
                navController.popBackStack()
            }) {
                Text(stringResource(R.string.text_end))
            }
        }
    }
}

@Composable
private fun FloatingLocalVideo(
    modifier: Modifier,
    webRTCManager: WebRTCManager,
    parentBounds: IntSize,
    setRendererRef: (SurfaceViewRenderer?) -> Unit
) {
    var videoSize by remember { mutableStateOf(IntSize(0, 0)) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val offsetAnimated by animateOffsetAsState(targetValue = Offset(offsetX, offsetY))

    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier
            .offset { IntOffset(offsetAnimated.x.toInt(), offsetAnimated.y.toInt()) }
            .pointerInput(parentBounds) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX = (offsetX + dragAmount.x)
                        .coerceIn(-calculateHorizontalOffsetBounds(parentBounds, videoSize), 0f)
                    offsetY = (offsetY + dragAmount.y)
                        .coerceIn(0f, calculateVerticalOffsetBounds(parentBounds, videoSize))
                }
            }
    ) {
        AndroidView(
            factory = { ctx ->
                SurfaceViewRenderer(ctx).apply {
                    init(webRTCManager.eglBaseLocal.eglBaseContext, null)
                    setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
                    setMirror(true)
                    setEnableHardwareScaler(true)
                    setZOrderMediaOverlay(true)
                    webRTCManager.setLocalVideoSink(this)
                    setRendererRef(this)
                }
            },
            update = { renderer ->
                webRTCManager.setLocalVideoSink(renderer)
            },
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { videoSize = it }
        )
    }
}

private fun calculateHorizontalOffsetBounds(
    parentBounds: IntSize,
    floatingVideoSize: IntSize
): Float {
    val parentWidthPx = parentBounds.width.toFloat()
    val floatingWidth = floatingVideoSize.width.toFloat()
    return if (parentWidthPx == 0f) 0f else (parentWidthPx - floatingWidth).coerceAtLeast(0f)
}

private fun calculateVerticalOffsetBounds(
    parentBounds: IntSize,
    floatingVideoSize: IntSize
): Float {
    val parentHeightPx = parentBounds.height.toFloat()
    val floatingHeight = floatingVideoSize.height.toFloat()
    return if (parentHeightPx == 0f) 0f else (parentHeightPx - floatingHeight).coerceAtLeast(0f)
}
