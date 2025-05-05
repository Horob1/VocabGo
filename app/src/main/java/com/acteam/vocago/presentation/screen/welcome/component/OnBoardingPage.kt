package com.acteam.vocago.presentation.screen.welcome.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.acteam.vocago.presentation.screen.welcome.data.OnBoardingPageData
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveFontSize
import com.acteam.vocago.utils.responsivePadding
import com.acteam.vocago.utils.responsiveSpacing

@Composable
fun PagerScreen(onBoardingPageData: OnBoardingPageData) {
    val deviceType = getDeviceType()

    val titleFontSize = responsiveFontSize(mobile = 18, tablet = 24)
    val descFontSize = responsiveFontSize(mobile = 14, tablet = 18)
    val horizontalPadding = responsivePadding(mobile = 24, tablet = 40)
    val topPadding = responsivePadding(mobile = 16, tablet = 24)
    val verticalSpacing = responsiveSpacing(mobile = 12, tablet = 20)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = verticalSpacing)
    ) {
        Spacer(modifier = Modifier.height(verticalSpacing))

        if (deviceType == DeviceType.Mobile) {
            OnBoardingImageCard(onBoardingPageData.image)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = onBoardingPageData.title),
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding)
                    .padding(top = topPadding),
                text = stringResource(id = onBoardingPageData.description),
                fontSize = descFontSize,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = responsivePadding(mobile = 12, tablet = 24))
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OnBoardingImageCard(onBoardingPageData.image, 0.5f, 0.8f)

                Column(
                    modifier = Modifier.fillMaxWidth(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = onBoardingPageData.title),
                        fontSize = titleFontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding)
                            .padding(top = topPadding),
                        text = stringResource(id = onBoardingPageData.description),
                        fontSize = descFontSize,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}