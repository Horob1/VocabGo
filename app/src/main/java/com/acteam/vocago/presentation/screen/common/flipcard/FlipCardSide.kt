package com.acteam.vocago.presentation.screen.common.flipcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FlipCardSize(
    modifier: Modifier = Modifier,
    height: Int,
    content: @Composable () -> Unit = {},
    color: Color = MaterialTheme.colorScheme.surface,
) {
    Box {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(height.dp),
            shape = RoundedCornerShape(20.dp),
            color = color
        ) {
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }
}




















