package com.acteam.vocago.presentation.screen.searchnovel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.main.novel.component.NovelCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNovelScreen(
    viewModel: SearchNovelViewModel,
    navController: NavController,
    keySearch: String,
) {

    val currentKeySearch by viewModel.keySearch.collectAsState()
    val novelPaging = viewModel.novelPagingFlow.collectAsLazyPagingItems()
    val pullRefreshState = rememberPullToRefreshState()
    val lazyListState = rememberLazyListState()

    val navigateKeySearch = remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        if (keySearch != currentKeySearch) {
            viewModel.setKeySearch(keySearch)
            navigateKeySearch.value = keySearch
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = navigateKeySearch.value,
                onValueChange = { navigateKeySearch.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("${stringResource(R.string.text_search)} ...") },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            if (navigateKeySearch.value.isNotEmpty() && navigateKeySearch.value != currentKeySearch)
                                navController.navigate(
                                    NavScreen.SearchNovelNavScreen(navigateKeySearch.value)
                                )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (navigateKeySearch.value.isNotEmpty() && navigateKeySearch.value != currentKeySearch)
                            navController.navigate(
                                NavScreen.SearchNovelNavScreen(navigateKeySearch.value)
                            )
                    }
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                )
            )


            PullToRefreshBox(
                state = pullRefreshState,
                onRefresh = { novelPaging.refresh() },
                isRefreshing = novelPaging.loadState.refresh is LoadState.Loading,
                modifier = Modifier.fillMaxSize(),
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = novelPaging.loadState.refresh is LoadState.Loading,
                        state = pullRefreshState
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState
                ) {

                    items(
                        count = novelPaging.itemCount,
                        key = { index ->
                            val item =
                                novelPaging.peek(index)
                            item?._id
                                ?: index
                        },
                        contentType = { "Novel card" }
                    ) { index ->
                        val item = novelPaging[index]

                        NovelCard(
                            novel = item!!,
                            onClick = {
                                navController.navigate(
                                    NavScreen.NovelDetailNavScreen(item._id)
                                )
                            }
                        )

                    }

                    if (novelPaging.loadState.append is LoadState.Loading) {
                        item {
                            LoadingSurface()
                        }
                    }

                    if (novelPaging.loadState.append is LoadState.Error) {
                        item {

                        }
                    }
                }
            }
        }
    }
}

