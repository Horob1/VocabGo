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
import androidx.compose.runtime.LaunchedEffect
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
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.LoginRequiredDialog
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.main.news.component.BigNewsCard
import com.acteam.vocago.presentation.screen.main.news.component.CheckInDialog
import com.acteam.vocago.presentation.screen.main.news.component.FeatureBar
import com.acteam.vocago.presentation.screen.main.news.component.FilterBar
import com.acteam.vocago.presentation.screen.main.news.component.FilterDialog
import com.acteam.vocago.presentation.screen.main.news.component.SmallNewsCard
import com.acteam.vocago.presentation.screen.main.news.component.UserBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    viewModel: NewsViewModel,
    rootNavController: NavController,
    navController: NavController,
) {
    val isAuth by viewModel.loginState.collectAsState()
    val newsPagingItems = viewModel.newsFlow.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()
    var isShowFilterDialog by remember { mutableStateOf(false) }
    val isRefreshing by remember(newsPagingItems.loadState.refresh) {
        derivedStateOf { newsPagingItems.loadState.refresh is LoadState.Loading }
    }
    var isShowLoginDialog by remember { mutableStateOf(false) }
    val showUserBar by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset < 50
        }
    }
    val pullRefreshState = rememberPullToRefreshState()
    var isShowCheckInDialog by remember { mutableStateOf(false) }
    val checkInState by viewModel.isCheckInState.collectAsState()

    LaunchedEffect(Unit) {
        if (isAuth) {
            viewModel.getUserRanking()
        }
    }

    LaunchedEffect(checkInState) {
        when (checkInState) {
            is UIState.UISuccess -> {
                val isCheckedInToday = (checkInState as UIState.UISuccess<Boolean>).data
                if (!isCheckedInToday) {
                    isShowCheckInDialog = true
                }
            }

            is UIState.UIError -> {
            }

            else -> Unit
        }
    }

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
            rootNavController = rootNavController,
            isAuth = isAuth,
            runOnUnAuth = {
                isShowLoginDialog = true
            }
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
                    key = { index ->
                        val item =
                            newsPagingItems.peek(index)
                        item?._id
                            ?: index
                    },
                    contentType = { index ->
                        if (index == 0) "big_news_card" else "small_news_card"
                    }
                ) { index ->
                    val item =
                        newsPagingItems[index]
                    item?.let { newsItem ->
                        if (index == 0) {
                            BigNewsCard(
                                news = newsItem,
                                onItemClick = {
                                    rootNavController.navigate(
                                        NavScreen.NewsDetailNavScreen(newsId = newsItem._id)
                                    )
                                },
                            )
                        } else {
                            SmallNewsCard(
                                news = newsItem,
                                onItemClick = {
                                    rootNavController.navigate(
                                        NavScreen.NewsDetailNavScreen(newsId = newsItem._id)
                                    )
                                }
                            )
                        }
                    }
                }

                if (newsPagingItems.loadState.append is LoadState.Loading) {
                    item {
                        LoadingSurface()
                    }
                }

                if (newsPagingItems.loadState.append is LoadState.Error) {
                    item {
                        //  Handle error state
                    }
                }
            }
        }
    }
    CheckInDialog(
        isVisible = isShowCheckInDialog,
        onDismiss = {
            isShowCheckInDialog = false
        },
        onCheckIn = {
            viewModel.checkIn()
        }
    )

    FilterDialog(
        isVisible = isShowFilterDialog,
        onDismiss = { isShowFilterDialog = false },
        viewModel = viewModel
    )

    if (isShowLoginDialog) {
        LoginRequiredDialog(
            onDismiss = { isShowLoginDialog = false },
            onConfirm = {
                isShowLoginDialog = false
                rootNavController.navigate(NavScreen.AuthNavScreen)
            }
        )
    }

}
