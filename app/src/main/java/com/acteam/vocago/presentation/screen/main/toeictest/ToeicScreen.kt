package com.acteam.vocago.presentation.screen.main.toeictest

import ToeicItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.main.toeictest.component.ToeicHeader
import com.acteam.vocago.utils.responsiveValue

@Composable
fun ToeicScreen(
    viewModel: ToeicViewModel,
    rootNavController: NavController,
) {
    val uiState by viewModel.toeicListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getToeicList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ToeicHeader()
        Spacer(modifier = Modifier.height(12.dp))

        when (val state = uiState) {
            is UIState.UILoading -> {
                LoadingSurface(picSize = responsiveValue(180, 360, 360))
            }

            is UIState.UISuccess -> {
                val dto = state.data
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                    items(dto.data) { test ->
                        ToeicItem(
                            title = test.title,
                            onStartTest = {
                                rootNavController.navigate(
                                    NavScreen.ToeicDetailNavScreen(
                                        test._id
                                    )
                                )
                            },
                            onViewResults = {
                                rootNavController.navigate(
                                    NavScreen.ToeicResultsNavScreen(test._id)
                                )
                            },
                            onPractice = {
                                rootNavController.navigate(
                                    NavScreen.ToeicPartSelectionNavScreen(
                                        test._id
                                    )
                                )
                            },
                        )
                    }
                }
            }

            is UIState.UIError -> {
            }
        }
    }
}
