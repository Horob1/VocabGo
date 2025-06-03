package com.acteam.vocago.presentation.screen.newsdetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Translate
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.data.model.NewsDetailDto
import com.acteam.vocago.presentation.screen.common.EmptySurface
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.utils.DateDisplayHelper
import com.acteam.vocago.utils.responsiveDP

@Composable
fun NewsDetailScreen(
    viewModel: NewsDetailViewModel,
    newsId: String,
) {
    LaunchedEffect(Unit) {
        viewModel.getNewsDetail(newsId)
    }

    val itemHeight = 250 // dp

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
                val paragraphs = remember(newsData.content) {
                    derivedStateOf {
                        newsData.content.split("\n")
                    }
                }
                NewsDetailTopBar(
                    backToHome = {},
                    translate = {},
                    toggleBookmark = {},
                    speak = {},
                    stop = {},
                    isBookmark = false,
                    isSpeaking = false,
                    backgroundAlpha = backgroundAlpha,
                )
                LazyColumn(
                    state = lazyListState,
                    contentPadding = innerPadding
                ) {
                    item {
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
                            modifier = Modifier
                                .height(250.dp)
                                .fillMaxWidth()
                        )
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(
                                            responsiveDP(
                                                32,
                                                36, 40
                                            )
                                        )
                                        .background(
                                            color,
                                            MaterialTheme.shapes.medium
                                        )
                                        .padding(
                                            8.dp,
                                            2.dp
                                        ), contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = newsData.level.uppercase(java.util.Locale.ROOT),
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))

                                TopBarIconButton(
                                    onClick = {},
                                    contentDescription = "Translate",
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Translate,
                                        contentDescription = "Trans",
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f, true))
                            Text(
                                text = DateDisplayHelper.formatDateString(
                                    newsData.createdAt
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }




                    item {
                        // Content
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

    }
}

@Composable
fun WordClickableModernText() {
    val context = LocalContext.current

    val paragraph = "This is a clickable word demo with Compose."
    val words = paragraph.split(" ")

    val annotatedText = buildAnnotatedString {
        words.forEachIndexed { index, word ->
            val start = this.length
            append(word)
            val end = this.length

            addStyle(SpanStyle(color = Color.Blue), start, end)
            addStringAnnotation(
                tag = "WORD",
                annotation = word,
                start = start,
                end = end
            )
            if (index != words.lastIndex) append(" ")
        }
    }

    var selectedWord by remember { mutableStateOf<String?>(null) }
    var wordOffset by remember { mutableStateOf(Offset.Zero) }
    var showPopup by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = annotatedText,
            style = TextStyle(fontSize = 18.sp, lineHeight = 24.sp),
            onTextLayout = { layoutResult ->
                textLayoutResult = layoutResult
            },
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    textLayoutResult?.let { layout ->
                        val offset = layout.getOffsetForPosition(tapOffset)

                        annotatedText.getStringAnnotations(
                            tag = "WORD",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let { annotation ->
                            selectedWord = annotation.item
                            val boxPosition = layout.getBoundingBox(offset).bottomLeft
                            wordOffset = boxPosition
                            showPopup = true
                        }
                    }
                }
            }
        )

        if (showPopup && selectedWord != null) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(wordOffset.x.toInt(), wordOffset.y.toInt()) }
                    .background(Color.Yellow)
                    .padding(8.dp)
                    .border(1.dp, Color.Black)
            ) {
                Text("Thông tin: $selectedWord", fontSize = 14.sp)
            }
        }
    }
}

@Preview
@Composable
fun NewsDetailScreenPreview() {
    WordClickableModernText()
}