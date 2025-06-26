package com.acteam.vocago.presentation.screen.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import android.util.Log
import android.view.TextureView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CameraScreen(
    rootNavController: NavController,
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val detectedText by viewModel.detectedText
    val ocrBitmap by viewModel::ocrBitmap
    val ocrLines by viewModel::ocrLines
    val coroutineScope = rememberCoroutineScope()
    val textureViewRef = remember { mutableStateOf<TextureView?>(null) }

    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            permissionGranted = granted
            viewModel.cameraHandler?.permissionGranted = granted
            if (granted) {
                textureViewRef.value?.let { textureView ->
                    viewModel.cameraHandler?.bindTextureView(textureView)
                }
            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                try {
                    val bitmap = ImageDecoder
                        .createSource(context.contentResolver, uri)
                        .let { ImageDecoder.decodeBitmap(it) }

                    performOcr(
                        bitmap,
                        onResult = { viewModel.setOcrImageAndLines(bitmap, it) },
                        onText = viewModel::onTextDetected
                    )
                } catch (e: IOException) {
                    Log.e("CameraScreen", "Lỗi đọc ảnh: ${e.message}")
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        if (viewModel.cameraHandler == null) {
            viewModel.cameraHandler = Camera2Handler(context) { bitmap ->
                performOcr(
                    bitmap,
                    onResult = { viewModel.setOcrImageAndLines(bitmap, it) },
                    onText = viewModel::onTextDetected
                )
            }
        }

        viewModel.cameraHandler?.permissionGranted = permissionGranted

        if (!permissionGranted) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Theo dõi thay đổi của ocrBitmap để khởi động lại camera khi cần
    LaunchedEffect(ocrBitmap) {
        if (ocrBitmap == null && permissionGranted) {
            textureViewRef.value?.let { textureView ->
                viewModel.cameraHandler?.bindTextureView(textureView)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.cameraHandler?.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (permissionGranted) {
            if (ocrBitmap == null) {
                AndroidView(
                    factory = { context ->
                        TextureView(context).also { textureView ->
                            textureViewRef.value = textureView
                            viewModel.cameraHandler?.bindTextureView(textureView)
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { textureView ->
                        if (textureViewRef.value != textureView) {
                            textureViewRef.value = textureView
                            viewModel.cameraHandler?.bindTextureView(textureView)
                        }
                    }
                )
            } else {
                OcrOverlayImage(bitmap = ocrBitmap!!, lines = ocrLines)
            }
        }

        if (ocrBitmap == null && detectedText.isNotBlank()) {
            Text(
                text = detectedText,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.6f))
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        ) {
            IconButton(onClick = {
                galleryLauncher.launch("image/*")
            }) {
                Icon(Icons.Default.Image, contentDescription = "Chọn ảnh", tint = Color.White)
            }

            Canvas(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .size(72.dp)
                    .clickable {
                        viewModel.cameraHandler?.captureStillImage()
                    }
            ) {
                drawCircle(
                    color = Color.LightGray,
                    radius = size.minDimension / 2f
                )
                drawCircle(
                    color = Color.White,
                    radius = size.minDimension / 2.5f
                )
            }

            IconButton(onClick = {
                if (!viewModel.isTranslated) {
                    coroutineScope.launch {
                        viewModel.translateOverlayText()
                    }
                }
            }) {
                Icon(Icons.Default.Translate, contentDescription = "Dịch ảnh", tint = Color.White)
            }
        }

        IconButton(
            onClick = {
                if (ocrBitmap != null) {
                    viewModel.reset()
                    textureViewRef.value?.let { textureView ->
                        viewModel.cameraHandler?.bindTextureView(textureView)
                    }
                } else {
                    rootNavController.popBackStack()
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Quay lại",
                tint = Color.White
            )
        }

        if (ocrBitmap != null) {
            IconButton(
                onClick = {
                    viewModel.reset()
                    // Đảm bảo camera được khởi động lại ngay lập tức
                    textureViewRef.value?.let { textureView ->
                        viewModel.cameraHandler?.bindTextureView(textureView)
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Chụp lại", tint = Color.White)
            }
        }
    }
}