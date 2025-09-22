package com.acteam.vocago.presentation.screen.user.ranking.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.data.model.RankingData

@Composable
fun RankingContent(
    rankingData: RankingData,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(300),
                    initialOffsetY = { -it }
                ) + fadeIn(animationSpec = tween(300))
            ) {
                PersonalStatsCard(userRanking = rankingData.self)
            }
        }

        item {
            Text(
                text = stringResource(R.string.ranking_top),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        itemsIndexed(rankingData.top8) { index, user ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    animationSpec = tween(400 + index * 100),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(400 + index * 100))
            ) {
                RankingItem(
                    rank = index + 1,
                    userRanking = user,
                    isCurrentUser = user.userId == rankingData.self.userId
                )
            }
        }
    }
}
