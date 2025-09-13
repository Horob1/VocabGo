package com.acteam.vocago.presentation.screen.noveldetail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.main.novel.component.DownloadDialog
import com.acteam.vocago.presentation.screen.noveldetail.component.StoryCard
import com.acteam.vocago.utils.DateDisplayHelper
import java.util.Locale

@Composable
fun NovelDetailScreen(
    novelId: String,
    viewModel: NovelDetailViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val novelDetail by viewModel.novelDetail.collectAsState()
    val readChapter by viewModel.readChapter.collectAsState()
    val localNovel by viewModel.getLocalChapterFlowUseCase(novelId)
        .collectAsState(initial = emptyList())
    val isBind by viewModel.isBind.collectAsState()
    val downloadState by viewModel.downloadState.collectAsState()
    LaunchedEffect(isBind) {
        if (!isBind) {
            viewModel.bindService(context)
        }
    }
    LaunchedEffect(Unit) {
        if (novelDetail !is UIState.UISuccess) {
            viewModel.loadNovel(novelId)
        }
    }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val showDownloadDialog = remember { mutableStateOf(false) }
    var hasPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasPermission = granted
            if (granted) {
                if (isBind) {
                    if (downloadState is DownloadServiceState.Downloading) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.already_downloading),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        showDownloadDialog.value = true
                    }
                } else {
                    viewModel.bindService(context)
                }
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.notification_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )


    val tabs = listOf(R.string.chapters)

    Scaffold { padding ->
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
                            if (readChapter.isEmpty() && novel.chapters.isNotEmpty()) {
                                navController.navigate(
                                    NavScreen.ReadNovelNavScreen(
                                        novelId = novelId,
                                        chapterId = novel.chapters[0]._id
                                    )
                                )
                            } else if (readChapter.isNotEmpty()) {
                                navController.navigate(
                                    NavScreen.ReadNovelNavScreen(
                                        novelId = novelId,
                                        chapterId = readChapter
                                    )
                                )
                            }
                        },
                        onDownloadClick = {
                            if (!hasPermission) {
                                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                return@StoryCard
                            }
                            if (isBind) {
                                if (downloadState is DownloadServiceState.Downloading) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.already_downloading),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@StoryCard
                                }
                                showDownloadDialog.value = true
                            } else {
                                viewModel.bindService(context)
                            }
                        }
                    )

                    // Enhanced TabRow

                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color.Transparent,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Text(
                                        text = stringResource(title).uppercase(),
                                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 14.sp
                                    )
                                }
                            )
                        }
                    }


                    when (selectedTabIndex) {
                        0 -> {
                            val sort = remember {
                                mutableStateOf(false)
                            }

                            // Enhanced header section

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.chapters).replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.getDefault()
                                            ) else it.toString()
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Text(
                                        text = " (${novel.chapters.size})",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        sort.value = !sort.value
                                    },
                                    modifier = Modifier
                                        .clip(CircleShape),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.1f
                                        )
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (sort.value) Icons.AutoMirrored.Filled.FormatAlignRight else Icons.AutoMirrored.Filled.FormatAlignLeft,
                                        contentDescription = "Sort",
                                        tint = MaterialTheme.colorScheme.primary
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

                                    // Enhanced chapter item
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 4.dp)
                                            .clickable {
                                                navController.navigate(
                                                    NavScreen.ReadNovelNavScreen(
                                                        novelId = novelId,
                                                        chapterId = novel.chapters[index]._id
                                                    )
                                                )
                                            },
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 2.dp,
                                            pressedElevation = 4.dp
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Chapter number badge
                                            Surface(
                                                modifier = Modifier
                                                    .clip(CircleShape),
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                                contentColor = MaterialTheme.colorScheme.primary
                                            ) {
                                                Text(
                                                    text = novel.chapters[index].chapterNumber.toString(),
                                                    modifier = Modifier.padding(
                                                        horizontal = 12.dp,
                                                        vertical = 6.dp
                                                    ),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = "${
                                                        stringResource(R.string.chapters).replaceFirstChar {
                                                            if (it.isLowerCase()) it.titlecase(
                                                                Locale.getDefault()
                                                            ) else it.toString()
                                                        }
                                                    } ${novel.chapters[index].chapterNumber}: ${novel.chapters[index].chapterTitle}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )

                                                Spacer(modifier = Modifier.padding(2.dp))

                                                Text(
                                                    text = DateDisplayHelper.formatDateString(
                                                        dateString = novel.chapters[index].createdAt
                                                    ),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.6f
                                                    )
                                                )
                                            }
                                            Spacer(modifier = Modifier.weight(1f))
                                            if (localNovel.firstOrNull { it == novel.chapters[index]._id } != null) Icon(
                                                imageVector = Icons.Default.DownloadForOffline,
                                                contentDescription = "Download",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (showDownloadDialog.value) {
                    DownloadDialog(
                        onDismiss = {
                            showDownloadDialog.value = false
                        },
                        onDownload = {
                            val intent =
                                Intent(context, DownloadNovelService::class.java).apply {
                                    action = DownloadNovelService.ACTION_DOWNLOAD
                                    putExtra(
                                        "novelInfo", NovelInfo(
                                            name = novel.fictionTitle,
                                            url = novel.image,
                                            chapterList = it.map { chapter ->
                                                ChapterInfo(
                                                    number = chapter.chapterNumber,
                                                    _id = chapter._id
                                                )
                                            },
                                            _id = novel._id
                                        )
                                    )
                                }
                            context.startService(intent)
                            showDownloadDialog.value = false
                        },
                        chapterList = novel.chapters
                    )

                }
            }

            is UIState.UIError -> {
                // Xử lý lỗi
            }
        }

    }
}