package com.acteam.vocago.presentation.screen.main.novel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NovelScreen() {
    val deviceType = getDeviceType()
    LazyColumn {
        stickyHeader {
            Row(
                Modifier
                    .background(
                        MaterialTheme.colorScheme.background
                    )
                    .padding(
                        horizontal = responsiveDP(
                            mobile = 16,
                            tabletPortrait = 24,
                            tabletLandscape = 32
                        ),
                        vertical = responsiveDP(
                            mobile = 8,
                            tabletPortrait = 12,
                            tabletLandscape = 16
                        )
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Novel Center", style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = responsiveSP(
                                mobile = 20,
                                tabletPortrait = 22,
                                tabletLandscape = 24
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(
                        modifier = Modifier.size(
                            responsiveDP(
                                mobile = 4,
                                tabletPortrait = 8,
                                tabletLandscape = 12
                            )
                        )
                    )
                    Text(
                        "Learn voca by reading novel",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                0.7f
                            )
                        )
                    )
                }
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "key_search",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
        if (deviceType == DeviceType.TabletLandscape) {
            item {
                TabletCarousel()
            }
        } else {
            item {
                NovelCarousel()
            }
        }
        if (
            deviceType == DeviceType.Mobile
        ) {
            item {
                NovelList(
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                NovelList(
                    modifier = Modifier.fillMaxWidth(),
                    bgColorAlpha = 0.3f
                )
            }
        } else {

            item {
                HorizontalDivider()
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    NovelList(
                        modifier = Modifier.weight(1f)
                    )

                    NovelList(
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

    }
}