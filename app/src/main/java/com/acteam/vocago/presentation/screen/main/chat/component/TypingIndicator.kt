package com.acteam.vocago.presentation.screen.main.chat.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.acteam.vocago.R
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import kotlinx.coroutines.delay


@Composable
fun TypingIndicator(
    avatar: Any? = R.drawable.capybara_avatar,
    chatbotName: String, // vẫn giữ tham số nếu muốn dùng sau
) {
    val deviceType = getDeviceType()

    val avatarSize = when (deviceType) {
        DeviceType.Mobile -> 32.dp
        DeviceType.TabletPortrait -> 40.dp
        DeviceType.TabletLandscape -> 48.dp
    }

    val horizontalPadding = when (deviceType) {
        DeviceType.Mobile -> 12.dp
        DeviceType.TabletPortrait -> 16.dp
        DeviceType.TabletLandscape -> 20.dp
    }

    var dotCount by remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500L)
            dotCount = if (dotCount == 5) 1 else dotCount + 1
        }
    }

    val dots = ".".repeat(dotCount)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = avatar),
            contentDescription = "Typing avatar",
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = dots,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

