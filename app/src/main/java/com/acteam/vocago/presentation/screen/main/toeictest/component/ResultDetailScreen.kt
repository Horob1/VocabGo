package com.acteam.vocago.presentation.screen.main.toeictest.component

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.data.model.PartResult
import com.acteam.vocago.data.model.QuestionResult
import com.acteam.vocago.data.model.TestResultListDto
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.main.toeictest.ToeicViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultDetailScreen(
    id: String,
    viewModel: ToeicViewModel,
    rootNavController: NavController,
) {
    LaunchedEffect(id) {
        viewModel.getToeicResultDetail(id)
    }

    val resultDetailState by viewModel.resultDetailState.collectAsState()

    var selectedPartIndex by remember { mutableStateOf<Int?>(null) }
    var expandedExplanations by remember { mutableStateOf<Set<String>>(emptySet()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.text_result_exam)) },
                navigationIcon = {
                    IconButton(onClick = { rootNavController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (resultDetailState) {
            is UIState.UILoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UIState.UIError -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Lỗi khi tải dữ liệu", color = Color.Red)
                }
            }

            is UIState.UISuccess -> {
                val data = (resultDetailState as UIState.UISuccess<List<TestResultListDto>>).data
                val testResult = data.firstOrNull()

                if (testResult == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Không tìm thấy kết quả phù hợp", color = Color.Gray)
                    }
                    return@Scaffold
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFE3F2FD),
                                    Color(0xFFEDE7F6)
                                )
                            )
                        )
                        .padding(16.dp)
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        TestResultHeader(testResult)
                    }

                    item {
                        ScoreOverviewCard(testResult)
                    }

                    item {
                        Text(
                            text = stringResource(R.string.text_part_detail),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    itemsIndexed(testResult.parts) { index, part ->
                        PartResultCard(
                            part = part,
                            isExpanded = selectedPartIndex == index,
                            onExpandClick = {
                                selectedPartIndex = if (selectedPartIndex == index) null else index
                            },
                            expandedExplanations = expandedExplanations,
                            onExplanationToggle = { questionId ->
                                expandedExplanations =
                                    if (expandedExplanations.contains(questionId)) {
                                        expandedExplanations - questionId
                                    } else {
                                        expandedExplanations + questionId
                                    }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TestResultHeader(
    testResult: TestResultListDto,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Dòng chứa nút Back
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.text_result_exam),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "${stringResource(R.string.text_complete)}: ${formatDate(testResult.submittedAt)}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ScoreOverviewCard(testResult: TestResultListDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.text_overall_score),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreItem(
                    title = stringResource(R.string.text_total_score),
                    score = testResult.totalScore.toString(),
                    color = Brush.linearGradient(
                        colors = listOf(Color(0xFF9C27B0), Color(0xFFE91E63))
                    ),
                    icon = Icons.Default.Star
                )

                ScoreItem(
                    title = stringResource(R.string.listenning),
                    score = testResult.listeningScore.toString(),
                    color = Brush.linearGradient(
                        colors = listOf(Color(0xFF2196F3), Color(0xFF00BCD4))
                    ),
                    icon = Icons.Default.Headset
                )

                ScoreItem(
                    title = stringResource(R.string.reading),
                    score = testResult.readingScore.toString(),
                    color = Brush.linearGradient(
                        colors = listOf(Color(0xFF4CAF50), Color(0xFF009688))
                    ),
                    icon = Icons.Default.MenuBook
                )

                ScoreItem(
                    title = stringResource(R.string.text_time),
                    score = formatTime(testResult.completionTime),
                    color = Brush.linearGradient(
                        colors = listOf(Color(0xFFFF9800), Color(0xFFF44336))
                    ),
                    icon = Icons.Default.AccessTime
                )
            }
        }
    }
}

@Composable
fun ScoreItem(
    title: String,
    score: String,
    color: Brush,
    icon: ImageVector
) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = score,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = title,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PartResultCard(
    part: PartResult,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    expandedExplanations: Set<String>,
    onExplanationToggle: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header của phần
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandClick() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (part.partNumber <= 4) Color(0xFF2196F3) else Color(
                                    0xFF4CAF50
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (part.partNumber <= 4) Icons.Default.Headset else Icons.Default.MenuBook,
                            contentDescription = "${stringResource(R.string.text_part)} ${part.partNumber}",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Part ${part.partNumber}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${part.correctAnswers}/${part.totalQuestions} ${stringResource(R.string.text_correct)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${(part.correctAnswers * 100 / part.totalQuestions)}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = getAccuracyColor(part.correctAnswers, part.totalQuestions)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Thu gọn" else "Mở rộng"
                    )
                }
            }

            // Thanh tiến trình
            LinearProgressIndicator(
                progress = { part.correctAnswers.toFloat() / part.totalQuestions.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = getAccuracyColor(part.correctAnswers, part.totalQuestions),
                trackColor = Color.Gray.copy(alpha = 0.3f),
            )

            // Chi tiết câu hỏi
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))

                part.questions.forEach { question ->
                    QuestionResultItem(
                        question = question,
                        isExplanationExpanded = expandedExplanations.contains(question._id),
                        onExplanationToggle = { onExplanationToggle(question._id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun QuestionResultItem(
    question: QuestionResult,
    isExplanationExpanded: Boolean,
    onExplanationToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (question.isCorrect) Color(0xFFE8F5E8) else Color(0xFFFFF0F0)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (question.isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = if (question.isCorrect) "Đúng" else "Sai",
                        tint = if (question.isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Câu ${question.questionIndex}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!question.isCorrect) {
                        Text(
                            text = question.userAnswer,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFF44336)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${stringResource(R.string.text_answer)}: ${question.correctAnswer}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4CAF50)
                        )
                    } else {
                        Text(
                            text = "${stringResource(R.string.text_answer)}: ${question.correctAnswer}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4CAF50)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onExplanationToggle,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isExplanationExpanded) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isExplanationExpanded) "Ẩn giải thích" else "Xem giải thích",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            if (isExplanationExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp),
                        color = Color.Gray.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

// Helper functions
fun formatDate(dateString: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = sdf.parse(dateString)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}

fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    return "${hours}h ${minutes}m"
}

fun getAccuracyColor(correct: Int, total: Int): Color {
    val percentage = (correct * 100) / total
    return when {
        percentage >= 80 -> Color(0xFF4CAF50)
        percentage >= 60 -> Color(0xFF2196F3)
        percentage >= 40 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }
}