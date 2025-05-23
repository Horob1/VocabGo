package com.acteam.vocago.presentation.screen.main.novel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun NovelList(
    modifier: Modifier = Modifier,
    bgColorAlpha: Float = 0f,
) {
    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = bgColorAlpha
                )
            )
            .padding(
                bottom = responsiveDP(
                    mobile = 8,
                    tabletPortrait = 12,
                    tabletLandscape = 12
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    responsiveDP(
                        mobile = 8,
                        tabletPortrait = 12,
                        tabletLandscape = 12
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Novel List",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = responsiveSP(
                        mobile = 16,
                        tabletPortrait = 18,
                        tabletLandscape = 20
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                ),
            )

            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to Novel list full",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        NovelCard()
        NovelCard()
        NovelCard()
    }

}