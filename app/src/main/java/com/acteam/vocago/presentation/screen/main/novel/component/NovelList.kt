package com.acteam.vocago.presentation.screen.main.novel.component

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.presentation.screen.common.EmptySurface
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun NovelList(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    novelList: List<Novel> = emptyList(),
    bgColorAlpha: Float = 0f,
    onNovelClick: (String) -> Unit = {},
    onGoToFullNovelList: () -> Unit = {},
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
                stringResource(title),
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
                onClick = onGoToFullNovelList,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to Novel list full",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (novelList.isEmpty()) {
            EmptySurface()
        }

        novelList.forEach { novel ->
            NovelCard(
                novel = novel,
                onClick = onNovelClick
            )
        }
    }
}