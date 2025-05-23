package com.acteam.vocago.presentation.screen.main.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen() {
    val deviceType = getDeviceType()
    val newsItems = remember {
        mutableStateListOf(
            0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
        )
    }
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    var isShowFilterDialog by remember {
        mutableStateOf(false)
    }

    fun refresh() {
        refreshScope.launch {
            isRefreshing = true
            delay(1500)
            newsItems.add(0) // Thêm một mục mới vào đầu
            isRefreshing = false
        }
    }

    val showUserBar by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset < 50
        }
    }


    val pullRefreshState = rememberPullToRefreshState()
    Column {
        AnimatedVisibility(
            visible = showUserBar,
        ) {
            UserBar(navigateToProfile = {}, navigateToLogin = {})
        }
        FeatureBar()
        PullToRefreshBox(
            state = pullRefreshState,
            onRefresh = { refresh() },
            isRefreshing = isRefreshing,
            modifier = Modifier.fillMaxSize(),
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    state = pullRefreshState
                )
            }
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {
                item {
                    FilterBar(
                        onFilterClick = {
                            isShowFilterDialog = true
                        }
                    )
                }
                if (deviceType === DeviceType.TabletLandscape) {
                    item {
                        Row {
                            // index 0
                            BigNewsCard(
                                modifier = Modifier.weight(1f)
                            )
                            // index 1
                            BigNewsCard(
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    items(newsItems.size - 2 / 2) { _ ->
                        Row {
                            // odd index
                            SmallNewsCard(
                                modifier = Modifier.weight(1f)
                            )
                            // even index
                            SmallNewsCard(
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    item {
                        BigNewsCard()
                    }
                    items(newsItems.size - 1) { _ ->
                        SmallNewsCard()
                    }
                }

            }
        }
    }

    FilterDialog(
        isVisible = isShowFilterDialog,
        onDismiss = { isShowFilterDialog = false },
        onSearch = { }
    )
}
