package com.acteam.vocago.presentation.screen.main.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.acteam.vocago.presentation.screen.common.EmptySurface
import com.acteam.vocago.presentation.screen.common.LoadingSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    viewModel: NewsViewModel,
    rootNavController: NavController,
    navController: NavController,
) {
    val isAuth = viewModel.loginState.collectAsState()
    val userState = viewModel.userState.collectAsState()
    val chosenCategories = viewModel.chosenCategories.collectAsState()
    val chosenLevel = viewModel.chosenLevel.collectAsState()
    val newsPagingItems = viewModel.newsFlow.collectAsLazyPagingItems()
    val keySearch = viewModel.keySearch.collectAsState()

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
                isAuth = isAuth.value,
                userState = userState.value,
                navigateToProfile = {},
                navigateToLogin = {
                    rootNavController.navigate(NavScreen.AuthNavScreen)
                },
                onLoadProfile = {
                    viewModel.syncProfile()
                }
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
                        level = chosenLevel.value,
                        onLevelChange = { viewModel.updateChosenLevel(it) },
                        onFilterClick = { isShowFilterDialog = true }
                    )
                }


                val isEmpty =
                    newsPagingItems.itemCount == 0 &&
                            newsPagingItems.loadState.append.endOfPaginationReached &&
                            newsPagingItems.loadState.refresh is LoadState.NotLoading

                if (isEmpty) item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptySurface()
                    }
                } else {
                    items(newsPagingItems.itemCount) { index ->
                        val item = newsPagingItems[index]
                        if (index == 0) {
                            BigNewsCard(
                                news = item!!,
                                onItemClick = {

                                }
                            )
                        } else {
                            SmallNewsCard(
                                news = item!!,
                                onItemClick = {

                                }
                            )
                        }
                    }
                }

                // Load more indicator
                item {
                    when (newsPagingItems.loadState.append) {
                        is LoadState.Loading -> LoadingSurface()
                        else -> {}
                    }
                }
            }
        }
    }

    FilterDialog(
        isVisible = isShowFilterDialog,
        onDismiss = { isShowFilterDialog = false },
        onSearch = {
            viewModel.setKeySearch(it)
            isShowFilterDialog = false
        },
        chosenCategory = chosenCategories.value,
        search = keySearch.value,
        onUpdateChosenCategory = {
            viewModel.updateChosenCategories(it)
        },
    )
}
