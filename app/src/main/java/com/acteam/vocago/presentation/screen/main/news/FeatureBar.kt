package com.acteam.vocago.presentation.screen.main.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.domain.model.FeatureBarItem
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.safeClickable

@Composable
fun FeatureBar(
    rootNavController: NavController,
) {
    val shadowColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    val items = listOf(
        FeatureBarItem.TranslateCam(onClick = {}),
        FeatureBarItem.BookMark(onClick = {}),
        FeatureBarItem.History(onClick = {}),
        FeatureBarItem.Dictionary(onClick = {}),
        FeatureBarItem.Setting(onClick = {
            rootNavController.navigate(NavScreen.SettingNavScreen)
        }),
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    color = shadowColor,
                    topLeft = Offset(0f, size.height),
                    size = Size(size.width, 1.dp.toPx())
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                FeatureItem(
                    item = item,
                    modifier = Modifier
                        .weight(1f)
                        .height(
                            responsiveDP(
                                mobile = 48,
                                tabletPortrait = 64,
                                tabletLandscape = 80
                            )
                        )
                        .padding(
                            responsiveDP(
                                mobile = 4,
                                tabletPortrait = 8,
                                tabletLandscape = 12
                            )
                        )
                )
            }
        }
    }

}

@Composable
fun FeatureItem(item: FeatureBarItem, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(
                MaterialTheme.shapes.medium
            )
            .safeClickable(key = "feature_item") { item.onItemClick() }
            .background(
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.3f
                ),
                shape = MaterialTheme.shapes.medium
            )
            .padding(responsiveDP(mobile = 4, tabletPortrait = 8, tabletLandscape = 12)),

        contentAlignment = Alignment.Center
    ) {
        val color = item.color ?: MaterialTheme.colorScheme.primary
        Icon(
            imageVector = item.icon,
            contentDescription = item.desc,
            tint = color
        )
    }
}