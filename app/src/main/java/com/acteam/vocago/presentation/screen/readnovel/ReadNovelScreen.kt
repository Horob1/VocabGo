package com.acteam.vocago.presentation.screen.readnovel

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.acteam.vocago.data.model.NovelDetailDto
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState

@Composable
fun ReadNovelScreen(
    chapterId: String,
    novelId: String,
    viewModel: ReadNovelViewModel,
    rootNavController: NavController,
) {
    val chapters by viewModel.chapters.collectAsState()
    val novelDetail by viewModel.novelDetail.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    // Chặn nút back
    BackHandler {
        showExitDialog = true
    }

    LaunchedEffect(Unit) {
        if (novelDetail is UIState.UILoading) {
            viewModel.loadNovel(novelId)
        }
    }
    LaunchedEffect(novelDetail) {
        if (novelDetail is UIState.UISuccess) {
            viewModel.loadChapters(chapterId) {
                // toast error
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (novelDetail is UIState.UISuccess) {
                val novelData = remember {
                    (novelDetail as UIState.UISuccess<NovelDetailDto>).data
                }
                // Horizontally Pager
                val pagerState = rememberPagerState(
                    initialPage = novelData.chapters.indexOfFirst {
                        it._id == chapterId
                    },
                    pageCount = {
                        novelData.chapters.size
                    }
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { index ->
                    val _id = novelData.chapters[index]._id
                    val chapter = chapters.find { it.chapter._id == _id }
                    if (chapter != null) {
                        Text(text = chapter.chapter.content)
                    } else {
                        LoadingSurface(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                LaunchedEffect(pagerState.currentPage) {
                    viewModel.loadChapters(novelData.chapters[pagerState.currentPage]._id) {
                        // toast error
                    }
                }

            } else {
                LoadingSurface(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Xác nhận thoát") },
            text = { Text("Bạn có chắc chắn muốn thoát không?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    rootNavController.popBackStack()
                }) {
                    Text("Thoát")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

