package com.acteam.vocago.presentation.screen.main.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.acteam.vocago.utils.safeClickable

@Composable
fun FilterBar(
    onFilterClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(
                12.dp
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val responsivePadding = 4.dp

        Box(
            modifier = Modifier
                .height(
                    40.dp
                )
                .clip(
                    MaterialTheme.shapes.large
                )
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.3f
                    ),
                    shape = MaterialTheme.shapes.large
                )
                .safeClickable(
                    key = "change_level",
                    onClick = {

                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier.size(responsivePadding)
                )
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(
                            responsivePadding
                        )
                        .clip(
                            MaterialTheme.shapes.medium
                        )
                ) {
                    Text(
                        "Level",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.padding(
                            responsivePadding
                        )
                    )
                }

                Spacer(
                    modifier = Modifier.size(
                        responsivePadding
                    )
                )

                Text(
                    "Basic",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(
                    modifier = Modifier.size(
                        responsivePadding
                    )
                )

            }
        }
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