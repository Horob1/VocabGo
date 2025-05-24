package com.acteam.vocago.presentation.screen.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveSP
import kotlinx.coroutines.delay

@Composable
fun ErrorBannerWithTimer(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    iconResId: Int,
    durationMillis: Long = 5_000L,
    onTimeout: () -> Unit,
    onDismiss: () -> Unit,
) {
    var progress by remember { mutableFloatStateOf(1f) }
    var visible by remember { mutableStateOf(true) }
    val deviceType = getDeviceType()
    val titleFontSize = responsiveSP(mobile = 26, tabletPortrait = 36, tabletLandscape = 42)
    val descFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)
    LaunchedEffect(Unit) {
        val steps = 100
        val delayMillis = durationMillis / steps
        for (i in 0..steps) {
            progress = 1f - i / steps.toFloat()
            delay(delayMillis)
        }
        visible = false
        onTimeout()
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(500)
        ) + fadeIn(animationSpec = tween(500)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(500)
        ) + fadeOut(animationSpec = tween(500)),
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = MaterialTheme.colorScheme.errorContainer,
            shadowElevation = 4.dp,
            tonalElevation = 4.dp,
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = iconResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 12.dp)
                                .clip(shape = RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Fit
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                                .padding(top = 4.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                fontSize = titleFontSize,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                textAlign = if(deviceType != DeviceType.Mobile) TextAlign.Center else TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = message,
                                fontSize = descFontSize,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                textAlign = if(deviceType != DeviceType.Mobile) TextAlign.Center else TextAlign.Start,
                                modifier = Modifier.padding(top = 4.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = MaterialTheme.colorScheme.error,
                        trackColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                    )
                }
            }
        }
    }
}