package com.acteam.vocago.presentation.screen.newsdetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.data.local.mapping.toNewsEntity
import com.acteam.vocago.data.model.NewsDetailDto
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.EmptySurface
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.LoginRequiredDialog
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.main.news.component.SmallNewsCard
import com.acteam.vocago.presentation.screen.newsdetail.component.ContentSection
import com.acteam.vocago.presentation.screen.newsdetail.component.NewsDetailTopBar
import com.acteam.vocago.presentation.screen.newsdetail.component.PracticeSection
import com.acteam.vocago.presentation.screen.newsdetail.component.WordDetail
import com.acteam.vocago.utils.DateDisplayHelper
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveValue
import com.acteam.vocago.utils.safeClickable

@Composable
fun NewsDetailScreen(
    viewModel: NewsDetailViewModel,
    rootNavController: NavController,
    newsId: String,
) {
    val isAuth by viewModel.isAuth.collectAsState()

    var isShowLoginDialog by remember { mutableStateOf(false) }

    val itemHeight = responsiveValue(
        mobile = 250,
        tabletPortrait = 450,
        tabletLandscape = 650
    )

    val lazyListState = rememberLazyListState()

    val totalScrollOffset by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex * itemHeight + lazyListState.firstVisibleItemScrollOffset
        }
    }

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (totalScrollOffset > 200) 1f else totalScrollOffset / 200f,
        label = "TopBarAlpha"
    )


    val uiState by viewModel.uiState.collectAsState()
    val isTranslate by viewModel.isShowTranslate.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState !is UIState.UISuccess) {
            viewModel.getNewsDetail(newsId)
        }
    }

    Scaffold { innerPadding ->

        when (uiState) {
            is UIState.UILoading -> {
                // Show loading
                LoadingSurface(
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is UIState.UISuccess -> {
                // Show news detail
                val newsData = (uiState as UIState.UISuccess<NewsDetailDto>).data
                NewsDetailTopBar(
                    backToHome = {
                        rootNavController.popBackStack()
                    },
                    translate = {
                        viewModel.toggleTranslate()
                    },
                    toggleBookmark = {
                        if (isAuth) {
                            viewModel.toggleBookmark(!(newsData.log?.isBookmarked ?: false))
                        } else {
                            isShowLoginDialog = true
                        }
                    },
                    speak = {},
                    stop = {},
                    isBookmark = newsData.log?.isBookmarked ?: false,
                    isTranslate = isTranslate,
                    isSpeaking = false,
                    backgroundAlpha = backgroundAlpha,
                )
                LazyColumn(
                    state = lazyListState,
                    contentPadding = innerPadding
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(itemHeight.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(newsData.coverImage)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(R.drawable.loading_news),
                                error = painterResource(R.drawable.loading_news),
                                contentDescription = newsData.title,
                                alignment = Alignment.TopCenter,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = newsData.category,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.surface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.vi_flag),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = newsData.title,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    item {
                        val color = when (newsData.level) {
                            "easy" -> Color.Green.copy(
                                alpha = 0.3f
                            )

                            "medium" -> Color.Yellow.copy(
                                alpha = 0.3f
                            )

                            "hard" -> Color.Red.copy(
                                alpha = 0.3f
                            )

                            else -> MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.3f
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    16.dp,
                                    0.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val height = responsiveDP(28, 32, 32)
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(
                                            height
                                        )
                                        .background(
                                            color,
                                            MaterialTheme.shapes.small
                                        )
                                        .padding(
                                            8.dp,
                                        ), contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = newsData.level.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                java.util.Locale.ROOT
                                            ) else it.toString()
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))

                                Box(
                                    modifier = Modifier
                                        .size(
                                            height
                                        )
                                        .background(
                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f),
                                            MaterialTheme.shapes.small
                                        )
                                        .clip(
                                            MaterialTheme.shapes.small
                                        )
                                        .safeClickable(
                                            key = "translate_button"
                                        ) {
                                            viewModel.toggleTranslate()
                                        }, contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Translate,
                                        contentDescription = "Trans",
                                        modifier = Modifier.size(16.dp),
                                        tint = if (isTranslate) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f, true))


                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = DateDisplayHelper.formatDateString(newsData.createdAt),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item {
                        ContentSection(
                            content = newsData.content,
                            modifier = Modifier.padding(16.dp),
                            wordList = newsData.words,
                            translations = newsData.translations,
                            rootNavController = rootNavController,
                            viewModel = viewModel,
                        )
                    }

                    item {
                        WordDetail(
                            modifier = Modifier.padding(16.dp),
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.3f)
                        )
                    }

                    item {
                        PracticeSection(
                            modifier = Modifier.padding(16.dp),
                            questions = newsData.questions,
                            viewModel = viewModel,
                        )
                    }

                    if (newsData.related.isNotEmpty()) {
                        item {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.3f)
                            )
                            Text(
                                text = stringResource(R.string.related_articles),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        items(newsData.related.size, key = { index -> index }) { news ->
                            val newsEntity = toNewsEntity(
                                newsData.related[news],
                                1
                            )
                            SmallNewsCard(
                                news = newsEntity,
                                onItemClick = {
                                    rootNavController.navigate(
                                        NavScreen.NewsDetailNavScreen(newsId = newsEntity._id)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            is UIState.UIError -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptySurface()
                }
            }
        }
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
}