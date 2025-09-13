package com.acteam.vocago.presentation.screen.readnovel.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acteam.vocago.R

@Composable
fun FontSizeSelector(
    selectedSize: Float,
    onSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    minValue: Float = 12f,
    maxValue: Float = 30f,
    step: Float = 2f,
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = stringResource(R.string.font_size),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        // Tạo danh sách các kích thước font
        val fontSizes = buildList {
            var current = minValue
            while (current <= maxValue) {
                add(current)
                current += step
            }
        }

        // Phiên bản 1: Sử dụng Row với các nút bấm
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .selectableGroup()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            fontSizes.forEach { fontSize ->
                val isSelected = fontSize == selectedSize

                FilterChip(
                    onClick = { onSizeChange(fontSize) },
                    label = {
                        Text(
                            text = "${fontSize.toInt()}",
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selected = isSelected,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.height(32.dp)
                )
            }
        }
    }
}

// Phiên bản 2: Sử dụng LazyRow nếu có quá nhiều tùy chọn
@Composable
fun FontSizeSelectorLazy(
    selectedSize: Float,
    onSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    minValue: Float = 12f,
    maxValue: Float = 30f,
    step: Float = 1f,
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = stringResource(R.string.font_size),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        val fontSizes = buildList {
            var current = minValue
            while (current <= maxValue) {
                add(current)
                current += step
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(fontSizes.size) { index ->
                val fontSize = fontSizes[index]
                val isSelected = fontSize == selectedSize

                FilterChip(
                    onClick = { onSizeChange(fontSize) },
                    label = {
                        Text(
                            text = "${fontSize.toInt()}",
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selected = isSelected,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.height(30.dp)
                )
            }
        }
    }
}

// Phiên bản 3: Sử dụng nút +/- với hiển thị giá trị hiện tại
@Composable
fun FontSizeAdjuster(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    minValue: Float = 12f,
    maxValue: Float = 30f,
    step: Float = 2f,
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = stringResource(R.string.font_size),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            // Nút giảm
            IconButton(
                onClick = {
                    val newValue = (value - step).coerceAtLeast(minValue)
                    onValueChange(newValue)
                },
                enabled = value > minValue
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Giảm cỡ chữ",
                    tint = if (value > minValue) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }

            // Hiển thị giá trị hiện tại
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "${value.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Nút tăng
            IconButton(
                onClick = {
                    val newValue = (value + step).coerceAtMost(maxValue)
                    onValueChange(newValue)
                },
                enabled = value < maxValue
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tăng cỡ chữ",
                    tint = if (value < maxValue) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }
        }
    }
}