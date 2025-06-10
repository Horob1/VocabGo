package com.acteam.vocago.presentation.screen.main.news.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.NewsLevel
import com.acteam.vocago.presentation.screen.main.news.NewsViewModel
import com.acteam.vocago.utils.safeClickable

@Composable
fun FilterBar(
    viewModel: NewsViewModel,
    onFilterClick: () -> Unit,
) {
    val level by viewModel.chosenLevel.collectAsState()

    val containerColor = when (level) {
        NewsLevel.EASY -> Color.Green.copy(
            alpha = 0.5f
        )

        NewsLevel.MEDIUM -> Color.Yellow.copy(
            alpha = 0.5f
        )

        NewsLevel.HARD -> Color.Red.copy(
            alpha = 0.5f
        )

        NewsLevel.ALL -> MaterialTheme.colorScheme.primary
    }
    val contentColor = when (level) {
        NewsLevel.EASY -> Color.Black
        NewsLevel.MEDIUM -> Color.Black
        NewsLevel.HARD -> Color.Black
        NewsLevel.ALL -> MaterialTheme.colorScheme.surface
    }

    val nextLevel = when (level) {
        NewsLevel.EASY -> NewsLevel.MEDIUM
        NewsLevel.MEDIUM -> NewsLevel.HARD
        NewsLevel.HARD -> NewsLevel.ALL
        NewsLevel.ALL -> NewsLevel.EASY
    }
    Row(
        modifier = Modifier
            .padding(
                12.dp
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        LevelSelector(
            level = level,
            nextLevel = nextLevel,
            onLevelChange = {
                viewModel.updateChosenLevel(it)
            },
            containerColor = containerColor,
            contentColor = contentColor
        )

        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .height(
                    40.dp
                )
                .width(
                    40.dp
                )
                .clip(
                    CircleShape
                )
                .safeClickable(
                    key = "filter",
                    onClick = {
                        onFilterClick()
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Tune,
                contentDescription = "Filter",
                tint = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
fun LevelSelector(
    level: NewsLevel,
    nextLevel: NewsLevel,
    onLevelChange: (NewsLevel) -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                onClick = { onLevelChange(nextLevel) }
            ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // "Level" badge
            Surface(
                color = containerColor,
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 1.dp
            ) {
                Text(
                    text = stringResource(R.string.text_level),
                    color = contentColor,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // Level value
            Text(
                text = stringResource(id = level.stringInt),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.widthIn(min = 68.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}