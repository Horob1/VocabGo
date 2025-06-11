package com.acteam.vocago.presentation.screen.main.chat.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.acteam.vocago.R
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType

@Composable
fun ThinTopBar(
    title: String,
    onBackClick: () -> Unit,
    avatarRes: Int? = null,
) {
    val deviceType = getDeviceType()

    val topBarHeight = when (deviceType) {
        DeviceType.Mobile -> 64.dp
        DeviceType.TabletPortrait -> 72.dp
        DeviceType.TabletLandscape -> 80.dp
    }

    val rowHeight = when (deviceType) {
        DeviceType.Mobile -> 48.dp
        DeviceType.TabletPortrait -> 56.dp
        DeviceType.TabletLandscape -> 64.dp
    }

    val avatarSize = when (deviceType) {
        DeviceType.Mobile -> 36.dp
        DeviceType.TabletPortrait -> 44.dp
        DeviceType.TabletLandscape -> 52.dp
    }

    val iconSize = avatarSize

    val titleTextStyle = when (deviceType) {
        DeviceType.Mobile -> MaterialTheme.typography.titleMedium
        DeviceType.TabletPortrait -> MaterialTheme.typography.titleLarge
        DeviceType.TabletLandscape -> MaterialTheme.typography.headlineSmall
    }


    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .height(topBarHeight)
            .drawBehind {
                val shadowColor = Color.Black.copy(alpha = 0.2f)
                val shadowHeight = if (deviceType == DeviceType.Mobile) 1.dp.toPx() else 4.dp.toPx()

                drawRect(
                    color = shadowColor,
                    topLeft = Offset(0f, size.height),
                    size = Size(size.width, shadowHeight)
                )
            }
    ) {
        Row(
            modifier = Modifier
                .height(rowHeight)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(iconSize)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = rememberAsyncImagePainter(
                    model = avatarRes ?: R.drawable.capybara_avatar
                ),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                style = titleTextStyle.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                maxLines = 1,
            )
        }
    }
}
