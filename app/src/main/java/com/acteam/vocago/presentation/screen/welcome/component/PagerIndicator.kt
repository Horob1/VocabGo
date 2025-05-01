package com.acteam.vocago.presentation.screen.welcome.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage

            val color by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                label = "indicatorColor"
            )

            val size by animateDpAsState(
                targetValue = if (isSelected) 12.dp else 8.dp,
                label = "indicatorSize"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(size)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
