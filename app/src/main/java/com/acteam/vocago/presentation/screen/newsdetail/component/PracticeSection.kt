package com.acteam.vocago.presentation.screen.newsdetail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.data.model.QuestionNewsDto
import com.acteam.vocago.presentation.screen.newsdetail.NewsDetailViewModel
import com.acteam.vocago.utils.safeClickable

@Composable
fun PracticeSection(
    modifier: Modifier = Modifier,
    questions: List<QuestionNewsDto>,
    viewModel: NewsDetailViewModel,
) {
    val practiceData by viewModel.practiceData.collectAsState()
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.title_practice),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(16.dp))
                AnimatedVisibility(
                    visible = practiceData.isSubmitted,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.text_score),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${practiceData.score}/${questions.size}",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = "Show hint",
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .safeClickable(key = "practice_section") {
                                    viewModel.toggleShowHint()
                                },
                            tint = if (practiceData.isShowHint) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )

                    }
                }
            }


            Text(
                text = "${practiceData.currentQuestionIndex + 1}/${questions.size}",
                style = MaterialTheme.typography.titleMedium
            )
        }


        Text(
            text = questions[practiceData.currentQuestionIndex].question,
            style = MaterialTheme.typography.bodyLarge,
        )

        AnimatedVisibility(
            visible = practiceData.isSubmitted && practiceData.isShowHint,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.shapes.small
                    )
                    .padding(8.dp)
            ) {

                Text(
                    text = questions[practiceData.currentQuestionIndex].explanation,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }


        if (practiceData.isShowHint && !practiceData.isSubmitted) {
            Text(
                text = questions[practiceData.currentQuestionIndex].explanation,
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = TextDecoration.Underline
            )
        }


        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            questions[practiceData.currentQuestionIndex].answers.forEachIndexed { index, option ->
                val color = when (practiceData.isSubmitted) {
                    true -> {
                        if (questions[practiceData.currentQuestionIndex].correctAnswerIndex == index)
                            Color(0xFF81C784) // Màu xanh lá cây
                        else if (practiceData.selectedOptions[practiceData.currentQuestionIndex] == index)
                            Color(0xFFEF9A9A) // Màu cam
                        else MaterialTheme.colorScheme.surface
                    }

                    false -> if (index == practiceData.selectedOptions[practiceData.currentQuestionIndex]) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            if (!practiceData.isSubmitted) {
                                viewModel.chooseAnswer(practiceData.currentQuestionIndex, index)
                            }
                        }
                        .background(
                            color = color,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = practiceData.selectedOptions[practiceData.currentQuestionIndex] == index,
                        onClick = {
                            if (!practiceData.isSubmitted) {
                                viewModel.chooseAnswer(practiceData.currentQuestionIndex, index)
                            }
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.White,
                            unselectedColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${index + 1}. $option",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (color == MaterialTheme.colorScheme.surface) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Button(
                    onClick = {
                        viewModel.prevQuestion()
                    },
                    modifier = Modifier.clip(
                        MaterialTheme.shapes.small
                    ),
                    enabled = practiceData.currentQuestionIndex >= 1
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Button(
                    onClick = {
                        viewModel.nextQuestion()
                    },
                    modifier = Modifier.clip(
                        MaterialTheme.shapes.small
                    ),
                    enabled = practiceData.currentQuestionIndex < questions.size - 1
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Forward"
                    )
                }
            }

            Button(
                onClick = {
                    if (!practiceData.isSubmitted) {
                        viewModel.submitPractice()
                    } else {
                        viewModel.retryPractice()
                    }
                },
                modifier = Modifier.clip(
                    MaterialTheme.shapes.small
                ),
            ) {
                if (practiceData.isSubmitted) {
                    Text(stringResource(R.string.text_retry))
                } else {
                    Text(stringResource(R.string.submit))
                }
            }
        }
    }
}