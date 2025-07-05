package com.acteam.vocago.presentation.screen.flashcard

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.flipcard.FlippableCard
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCardScreen(
    vocaListId: Int,
    viewModel: FlashCardViewModel,
    navController: NavController,
) {
    val vocabListData by viewModel.vocaListDetailData.collectAsState()
    val listId by viewModel.listId.collectAsState()

    LaunchedEffect(Unit) {
        if (listId == null) {
            viewModel.setId(vocaListId)
        }
    }
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    // Khởi tạo TextToSpeech
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    // Đảm bảo dọn bộ nhớ khi không dùng nữa
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val scope = rememberCoroutineScope()
    val cardState = vocabListData?.let {
        rememberPagerState(initialPage = 0) {
            it.vocas.size
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (vocabListData != null)
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                stringResource(R.string.flashcard),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            cardState?.let { state ->
                                Text(
                                    "${state.currentPage + 1} / ${state.pageCount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
        },
        bottomBar = {
            if (vocabListData != null)
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically {
                        it
                    },
                    exit = slideOutVertically {
                        it
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clip(
                                    MaterialTheme.shapes.medium
                                )
                                .clickable {
                                    scope.launch {
                                        cardState?.currentPage?.let {
                                            if (it > 0) {
                                                cardState.animateScrollToPage(cardState.currentPage - 1)
                                            }
                                        }
                                    }
                                }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Previous Card",
                                tint = if (cardState?.currentPage == 0) {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clip(
                                    MaterialTheme.shapes.medium
                                )
                                .clickable {
                                    scope.launch {
                                        cardState?.currentPage?.let {
                                            if (it < cardState.pageCount - 1) {
                                                cardState.animateScrollToPage(cardState.currentPage + 1)
                                            }
                                        }
                                    }
                                }
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Next Card",
                                tint = if (cardState?.currentPage == (cardState?.pageCount
                                        ?: 0) - 1
                                ) {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
        }
    ) { paddingValues ->
        if (vocabListData == null) {
            LoadingSurface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }

        if (vocabListData != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                HorizontalPager(state = cardState!!) { index ->
                    val word = vocabListData!!.vocas[index]
                    FlippableCard(
                        frontSide = {
                            ElevatedCard(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize()
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        // Hiển thị hình ảnh nếu có
                                        word.urlImage?.let { imageUrl ->
                                            AsyncImage(
                                                model = imageUrl,
                                                contentDescription = "Image for ${word.word}",
                                                modifier = Modifier
                                                    .size(150.dp)
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.height(24.dp))
                                        }

                                        // Hiển thị từ
                                        Text(
                                            word.word,
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Chỉ dẫn để flip card
                                        Text(
                                            stringResource(R.string.tap_to_see_meaning),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        },
                        backSide = {
                            ElevatedCard(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize()
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        // Hiển thị hình ảnh nếu có
                                        word.urlImage?.let { imageUrl ->
                                            AsyncImage(
                                                model = imageUrl,
                                                contentDescription = "Image for ${word.word}",
                                                modifier = Modifier
                                                    .size(120.dp)
                                                    .clip(RoundedCornerShape(8.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                        }

                                        // Hiển thị từ
                                        Text(
                                            word.word,
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )

                                        // Hiển thị phát âm nếu có
                                        word.pronunciation?.let { pronunciation ->
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    pronunciation.split(",").joinToString("-"),
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.6f
                                                    ),
                                                    textAlign = TextAlign.Center
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                IconButton(
                                                    onClick = {
                                                        tts?.speak(
                                                            word.word,
                                                            TextToSpeech.QUEUE_FLUSH,
                                                            null,
                                                            null
                                                        )
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                                        contentDescription = "Play pronunciation",
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                        }

                                        // Hiển thị loại từ nếu có
                                        word.type?.let { type ->
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                "($type)",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Hiển thị nghĩa
                                        Text(
                                            word.meaning,
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.primary
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Chỉ dẫn để flip card
                                        Text(
                                            stringResource(R.string.tap_to_see_word),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}