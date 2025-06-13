package com.acteam.vocago.presentation.screen.main.toeictest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.acteam.vocago.presentation.screen.main.toeictest.component.TestItem
import com.acteam.vocago.presentation.screen.main.toeictest.component.ToeicHeader
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType

@Composable
fun ToeicScreen() {
    val deviceType = getDeviceType()
    val isTabletLandscape = deviceType == DeviceType.TabletLandscape

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ToeicHeader()

        LazyVerticalGrid(
            columns = if (isTabletLandscape) GridCells.Fixed(2) else GridCells.Fixed(1),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(4) { index ->
                TestItem(
                    test = TestData(title = "Sample Test #${index + 1}"),
                    onClick = {},
                    isOdd = index % 2 == 0
                )
            }
        }
    }
}

data class TestData(val title: String)
