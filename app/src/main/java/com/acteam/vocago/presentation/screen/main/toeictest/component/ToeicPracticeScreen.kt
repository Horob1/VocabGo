package com.acteam.vocago.presentation.screen.main.toeictest.component

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.acteam.vocago.R
import com.acteam.vocago.data.model.SubmitRequest
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.main.toeictest.ToeicViewModel
import com.acteam.vocago.presentation.screen.main.toeictest.data.TOEICTest
import com.acteam.vocago.utils.responsiveValue
import kotlinx.coroutines.delay
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ToeicPracticeScreen(
    id: String,
    selectedParts: String = "",
    viewModel: ToeicViewModel,
    rootNavController: NavController
) {
    val uiState by viewModel.toeicDetailState.collectAsState()
    val submitState by viewModel.submitState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getToeicDetail(id)
        viewModel.loadCurrentUserId()
    }
    val selectedPartsList = remember(selectedParts) {
        selectedParts.split(",").mapNotNull { it.toIntOrNull() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = TOEICColors.Surface
            ) {
                when (val state = uiState) {
                    is UIState.UILoading -> {
                        LoadingSurface(picSize = responsiveValue(180, 360, 360))
                    }

                    is UIState.UIError -> {
                        ErrorDisplay()
                    }

                    is UIState.UISuccess -> {
                        val filteredDto =
                            filterToeicDetailBySelectedParts(state.data, selectedPartsList)
                        ToeicPracticeContent(
                            testData = filteredDto.toTest(),
                            selectedParts = selectedPartsList,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }

        if (submitState is UIState.UILoading) {
            LoadingSurface(picSize = responsiveValue(180, 360, 360))
        }

        if (submitState is UIState.UISuccess) {
            val result = (submitState as UIState.UISuccess).data
            SubmitResultOverlay(
                result = result,
                onDismiss = { rootNavController.popBackStack() }
            )
        }
    }
}

@Composable
fun ErrorDisplay() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = TOEICColors.Error.copy(alpha = 0.1f)
            ),
            border = BorderStroke(1.dp, TOEICColors.Error.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = TOEICColors.Error,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Đã xảy ra lỗi",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TOEICColors.Error
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun ToeicPracticeContent(
    testData: TOEICTest,
    selectedParts: List<Int> = emptyList(),
    viewModel: ToeicViewModel,
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var userAnswers by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    var timeRemaining by remember { mutableIntStateOf(7200) }
    val startTime = remember { Instant.now().toString() }

    val allQuestions = buildUnifiedQuestionListPractice(testData, selectedParts)
    val totalQuestions = allQuestions.size
    val progress = (currentQuestionIndex + 1).toFloat() / totalQuestions
    var showFullImage by remember { mutableStateOf(false) }
    var currentImageIndex by remember { mutableIntStateOf(0) }
    val userId by viewModel.userId.collectAsState()
    val currentQuestion = allQuestions[currentQuestionIndex]
    val currentPart = currentQuestion.part
    val showAudio = currentPart < 5

    LaunchedEffect(currentQuestionIndex) {
        currentImageIndex = 0
    }
    LaunchedEffect(Unit) {
        while (timeRemaining > 0) {
            delay(1000)
            timeRemaining -= 1
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TOEICColors.Surface)
    ) {
        ToeicPracticeHeader(
            testTitle = testData.title,
            currentPart = currentPart,
            timeRemaining = timeRemaining,
            progress = progress,
            currentQuestion = currentQuestionIndex + 1,
            totalQuestions = totalQuestions
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            if (showAudio && !currentQuestion.audioUrl.isNullOrBlank()) {
                AudioQuestionControls(currentQuestion.audioUrl)
            }
            RenderPracticeQuestionScreen(
                questionList = allQuestions,
                currentQuestionIndex = currentQuestionIndex,
                userAnswer = userAnswers,
                onAnswerSelected = { ans ->
                    userAnswers = userAnswers.toMutableMap().apply {
                        this[allQuestions[currentQuestionIndex].index] = ans
                    }
                },
                currentImageIndex = currentImageIndex,
                onNextImage = {
                    if ((allQuestions[currentQuestionIndex].imageUrls?.size
                            ?: 0) > currentImageIndex + 1
                    )
                        currentImageIndex++
                },
                onPrevImage = {
                    if (currentImageIndex > 0) currentImageIndex--
                },
                onImageClick = { showFullImage = true },
                totalQuestions = totalQuestions,
                onJumpToQuestion = { i -> currentQuestionIndex = i }
            )
            1
        }

        if (showFullImage && currentQuestion.imageUrls?.isNotEmpty() == true) {
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            val imageHeight = screenHeight * 0.6f

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .height(imageHeight)
                            .fillMaxWidth()
                            .clickable { showFullImage = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(currentQuestion.imageUrls[currentImageIndex]),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp)
                        )
                    }

                    if (currentQuestion.imageUrls.size > 1) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (currentImageIndex > 0) currentImageIndex--
                                },
                                enabled = currentImageIndex > 0
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Previous",
                                    tint = Color.White
                                )
                            }

                            Text(
                                text = "${currentImageIndex + 1}/${currentQuestion.imageUrls.size}",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            IconButton(
                                onClick = {
                                    if (currentImageIndex < currentQuestion.imageUrls.size - 1) currentImageIndex++
                                },
                                enabled = currentImageIndex < currentQuestion.imageUrls.size - 1
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = { showFullImage = false }) {
                        Text(text = stringResource(R.string.text_btn_close))
                    }
                }
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { if (currentQuestionIndex > 0) currentQuestionIndex-- },
                    enabled = currentQuestionIndex > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TOEICColors.SurfaceVariant,
                        contentColor = TOEICColors.OnSurface
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    stringResource(R.string.text_previous)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "${currentQuestionIndex + 1} / $totalQuestions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TOEICColors.OnSurface
                )

                Spacer(modifier = Modifier.width(16.dp))
                if (currentQuestionIndex == totalQuestions - 1) {
                    Button(
                        onClick = {
                            val now = Instant.now()
                            val submitData = SubmitRequest(
                                testId = testData._id,
                                userId = userId ?: return@Button,
                                startTime = startTime,
                                endTime = now.toString(),
                                timeSpent = 7200 - timeRemaining,
                                userAnswers = buildSubmitUserAnswers(userAnswers, allQuestions)
                            )
                            viewModel.submitToeicTest(submitData)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TOEICColors.Success),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.submit))
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                } else {
                    Button(
                        onClick = { currentQuestionIndex++ },
                        colors = ButtonDefaults.buttonColors(containerColor = TOEICColors.Primary),
                        modifier = Modifier.weight(1f)
                    ) {
                        stringResource(R.string.text_next)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AudioQuestionControls(audioUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    var progress by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(true) }
    var isCompleted by remember { mutableStateOf(false) }

    // Load and play audio when audioUrl changes
    LaunchedEffect(audioUrl) {
        isCompleted = false
        isPlaying = true
        val mediaItem = MediaItem.fromUri(audioUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    // Update progress
    LaunchedEffect(exoPlayer) {
        while (true) {
            val duration = exoPlayer.duration
            val position = exoPlayer.currentPosition
            progress = if (duration > 0) position / duration.toFloat() else 0f

            if (duration > 0 && position >= duration && !isCompleted) {
                isCompleted = true
                isPlaying = false
            }
            delay(500)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    fun handlePlayPause() {
        if (isCompleted) {
            exoPlayer.seekTo(0)
            exoPlayer.playWhenReady = true
            isPlaying = true
            isCompleted = false
        } else {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
                isPlaying = false
            } else {
                exoPlayer.play()
                isPlaying = true
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { handlePlayPause() },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = when {
                    isCompleted -> Icons.Default.PlayArrow
                    isPlaying -> Icons.Default.Pause
                    else -> Icons.Default.PlayArrow
                },
                contentDescription = null,
                tint = TOEICColors.Primary,
                modifier = Modifier.size(20.dp)
            )
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = TOEICColors.Primary,
            trackColor = Color(0xFFE0E0E0),
        )
    }
}

@Composable
fun ToeicPracticeHeader(
    testTitle: String,
    currentPart: Int,
    timeRemaining: Int,
    progress: Float,
    currentQuestion: Int,
    totalQuestions: Int
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = testTitle,
                        color = TOEICColors.OnSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = TOEICColors.Primary.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Part $currentPart",
                                color = TOEICColors.Primary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${stringResource(R.string.text_question)} $currentQuestion/$totalQuestions",
                            color = TOEICColors.OnSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }

                // Timer Card
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (timeRemaining < 600) TOEICColors.Warning.copy(alpha = 0.1f)
                        else TOEICColors.Success.copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (timeRemaining < 600) TOEICColors.Warning.copy(alpha = 0.3f)
                        else TOEICColors.Success.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Time",
                            tint = if (timeRemaining < 600) TOEICColors.Warning else TOEICColors.Success,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${timeRemaining / 60}:${
                                (timeRemaining % 60).toString().padStart(2, '0')
                            }",
                            color = if (timeRemaining < 600) TOEICColors.Warning else TOEICColors.Success,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar
            Column {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = TOEICColors.Primary,
                    trackColor = TOEICColors.SurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(progress * 100).toInt()}% ${stringResource(R.string.text_complete)}",
                    fontSize = 12.sp,
                    color = TOEICColors.OnSurfaceVariant
                )
            }
        }
    }
}

