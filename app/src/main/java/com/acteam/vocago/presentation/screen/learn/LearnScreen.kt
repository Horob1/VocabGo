package com.acteam.vocago.presentation.screen.learn

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LearnScreen(
    vocaListId: Int,
    viewModel: LearnViewModel,
    navController: NavController
) {
    val questions by viewModel.questions.collectAsState()
    val listId by viewModel.listId.collectAsState()
    LaunchedEffect(vocaListId) {
        if (listId == null) {
            viewModel.setId(vocaListId)
        }
    }
    if (questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.padding(32.dp))
        }
        return
    }

    QuizletQuizScreen(
        questions = questions,
        onBackClick = { navController.popBackStack() },
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizletQuizScreen(
    questions: List<Question>,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }

    val currentQuestion = questions[currentQuestionIndex]
    val progress = (currentQuestionIndex + 1).toFloat() / questions.size
    var showConfirmDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        if (!showConfirmDialog) {
            showConfirmDialog = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "${currentQuestionIndex + 1} / ${questions.size}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1D29)
                )
            },
            navigationIcon = {
                IconButton(onClick = { showConfirmDialog = true }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1A1D29)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFFFFFFF),
                titleContentColor = Color(0xFF1A1D29)
            )
        )

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = Color(0xFF6366F1),
            trackColor = Color(0xFFE2E8F0),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = currentQuestion.question,
                        modifier = Modifier
                            .padding(28.dp)
                            .fillMaxWidth(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp,
                        color = Color(0xFF1A1D29)
                    )
                }

                currentQuestion.options.forEachIndexed { index, option ->
                    val isSelected = selectedAnswer == index
                    val isCorrect = index == currentQuestion.correctAnswerIndex
                    val isShowingResult = showResult

                    val backgroundColor by animateColorAsState(
                        targetValue = when {
                            isShowingResult && isCorrect -> Color(0xFF10B981)
                            isShowingResult && isSelected && !isCorrect -> Color(0xFFEF4444)
                            isSelected && !isShowingResult -> Color(0xFF6366F1)
                            else -> Color(0xFFFFFFFF)
                        },
                        animationSpec = tween(300)
                    )

                    val textColor by animateColorAsState(
                        targetValue = when {
                            isShowingResult && (isCorrect || (isSelected && !isCorrect)) -> Color.White
                            isSelected && !isShowingResult -> Color.White
                            else -> Color(0xFF1F2937)
                        },
                        animationSpec = tween(300)
                    )

                    val borderColor = when {
                        isShowingResult && isCorrect -> Color(0xFF10B981)
                        isShowingResult && isSelected && !isCorrect -> Color(0xFFEF4444)
                        isSelected && !isShowingResult -> Color(0xFF6366F1)
                        else -> Color(0xFFE5E7EB)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(2.dp, borderColor, RoundedCornerShape(14.dp))
                            .clickable(enabled = !showResult) {
                                selectedAnswer = index
                            },
                        colors = CardDefaults.cardColors(containerColor = backgroundColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = option,
                            modifier = Modifier
                                .padding(18.dp)
                                .fillMaxWidth(),
                            fontSize = 17.sp,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (showResult) {
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            selectedAnswer = null
                            showResult = false
                        } else {
                        }
                    } else {
                        if (selectedAnswer != null) {
                            showResult = true
                            if (selectedAnswer == currentQuestion.correctAnswerIndex) {
                                score++
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6366F1),
                    disabledContainerColor = Color(0xFFD1D5DB)
                ),
                enabled = selectedAnswer != null,
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = when {
                        showResult && currentQuestionIndex < questions.size - 1 -> "Tiếp tục"
                        showResult && currentQuestionIndex == questions.size - 1 -> "Hoàn thành"
                        else -> "Kiểm tra"
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            confirmButton = {
                Button(onClick = {
                    showConfirmDialog = false
                    onBackClick()
                }) {
                    Text("Thoát")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmDialog = false }) {
                    Text("Hủy")
                }
            },
            title = { Text("Xác nhận") },
            text = { Text("Bạn có chắc muốn thoát khỏi bài kiểm tra không?") }
        )
    }
    if (showResult && currentQuestionIndex == questions.size - 1) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable { /* Prevent clicks behind overlay */ },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon kết quả
                    val resultIcon = if (score >= questions.size * 0.7) "🎉" else "💪"
                    Text(
                        text = resultIcon,
                        fontSize = 64.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Tiêu đề
                    Text(
                        text = if (score >= questions.size * 0.7) "Xuất sắc!" else "Cố gắng thêm!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1D29),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Điểm số
                    Text(
                        text = "Điểm của bạn",
                        fontSize = 16.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "$score/${questions.size}",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6366F1),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Phần trăm
                    val percentage = (score.toFloat() / questions.size * 100).toInt()
                    Text(
                        text = "$percentage%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Thanh tiến trình
                    LinearProgressIndicator(
                        progress = { score.toFloat() / questions.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .padding(bottom = 32.dp),
                        color = when {
                            percentage >= 90 -> Color(0xFF10B981)
                            percentage >= 70 -> Color(0xFF6366F1)
                            percentage >= 50 -> Color(0xFFF59E0B)
                            else -> Color(0xFFEF4444)
                        },
                        trackColor = Color(0xFFE2E8F0),
                    )

                    // Buttons
                    Button(
                        onClick = { onBackClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(bottom = 12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6366F1)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "Hoàn thành",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            // Reset quiz
                            currentQuestionIndex = 0
                            selectedAnswer = null
                            showResult = false
                            score = 0
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "Làm lại",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6366F1)
                        )
                    }
                }
            }
        }
    }

}