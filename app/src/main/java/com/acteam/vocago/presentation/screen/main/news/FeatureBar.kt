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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.domain.model.FeatureBarItem
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.safeClickable

@Composable
fun FeatureBar(
    rootNavController: NavController,
    shadowColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
) {
    val horizontalPadding = responsiveDP(
        mobile = 16,
        tabletPortrait = 24,
        tabletLandscape = 32
    )
    val verticalPadding = responsiveDP(
        mobile = 8,
        tabletPortrait = 12,
        tabletLandscape = 16
    )
    val itemHeight = responsiveDP(
        mobile = 48,
        tabletPortrait = 64,
        tabletLandscape = 80
    )
    val itemPadding = responsiveDP(
        mobile = 4,
        tabletPortrait = 8,
        tabletLandscape = 12
    )
    val items = remember {
        listOf(
            FeatureBarItem.TranslateCam(onClick = {
                rootNavController.navigate(NavScreen.CameraNavScreen)
            }),
            FeatureBarItem.BookMark(onClick = {
                rootNavController.navigate(NavScreen.NewsHistoryNavScreen(isBookmark = true))
            }),
            FeatureBarItem.History(onClick = {
                rootNavController.navigate(NavScreen.NewsHistoryNavScreen(isBookmark = false))
            }),
            FeatureBarItem.Dictionary(onClick = {
                rootNavController.navigate(NavScreen.DictionaryNavScreen)
            }),
            FeatureBarItem.Setting(onClick = {
                rootNavController.navigate(NavScreen.SettingNavScreen)
            }),
        )
    }
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
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
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
                            itemHeight
                        )
                        .padding(
                            itemPadding
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