package com.acteam.vocago.presentation.screen.newshistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.EmptySurface
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.newshistory.component.NewsHistoryItemCard
import com.acteam.vocago.presentation.screen.newshistory.component.NewsHistoryTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsHistoryScreen(
    viewModel: NewsHistoryViewModel,
    rootNavController: NavController,
) {
    val newsHistory = viewModel.newsHistory.collectAsLazyPagingItems()
    val isBookmark by viewModel.isBookmark.collectAsState()

    val pullRefreshState = rememberPullToRefreshState()
    val isRefreshing by remember(newsHistory.loadState.refresh) {
        derivedStateOf { newsHistory.loadState.refresh is LoadState.Loading }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NewsHistoryTopBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                isBookmark = isBookmark,
                onToggleBookmark = {
                    viewModel.setIsBookmark(!isBookmark)
                },
                onBackClick = {
                    rootNavController.popBackStack()
                }
            )
            PullToRefreshBox(
                state = pullRefreshState,
                onRefresh = { newsHistory.refresh() },
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
                    modifier = Modifier.fillMaxSize()
                ) {


                    items(
                        count = newsHistory.itemCount,
                        key = { index ->
                            val item =
                                newsHistory.peek(index)
                            item?._id
                                ?: index
                        },
                        contentType = { "history_item" }
                    ) { index ->
                        val item =
                            newsHistory[index]
                        item?.let { newsItem ->
                            if ((isBookmark && newsItem.isBookmarked) || !isBookmark)
                                NewsHistoryItemCard(
                                    newsHistory = newsItem,
                                    onItemClick = {
                                        rootNavController.navigate(
                                            NavScreen.NewsDetailNavScreen(newsId = newsItem.news._id)
                                        )
                                    }
                                )
                        }
                    }



                    if (newsHistory.loadState.append is LoadState.Loading) {
                        item {
                            LoadingSurface()
                        }
                    }

                    if (newsHistory.loadState.append is LoadState.Error) {
                        item {
                            //  Handle error state
                        }
                    }

                    if (
                        newsHistory.loadState.refresh is LoadState.NotLoading &&
                        ((newsHistory.itemCount == 0 && !isBookmark) || (newsHistory.itemSnapshotList.count { it?.isBookmarked == true } == 0 && isBookmark))
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize(), // Chiếm hết phần còn lại của LazyColumn
                                contentAlignment = Alignment.Center
                            ) {
                                EmptySurface()
                            }
                        }
                    }
                }
            }
        }

    }


}

