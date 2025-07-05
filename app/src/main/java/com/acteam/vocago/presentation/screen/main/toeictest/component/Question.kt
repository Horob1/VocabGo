package com.acteam.vocago.presentation.screen.main.toeictest.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.main.toeictest.data.TOEICTest
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderSingleQuestionScreen(
    index: Int,
    audioUrl: String? = null,
    imageUrls: List<String>? = null,
    question: String? = null,
    answers: List<String>,
    userAnswer: Map<Int, String>,
    onAnswerSelected: (String) -> Unit,
    currentImageIndex: Int,
    onNextImage: () -> Unit,
    onPrevImage: () -> Unit,
    onImageClick: () -> Unit,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    onJumpToQuestion: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp)
    ) {
        val imageList = imageUrls ?: emptyList()

        val deviceType = getDeviceType()

        val imageHeight = when (deviceType) {
            DeviceType.Mobile -> 200.dp
            DeviceType.TabletPortrait -> 400.dp
            DeviceType.TabletLandscape -> 200.dp
        }


        if (imageList.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clickable { onImageClick() },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageList[currentImageIndex]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (imageList.size > 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onPrevImage) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                    Text("${currentImageIndex + 1}/${imageList.size}")
                    IconButton(onClick = onNextImage) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Câu hỏi
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = TOEICColors.Accent.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${stringResource(R.string.text_question)} $index",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TOEICColors.Accent,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        if (userAnswer.containsKey(index)) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = TOEICColors.Success.copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = TOEICColors.Success,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stringResource(R.string.text_answer),
                                        fontSize = 12.sp,
                                        color = TOEICColors.Success,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    IconButton(onClick = { showSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "next question",
                            tint = TOEICColors.Primary
                        )
                    }
                }

                question?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = TOEICColors.OnSurface,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            answers.forEachIndexed { i, answer ->
                val isSelected = userAnswer[index] == answer
                AnswerOption(
                    optionLetter = ('A' + i).toString(),
                    answerText = answer,
                    isSelected = isSelected,
                    onSelected = { onAnswerSelected(answer) }
                )
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            tonalElevation = 8.dp
        ) {
            val questionsPerPage = 20
            val totalPages = (totalQuestions + questionsPerPage - 1) / questionsPerPage
            val pagerState = rememberPagerState(pageCount = { totalPages })

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Page indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(totalPages) { pageIndex ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .padding(horizontal = 2.dp)
                                .background(
                                    color = if (pageIndex == pagerState.currentPage)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val startIndex = page * questionsPerPage
                    val endIndex = minOf(startIndex + questionsPerPage, totalQuestions)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(endIndex - startIndex) { i ->
                            val questionIndex = startIndex + i

                            Card(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clickable {
                                        onJumpToQuestion(questionIndex)
                                        showSheet = false
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (userAnswer.containsKey(questionIndex + 1))
                                        TOEICColors.Success
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${questionIndex + 1}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium,
                                        color = if (userAnswer.containsKey(questionIndex + 1))
                                            Color.White
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // Page info
                Text(
                    text = "${stringResource(R.string.text_page)} ${pagerState.currentPage + 1}/${totalPages}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderPracticeQuestionScreen(
    questionList: List<FlatQuestion>,
    currentQuestionIndex: Int,
    userAnswer: Map<Int, String>,
    onAnswerSelected: (String) -> Unit,
    currentImageIndex: Int,
    onNextImage: () -> Unit,
    onPrevImage: () -> Unit,
    onImageClick: () -> Unit,
    totalQuestions: Int,
    onJumpToQuestion: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    val currentQuestion = questionList[currentQuestionIndex]
    val index = currentQuestion.index
    val imageList = currentQuestion.imageUrls ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp)
    ) {
        val deviceType = getDeviceType()

        val imageHeight = when (deviceType) {
            DeviceType.Mobile -> 200.dp
            DeviceType.TabletPortrait -> 400.dp
            DeviceType.TabletLandscape -> 200.dp
        }

        if (imageList.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clickable { onImageClick() },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageList[currentImageIndex]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (imageList.size > 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onPrevImage) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                    Text("${currentImageIndex + 1}/${imageList.size}")
                    IconButton(onClick = onNextImage) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        // Câu hỏi
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = TOEICColors.Accent.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${stringResource(R.string.text_question)} $index",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TOEICColors.Accent,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        if (userAnswer.containsKey(index)) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = TOEICColors.Success.copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = TOEICColors.Success,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stringResource(R.string.text_answer),
                                        fontSize = 12.sp,
                                        color = TOEICColors.Success,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    IconButton(onClick = { showSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "next question",
                            tint = TOEICColors.Primary
                        )
                    }
                }

                currentQuestion.question?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = TOEICColors.OnSurface,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            currentQuestion.answers.forEachIndexed { i, answer ->
                val isSelected = userAnswer[index] == answer
                AnswerOption(
                    optionLetter = ('A' + i).toString(),
                    answerText = answer,
                    isSelected = isSelected,
                    onSelected = { onAnswerSelected(answer) }
                )
            }
        }
    }
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            tonalElevation = 8.dp
        ) {
            val questionsPerPage = 20
            val totalPages = (totalQuestions + questionsPerPage - 1) / questionsPerPage
            val pagerState = rememberPagerState(pageCount = { totalPages })

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(totalPages) { pageIndex ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .padding(horizontal = 2.dp)
                                .background(
                                    color = if (pageIndex == pagerState.currentPage)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val startIndex = page * questionsPerPage
                    val endIndex = minOf(startIndex + questionsPerPage, totalQuestions)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(endIndex - startIndex) { i ->
                            val questionIdx = startIndex + i
                            val actualIndex = questionList[questionIdx].index

                            Card(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clickable {
                                        onJumpToQuestion(questionIdx)
                                        showSheet = false
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (userAnswer.containsKey(actualIndex))
                                        TOEICColors.Success
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = actualIndex.toString(),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium,
                                        color = if (userAnswer.containsKey(actualIndex))
                                            Color.White
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = "${stringResource(R.string.text_page)} ${pagerState.currentPage + 1}/$totalPages",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp)
                )
            }
        }
    }
}


fun buildUnifiedQuestionList(test: TOEICTest): List<FlatQuestion> {
    val list = mutableListOf<FlatQuestion>()
    list += test.part1.map {
        FlatQuestion(it.index, it.audioUrl, listOfNotNull(it.imageUrl), null, it.answers, 1)
    }
    list += test.part2.map {
        FlatQuestion(it.index, it.audioUrl, null, null, it.answers, 2)
    }
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
    list += test.part4.flatMap { block ->
        block.questions.map {
            FlatQuestion(it.index, block.audioUrl, null, it.question, it.answers, 4)
        }
    }
    list += test.part5.map {
        FlatQuestion(it.index, null, null, it.question, it.answers, 5)
    }
    list += test.part6.flatMap { block ->
        block.questions.map {
            FlatQuestion(it.index, null, listOfNotNull(block.imageUrl), null, it.answers, 6)
        }
    }
    list += test.part7.flatMap { block ->
        block.questions.map {
            FlatQuestion(it.index, null, block.imageUrl, it.question, it.answers, 7)
        }
    }
    return list
}


data class FlatQuestion(
    val index: Int,
    val audioUrl: String? = null,
    val imageUrls: List<String>? = null,
    val question: String? = null,
    val answers: List<String>,
    val part: Int
)
