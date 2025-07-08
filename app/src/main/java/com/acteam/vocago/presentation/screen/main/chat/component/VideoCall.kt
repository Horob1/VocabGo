package com.acteam.vocago.presentation.screen.main.chat.component

import android.media.MediaPlayer
import android.widget.VideoView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.utils.RequestCameraPermission
import kotlinx.coroutines.delay

enum class CallState {
    PREVIEW, VIDEO, END
}

@Composable
fun VideoCallScreen(
    rootNavController: NavController,
    receivedName: String,
    @DrawableRes avatarResId: Int,
    @RawRes videoResId: Int? = null
) {
    val context = LocalContext.current
    var callState by remember { mutableStateOf(CallState.PREVIEW) }
    var micEnabled by remember { mutableStateOf(true) }
    var speakerEnabled by remember { mutableStateOf(true) }
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var ringtonePlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var callDuration by remember { mutableLongStateOf(0L) }

    val avatar = painterResource(id = avatarResId)
    val videoUri = remember(videoResId) {
        videoResId?.let { "android.resource://${context.packageName}/$it".toUri() }
    }

    var permissionGranted by remember { mutableStateOf(false) }

    RequestCameraPermission(
        onGranted = { permissionGranted = true },
        onDenied = {
            rootNavController.popBackStack()
        }
    )

    fun updateVolume(mp: MediaPlayer?, enabled: Boolean) {
        mp?.setVolume(if (enabled) 1f else 0f, if (enabled) 1f else 0f)
    }
    DisposableEffect(Unit) {
        onDispose {
            ringtonePlayer?.let {
                try {
                    if (it.isPlaying) it.stop()
                } catch (e: IllegalStateException) {
                } finally {
                    it.release()
                    ringtonePlayer = null
                }
            }

            mediaPlayer?.let {
                try {
                    if (it.isPlaying) it.stop()
                } catch (e: IllegalStateException) {
                } finally {
                    it.release()
                    mediaPlayer = null
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (callState) {
            CallState.PREVIEW -> {
                if (permissionGranted) {
                    CameraPreviewView(modifier = Modifier.fillMaxSize())

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 180.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = avatar,
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.White, CircleShape)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = receivedName,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    LaunchedEffect(Unit) {
                        val ringPlayer = MediaPlayer.create(context, R.raw.callmusic)
                        ringtonePlayer = ringPlayer
                        ringPlayer.isLooping = true
                        ringPlayer.start()
                        val startTime = System.currentTimeMillis()
                        delay(10000)
                        val endTime = System.currentTimeMillis()
                        callDuration = (endTime - startTime) / 1000
                        ringPlayer.stop()
                        ringPlayer.release()
                        ringtonePlayer = null
                        callState = CallState.VIDEO
                    }
                }
            }

            CallState.VIDEO -> {
                if (videoUri == null) {
                    CameraPreviewView(modifier = Modifier.fillMaxSize())

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 180.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = avatar,
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.White, CircleShape)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = receivedName,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Không trả lời",
                            color = Color.LightGray,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        IconButton(
                            onClick = {
                                rootNavController.popBackStack()
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 90.dp)
                                .size(64.dp)
                                .background(Color.Red, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                } else {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            VideoView(context).apply {
                                setVideoURI(videoUri)
                                setOnPreparedListener { mp ->
                                    mediaPlayer = mp
                                    updateVolume(mp, speakerEnabled)
                                }
                                setOnCompletionListener {
                                    val duration = mediaPlayer?.duration?.toLong() ?: 0L
                                    callDuration = duration / 1000
                                    callState = CallState.END
                                }
                                start()
                            }
                        },
                        update = {
                            updateVolume(mediaPlayer, speakerEnabled)
                        }
                    )

                    Box(
                        modifier = Modifier
                            .size(120.dp, 160.dp)
                            .align(Alignment.TopEnd)
                            .offset(y = 90.dp, x = (-12).dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(alpha = 0.2f))
                            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                    ) {
                        CameraPreviewView(modifier = Modifier.fillMaxSize())
                    }
                }
            }

            CallState.END -> {
                CameraPreviewView(modifier = Modifier.fillMaxSize())
                IconButton(
                    onClick = { rootNavController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 180.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = avatar,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(id = R.string.text_call_ended),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${stringResource(id = R.string.text_call_duration)} ${callDuration}s",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }

        if (permissionGranted && callState != CallState.END) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 90.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { micEnabled = !micEnabled },
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (micEnabled) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(
                    onClick = {
                        speakerEnabled = !speakerEnabled
                        updateVolume(mediaPlayer, speakerEnabled)
                    },
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (speakerEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(
                    onClick = {
                        ringtonePlayer?.let {
                            if (it.isPlaying) {
                                it.stop()
                            }
                            it.release()
                            ringtonePlayer = null
                        }
                        val position = mediaPlayer?.currentPosition?.toLong() ?: 0L
                        callDuration = position / 1000
                        callState = CallState.END
                    },
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.Red, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun CameraPreviewView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black),
        factory = {
            PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                implementationMode =
                    PreviewView.ImplementationMode.COMPATIBLE
            }.also { previewView ->
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build()
                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    preview.surfaceProvider = previewView.surfaceProvider

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        }
    )
}
