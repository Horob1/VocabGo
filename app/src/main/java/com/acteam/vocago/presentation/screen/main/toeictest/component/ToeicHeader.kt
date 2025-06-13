package com.acteam.vocago.presentation.screen.main.toeictest.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveSP

@Composable
fun ToeicHeader() {
    val deviceType = getDeviceType()

    val headerHeight = when (deviceType) {
        DeviceType.Mobile -> 60.dp
        DeviceType.TabletPortrait -> 90.dp
        DeviceType.TabletLandscape -> 80.dp
    }

    val horizontalPadding = when (deviceType) {
        DeviceType.Mobile -> 16.dp
        DeviceType.TabletPortrait -> 32.dp
        DeviceType.TabletLandscape -> 24.dp
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = horizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "TOEIC Test",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = responsiveSP(
                        mobile = 20,
                        tabletPortrait = 22,
                        tabletLandscape = 24
                    ),
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Practice and challenge yourself!",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Start
            )
        }
    }
}
