package com.acteam.vocago.presentation.screen.main.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    viewModel: NewsViewModel,
    rootNavController: NavController,
    navController: NavController,
) {
    val newsPagingItems = viewModel.newsFlow.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()
    var isShowFilterDialog by remember { mutableStateOf(false) }
    val isRefreshing = newsPagingItems.loadState.refresh is LoadState.Loading
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
            UserBar(
                viewModel = viewModel,
                navigateToProfile = {
                    rootNavController.navigate(NavScreen.UserNavScreen)
                },
                navigateToLogin = {
                    rootNavController.navigate(NavScreen.AuthNavScreen)
                },
            )
        }

        FeatureBar(
            rootNavController = rootNavController
        )

        PullToRefreshBox(
            state = pullRefreshState,
            onRefresh = { newsPagingItems.refresh() },
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
                        viewModel = viewModel,
                        onFilterClick = { isShowFilterDialog = true }
                    )
                }

                items(
                    count = newsPagingItems.itemCount,
                    key = {
                        UUID.randomUUID().toString()
                    },
                ) { index ->
                    val item = newsPagingItems[index]
                    item?.let {
                        if (index == 0) {
                            BigNewsCard(
                                news = item,
                                onItemClick = {
                                    rootNavController.navigate(
                                        NavScreen.NewsDetailNavScreen(newsId = item._id)
                                    )
                                },
                            )
                        } else {
                            SmallNewsCard(
                                news = item,
                                onItemClick = {
                                    rootNavController.navigate(
                                        NavScreen.NewsDetailNavScreen(newsId = item._id)
                                    )
                                }
                            )
                        }
                    }
                }

                item {
                    when (newsPagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            LoadingSurface()
                        }

                        else -> {}
                    }
                }
            }
        }
    }



    FilterDialog(
        isVisible = isShowFilterDialog,
        onDismiss = { isShowFilterDialog = false },
        viewModel = viewModel
    )

}
