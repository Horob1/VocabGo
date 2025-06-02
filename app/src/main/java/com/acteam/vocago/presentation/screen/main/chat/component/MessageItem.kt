import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.acteam.vocago.presentation.screen.main.chat.data.MessageModel
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType

@Composable
fun MessageItem(
    message: MessageModel,
    userAvatarUrl: String? = null,
    userDefautlAvatar: Int,
    chatbotAvatar: Int
) {
    val deviceType = getDeviceType()

    val avatarSize = when (deviceType) {
        DeviceType.Mobile -> 32.dp
        DeviceType.TabletPortrait -> 40.dp
        DeviceType.TabletLandscape -> 48.dp
    }

    val bubblePadding = when (deviceType) {
        DeviceType.Mobile -> 12.dp
        DeviceType.TabletPortrait -> 16.dp
        DeviceType.TabletLandscape -> 20.dp
    }

    when (deviceType) {
        DeviceType.Mobile -> 280.dp
        DeviceType.TabletPortrait -> 380.dp
        DeviceType.TabletLandscape -> 500.dp
    }

    val textStyle = when (deviceType) {
        DeviceType.Mobile -> MaterialTheme.typography.bodyMedium
        DeviceType.TabletPortrait -> MaterialTheme.typography.bodyLarge
        DeviceType.TabletLandscape -> MaterialTheme.typography.titleMedium
    }

    val alignment = if (message.isModel) Arrangement.Start else Arrangement.End
    val bubbleColor = if (message.isModel) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.primary
    }
    val textColor = if (message.isModel) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = alignment,
        verticalAlignment = Alignment.Bottom // ✅ Cần đáy khớp
    ) {
        if (message.isModel) {
            Box(
                modifier = Modifier.align(Alignment.Bottom) // ✅ Avatar canh đáy
            ) {
                Image(
                    painter = painterResource(id = chatbotAvatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }

        Box(
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                .background(bubbleColor, RoundedCornerShape(12.dp))
                .padding(bubblePadding)
        ) {
            Text(
                text = message.message,
                color = textColor,
                style = textStyle
            )
        }

        if (!message.isModel) {
            Spacer(modifier = Modifier.width(4.dp))

            val painter = if (!userAvatarUrl.isNullOrBlank()) {
                rememberAsyncImagePainter(model = userAvatarUrl)
            } else {
                painterResource(id = userDefautlAvatar)
            }

            Box(
                modifier = Modifier.align(Alignment.Bottom) // ✅ Avatar canh đáy
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }

}
