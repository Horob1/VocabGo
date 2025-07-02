package com.acteam.vocago.presentation.screen.noveldetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.noveldetail.component.StoryCard
import com.acteam.vocago.utils.DateDisplayHelper
import java.util.Locale

@Composable
fun NovelDetailScreen(
    novelId: String,
    viewModel: NovelDetailViewModel,
    navController: NavController,
) {
    val novelDetail by viewModel.novelDetail.collectAsState()

    LaunchedEffect(Unit) {
        if (novelDetail !is UIState.UISuccess) {
            viewModel.loadNovel(novelId)
        }
    }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf(R.string.chapters)

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = novelDetail is UIState.UISuccess,
                enter = slideInVertically { it },
            ) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        // Nút Download
//                        Box(
//                            modifier = Modifier
//                                .weight(1f)
//                                .background(MaterialTheme.colorScheme.primary)
//                                .clickable {
//
//                                }
//                                .padding(16.dp),
//
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Icon(
//                                    imageVector = Icons.Filled.DownloadForOffline,
//                                    contentDescription = "Download",
//                                    tint = MaterialTheme.colorScheme.onPrimary
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(
//                                    stringResource(R.string.btn_dowload),
//                                    color = MaterialTheme.colorScheme.onPrimary
//                                )
//                            }
//                        }

                        // Nút Đọc
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {

                                }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.LibraryBooks,
                                    contentDescription = "Read",
                                    tint = MaterialTheme.colorScheme.onSurface // Tuỳ chỉnh màu icon khác
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    stringResource(R.string.btn_read),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        when (novelDetail) {
            is UIState.UILoading -> {
                LoadingSurface()
            }

            is UIState.UISuccess -> {
                val novel = (novelDetail as UIState.UISuccess).data
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                ) {
                    StoryCard(
                        title = novel.fictionTitle,
                        author = novel.author,
                        imageUrl = novel.image,
                        onBackClick = { navController.popBackStack() },
                        onReadClick = {

                        }
                    )
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(text = stringResource(title).uppercase()) }
                            )
                        }
                    }

                    when (selectedTabIndex) {
                        0 -> {
                            val sort = remember {
                                mutableStateOf(false)
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        16.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        stringResource(R.string.chapters).replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.getDefault()
                                            ) else it.toString()
                                        },
                                        style = MaterialTheme.typography.titleSmall
                                    )

                                    Text(
                                        " (${novel.chapters.size})"
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        sort.value = !sort.value
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (sort.value) Icons.AutoMirrored.Filled.FormatAlignRight else Icons.AutoMirrored.Filled.FormatAlignLeft,
                                        contentDescription = "Sort"
                                    )
                                }
                            }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {

                                items(
                                    novel.chapters.size,
                                    key = { index -> novel.chapters[index]._id }) { currentIndex ->
                                    val index =
                                        if (sort.value) novel.chapters.size - 1 - currentIndex else currentIndex
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable { }
                                            .padding(vertical = 8.dp, horizontal = 16.dp)
                                    ) {
                                        Text(
                                            text = novel.chapters[index].chapterNumber.toString()
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(
                                            Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = "${
                                                    stringResource(R.string.chapters).replaceFirstChar {
                                                        if (it.isLowerCase()) it.titlecase(
                                                            Locale.getDefault()
                                                        ) else it.toString()
                                                    }
                                                } ${novel.chapters[index].chapterNumber}: ${novel.chapters[index].chapterTitle}")
                                            Text("(${DateDisplayHelper.formatDateString(dateString = novel.chapters[index].createdAt)})")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is UIState.UIError -> {
                // Xử lý lỗi
            }
        }
    }
}

