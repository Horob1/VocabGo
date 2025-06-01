package com.acteam.vocago.presentation.screen.common

import android.graphics.Shader
import android.os.Build
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.acteam.vocago.R


@Composable
fun LoadingSurface(
    modifier: Modifier = Modifier,
    picSize: Int = 180,
) {
    val context = LocalContext.current
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, tween(500))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Nền mờ (chỉ nền)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        renderEffect = android.graphics.RenderEffect
                            .createBlurEffect(6f, 6f, Shader.TileMode.CLAMP)
                            .asComposeRenderEffect()
                    }
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
            )
        } else {
            // Fake blur bằng overlay nếu cần
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
            )
        }

        // Hộp loading ở trung tâm
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                        shape = CircleShape
                        clip = true
                    }
                    .size(picSize.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.loading)
                        .decoderFactory(GifDecoder.Factory())
                        .size(Size(picSize, picSize))
                        .build(),
                    contentDescription = "Loading...",
                    modifier = Modifier
                        .size(picSize.dp)
                        .clip(CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(picSize.dp + 6.dp)
                        .align(Alignment.Center),
                    strokeWidth = 8.dp,
                )
            }

        }
    }
}