fun buildUnifiedQuestionListPractice(
    test: TOEICTest,
    selectedParts: List<Int>
): List<FlatQuestion> {
    val list = mutableListOf<FlatQuestion>()

    if (1 in selectedParts) {
        list += test.part1.map {
            FlatQuestion(it.index, it.audioUrl, listOfNotNull(it.imageUrl), null, it.answers, 1)
        }
    }
    if (2 in selectedParts) {
        list += test.part2.map {
            FlatQuestion(it.index, it.audioUrl, null, null, it.answers, 2)
        }
    }
    if (3 in selectedParts) {
        list += test.part3.flatMap { block ->
            block.questions.map {
                FlatQuestion(
                    it.index,
                    block.audioUrl,
                    listOfNotNull(block.imageUrl),
                    it.question,
                    it.answers,
                    3
                )
            }
        }
    }
    if (4 in selectedParts) {
        list += test.part4.flatMap { block ->
            block.questions.map {
                FlatQuestion(it.index, block.audioUrl, null, it.question, it.answers, 4)
            }
        }
    }
    if (5 in selectedParts) {
        list += test.part5.map {
            FlatQuestion(it.index, null, null, it.question, it.answers, 5)
        }
    }
    if (6 in selectedParts) {
        list += test.part6.flatMap { block ->
            block.questions.map {
                FlatQuestion(it.index, null, listOfNotNull(block.imageUrl), null, it.answers, 6)
            }
        }
    }
    if (7 in selectedParts) {
        list += test.part7.flatMap { block ->
            block.questions.map {
                FlatQuestion(it.index, null, block.imageUrl, it.question, it.answers, 7)
            }
        }
    }

    return list
}
