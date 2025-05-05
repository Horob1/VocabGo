package com.acteam.vocago.presentation.screen.welcome.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun PagerScreen(onBoardingPageData: OnBoardingPageData) {
    val deviceType = getDeviceType()

    val titleFontSize = responsiveSP(mobile = 18, tabletPortrait = 24, tabletLandscape = 28)
    val descFontSize = responsiveSP(mobile = 14, tabletPortrait = 18, tabletLandscape = 20)
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val topPadding = responsiveDP(mobile = 16, tabletPortrait = 24, tabletLandscape = 32)
    val verticalSpacing = responsiveDP(mobile = 12, tabletPortrait = 20, tabletLandscape = 24)

    if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
        Column(
            modifier = Modifier
                .padding(top = topPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = verticalSpacing)
        ) {
            Spacer(modifier = Modifier.height(verticalSpacing))

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
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding)
                .padding(horizontal = horizontalPadding),
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