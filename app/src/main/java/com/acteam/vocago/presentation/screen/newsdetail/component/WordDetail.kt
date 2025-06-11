package com.acteam.vocago.presentation.screen.newsdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WordDetail(
    modifier: Modifier = Modifier,
) {
    val levels: List<Pair<String, Color>> = remember {
        listOf(
            Pair("A1", Color(0xFF81C784)),
            Pair("A2", Color(0xFF64B5F6)),
            Pair("B1", Color(0xFFFFF176)),
            Pair("B2", Color(0xFFEF9A9A))
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        levels.forEachIndexed { index, (label, color) ->
            LevelItem(label = label, color = color)
            if (index != levels.lastIndex) {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
fun LevelItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(
                    color = color,
                    shape = MaterialTheme.shapes.extraSmall
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )
    }
}