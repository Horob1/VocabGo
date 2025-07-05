package com.acteam.vocago.presentation.screen.main.toeictest.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part1Question
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part2Question
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part3Block
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part4Block
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part5Question
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part6Block
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part7Block

@Composable
fun Part1Screen(
    questions: List<Part1Question>,
    currentIndex: Int,
    userAnswers: Map<Int, String>,
    onAnswerSelected: (Int, String) -> Unit
) {
    if (currentIndex < questions.size) {
        val question = questions[currentIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Audio Controls
            AudioControls(audioUrl = question.audioUrl)

            Spacer(modifier = Modifier.height(16.dp))

            // Image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = question.imageUrl,
                    contentDescription = "Question Image",
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.songcau)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question Number
            Text(
                text = "Question ${question.index}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Answer Options
            question.answers.forEachIndexed { index, answer ->
                AnswerOption(
                    optionLetter = ('A' + index).toString(),
                    answerText = answer,
                    isSelected = userAnswers[question.index] == answer,
                    onSelected = { onAnswerSelected(question.index, answer) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Part 2: Question-Response
@Composable
fun Part2Screen(
    questions: List<Part2Question>,
    currentIndex: Int,
    userAnswers: Map<Int, String>,
    onAnswerSelected: (Int, String) -> Unit
) {
    if (currentIndex < questions.size) {
        val question = questions[currentIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Audio Controls
            AudioControls(audioUrl = question.audioUrl)

            Spacer(modifier = Modifier.height(24.dp))

            // Question Number
            Text(
                text = "Question ${question.index}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
            ) {
                Text(
                    text = "Listen to the question and choose the best response.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Answer Options
            question.answers.forEachIndexed { index, answer ->
                AnswerOption(
                    optionLetter = ('A' + index).toString(),
                    answerText = answer,
                    isSelected = userAnswers[question.index] == answer,
                    onSelected = { onAnswerSelected(question.index, answer) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// Part 3: Conversations
@Composable
fun Part3Screen(
    blocks: List<Part3Block>,
    currentIndex: Int,
    userAnswers: Map<Int, String>,
    onAnswerSelected: (Int, String) -> Unit
) {
    if (currentIndex < blocks.size) {
        val block = blocks[currentIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Audio Controls
            AudioControls(audioUrl = block.audioUrl)

            Spacer(modifier = Modifier.height(16.dp))

            // Image if available
            block.imageUrl?.let { imageUrl ->
                if (imageUrl.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Context Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Questions
            block.questions.forEach { question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "${question.index}. ${question.question}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        question.answers.forEachIndexed { index, answer ->
                            AnswerOption(
                                optionLetter = ('A' + index).toString(),
                                answerText = answer,
                                isSelected = userAnswers[question.index] == answer,
                                onSelected = { onAnswerSelected(question.index, answer) }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}

// Part 4: Talks
@Composable
fun Part4Screen(
    blocks: List<Part4Block>,
    currentIndex: Int,
    userAnswers: Map<Int, String>,
    onAnswerSelected: (Int, String) -> Unit
) {
    if (currentIndex < blocks.size) {
        val block = blocks[currentIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Audio Controls
            AudioControls(audioUrl = block.audioUrl)

            Spacer(modifier = Modifier.height(16.dp))

            // Questions
            block.questions.forEach { question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "${question.index}. ${question.question}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        question.answers.forEachIndexed { index, answer ->
                            AnswerOption(
                                optionLetter = ('A' + index).toString(),
                                answerText = answer,
                                isSelected = userAnswers[question.index] == answer,
                                onSelected = { onAnswerSelected(question.index, answer) }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}

// Part 5: Incomplete Sentences
@Composable
fun Part5Screen(
    questions: List<Part5Question>,
    currentIndex: Int,
    userAnswers: Map<Int, String>,
    onAnswerSelected: (Int, String) -> Unit
) {
    if (currentIndex < questions.size) {
        val question = questions[currentIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Question Number
            Text(
                text = "Question ${question.index}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Question Text
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = question.question,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Answer Options
            question.answers.forEachIndexed { index, answer ->
                AnswerOption(
                    optionLetter = ('A' + index).toString(),
                    answerText = answer,
                    isSelected = userAnswers[question.index] == answer,
                    onSelected = { onAnswerSelected(question.index, answer) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Part 6: Text Completion
@Composable
fun Part6Screen(
    blocks: List<Part6Block>,
    currentIndex: Int,
    userAnswers: Map<Int, String>,
    onAnswerSelected: (Int, String) -> Unit
) {
    if (currentIndex < blocks.size) {
        val block = blocks[currentIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Text Image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = block.imageUrl,
                    contentDescription = "Text Document",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Questions
            block.questions.forEach { question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Question ${question.index}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        question.answers.forEachIndexed { index, answer ->
                            AnswerOption(
                                optionLetter = ('A' + index).toString(),
                                answerText = answer,
                                isSelected = userAnswers[question.index] == answer,
                                onSelected = { onAnswerSelected(question.index, answer) }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}

// Part 7: Reading Comprehension
@Composable
fun Part7Screen(
    blocks: List<Part7Block>,
    currentIndex: Int,
    userAnswers: Map<Int, String>,
    onAnswerSelected: (Int, String) -> Unit
) {
    if (currentIndex < blocks.size) {
        val block = blocks[currentIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Text Images
            block.imageUrl.forEach { imageUrl ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(vertical = 4.dp)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Reading Passage",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Questions
            block.questions.forEach { question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "${question.index}. ${question.question}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        question.answers.forEachIndexed { index, answer ->
                            AnswerOption(
                                optionLetter = ('A' + index).toString(),
                                answerText = answer,
                                isSelected = userAnswers[question.index] == answer,
                                onSelected = { onAnswerSelected(question.index, answer) }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}
