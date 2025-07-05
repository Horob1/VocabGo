package com.acteam.vocago.presentation.screen.main.toeictest.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.data.model.ToeicDetailDto
import com.acteam.vocago.presentation.navigation.NavScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TOEICPartSelectionScreen(
    id: String,
    rootNavController: NavController,
) {
    var selectedParts by remember { mutableStateOf(setOf<Int>()) }
    var expandedParts by remember { mutableStateOf(setOf<Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        TOEICColors.Primary.copy(alpha = 0.05f),
                        Color.White
                    )
                )
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.text_selection_part),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    rootNavController.popBackStack()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                SectionHeader(
                    title = "LISTENING",
                    subtitle = "${stringResource(R.string.listenning)} (100 ${stringResource(R.string.text_question)} - 45 ${
                        stringResource(
                            R.string.text_minute
                        )
                    })",
                    icon = "🎧",
                    totalQuestions = 100,
                    timeLimit = "45 ${stringResource(R.string.text_minute)}"
                )
            }

            items(listeningParts) { part ->
                CompactPartCard(
                    part = part,
                    isSelected = selectedParts.contains(part.partNumber),
                    isExpanded = expandedParts.contains(part.partNumber),
                    onToggleSelect = {
                        selectedParts = if (selectedParts.contains(part.partNumber)) {
                            selectedParts - part.partNumber
                        } else {
                            selectedParts + part.partNumber
                        }
                    },
                    onToggleExpand = {
                        expandedParts = if (expandedParts.contains(part.partNumber)) {
                            expandedParts - part.partNumber
                        } else {
                            expandedParts + part.partNumber
                        }
                    }
                )
            }

            item { Spacer(Modifier.height(8.dp)) }

            item {
                SectionHeader(
                    title = "READING",
                    subtitle = "${stringResource(R.string.reading)} (100 ${stringResource(R.string.text_question)} - 75 ${
                        stringResource(
                            R.string.text_minute
                        )
                    })",
                    icon = "📖",
                    totalQuestions = 100,
                    timeLimit = "75 phút"
                )
            }

            items(readingParts) { part ->
                CompactPartCard(
                    part = part,
                    isSelected = selectedParts.contains(part.partNumber),
                    isExpanded = expandedParts.contains(part.partNumber),
                    onToggleSelect = {
                        selectedParts = if (selectedParts.contains(part.partNumber)) {
                            selectedParts - part.partNumber
                        } else {
                            selectedParts + part.partNumber
                        }
                    },
                    onToggleExpand = {
                        expandedParts = if (expandedParts.contains(part.partNumber)) {
                            expandedParts - part.partNumber
                        } else {
                            expandedParts + part.partNumber
                        }
                    }
                )
            }

            item {
                if (selectedParts.isNotEmpty()) {
                    Button(
                        onClick = {
                            rootNavController.navigate(
                                NavScreen.ToeicPracticeNavScreen(
                                    id, selectedParts.joinToString(",")
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            "${stringResource(R.string.text_start_practice)} (${selectedParts.size} ${
                                stringResource(
                                    R.string.text_part
                                )
                            })"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    icon: String,
    totalQuestions: Int,
    timeLimit: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TOEICColors.Primary.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 32.sp, modifier = Modifier.padding(end = 20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TOEICColors.Primary
                )
                Text(subtitle, fontSize = 14.sp, color = TOEICColors.OnSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$totalQuestions ${stringResource(R.string.text_question)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TOEICColors.Primary
                )
                Text(timeLimit, fontSize = 12.sp, color = TOEICColors.OnSurfaceVariant)
            }
        }
    }
}

@Composable
fun CompactPartCard(
    part: TOEICPart,
    isSelected: Boolean,
    isExpanded: Boolean,
    onToggleSelect: () -> Unit,
    onToggleExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) TOEICColors.Primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = if (isSelected) TOEICColors.Primary else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = if (isSelected) TOEICColors.Primary else TOEICColors.OnSurfaceVariant.copy(
                                alpha = 0.5f
                            ),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { onToggleSelect() },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                // Part number
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(TOEICColors.Primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "P${part.partNumber}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TOEICColors.Primary
                    )
                }

                Spacer(Modifier.width(12.dp))

                // Title and basic info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        part.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TOEICColors.OnSurface
                    )
                    Text(
                        "${part.questionCount} ${stringResource(R.string.text_question)} • ${part.timePerQuestion} • ${part.difficulty}",
                        fontSize = 12.sp,
                        color = TOEICColors.OnSurfaceVariant
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Expand/collapse button
                IconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Thu gọn" else "Mở rộng",
                        tint = TOEICColors.OnSurfaceVariant
                    )
                }
            }

            // Expanded content
            if (isExpanded) {
                Spacer(Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    QuickInfoItem(
                        Icons.Default.Quiz,
                        stringResource(R.string.text_question),
                        "${part.questionCount} ${stringResource(R.string.text_question)}"
                    )
                    QuickInfoItem(
                        Icons.Default.AccessTime,
                        stringResource(R.string.text_time),
                        part.timePerQuestion
                    )
                    QuickInfoItem(
                        Icons.AutoMirrored.Filled.TrendingUp,
                        stringResource(R.string.text_level),
                        part.difficulty
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    stringResource(R.string.text_decribe),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TOEICColors.OnSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    part.description,
                    fontSize = 14.sp,
                    color = TOEICColors.OnSurfaceVariant,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    stringResource(R.string.text_format),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TOEICColors.OnSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    part.format,
                    fontSize = 14.sp,
                    color = TOEICColors.OnSurfaceVariant,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = TOEICColors.Success.copy(alpha = 0.1f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = TOEICColors.Success,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                stringResource(R.string.text_tip_and_trick),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TOEICColors.Success
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                part.tips,
                                fontSize = 12.sp,
                                color = TOEICColors.OnSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickInfoItem(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            tint = TOEICColors.Primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 11.sp, color = TOEICColors.OnSurfaceVariant)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TOEICColors.OnSurface)
    }
}

data class TOEICPart(
    val partNumber: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val format: String,
    val tips: String,
    val questionCount: Int,
    val timePerQuestion: String,
    val difficulty: String,
    val type: String
)

private val listeningParts = listOf(
    TOEICPart(
        partNumber = 1,
        title = "Photographs",
        subtitle = "Image Description",
        description = "You will hear four descriptions of a picture. Choose the sentence that best describes what you see. The question and choices are not printed and are read only once.",
        format = "6 questions, each with 4 options A, B, C, D. You will see a photo and hear 4 sentences, choose the most accurate description.",
        tips = "Focus on the details in the image such as people, objects, actions, and positions. Eliminate answers with similar-sounding words but different meanings.",
        questionCount = 6,
        timePerQuestion = "~30 seconds",
        difficulty = "Easy",
        type = "listening"
    ),
    TOEICPart(
        partNumber = 2,
        title = "Question-Response",
        subtitle = "Q&A",
        description = "You will hear a question followed by three responses. Choose the most appropriate response. The audio is played only once and is not printed.",
        format = "25 questions with 3 options A, B, C. Listen to the question, then the responses, and choose the best one.",
        tips = "Pay attention to the question word (What, Where, When, Why, How). Avoid answers with similar sounds but unrelated meaning.",
        questionCount = 25,
        timePerQuestion = "~25 seconds",
        difficulty = "Medium",
        type = "listening"
    ),
    TOEICPart(
        partNumber = 3,
        title = "Conversations",
        subtitle = "Dialogue",
        description = "You will hear several conversations between two or three people. Then answer the questions based on what you hear. Questions and answer choices are printed.",
        format = "13 conversations, each with 3 questions (39 total). Each question has 4 options A, B, C, D. Some may include visuals or tables.",
        tips = "Read the questions first to know what to listen for. Pay attention to context, speakers, time, place, and purpose.",
        questionCount = 39,
        timePerQuestion = "~1 minute",
        difficulty = "Medium",
        type = "listening"
    ),
    TOEICPart(
        partNumber = 4,
        title = "Talks",
        subtitle = "Presentations",
        description = "You will hear short talks given by a speaker. Then answer the questions based on the talk. Questions and answer choices are printed.",
        format = "10 talks, each with 3 questions (30 total). Talks may include announcements, ads, news, instructions, or presentations.",
        tips = "Read the questions beforehand. Focus on purpose, audience, time, location, and specific details mentioned.",
        questionCount = 30,
        timePerQuestion = "~1 minute",
        difficulty = "Hard",
        type = "listening"
    )
)
private val readingParts = listOf(
    TOEICPart(
        partNumber = 5,
        title = "Incomplete Sentences",
        subtitle = "Sentence Completion",
        description = "You will see a sentence with a blank. Below are four words or phrases labeled A, B, C, D. Choose the one that best completes the sentence.",
        format = "40 independent questions. Each sentence has a blank and 4 options. Tests grammar, vocabulary, and usage in context.",
        tips = "Read the entire sentence before choosing. Pay attention to grammar, word type, verb tense, and meaning.",
        questionCount = 30,
        timePerQuestion = "~45 seconds",
        difficulty = "Medium",
        type = "reading"
    ),
    TOEICPart(
        partNumber = 6,
        title = "Text Completion",
        subtitle = "Paragraph Completion",
        description = "You will see four short texts with some blanks. Each blank has four answer choices. Choose the best word or sentence to complete the text.",
        format = "4 texts, each with 4 blanks (16 total). You may fill in words, phrases, or complete sentences.",
        tips = "Read the whole passage to understand the context. Pay attention to sentence connections and logical flow.",
        questionCount = 16,
        timePerQuestion = "~1.5 minutes",
        difficulty = "Hard",
        type = "reading"
    ),
    TOEICPart(
        partNumber = 7,
        title = "Reading Comprehension",
        subtitle = "Reading",
        description = "You will read various texts such as emails, letters, announcements, articles, and ads. Then answer questions based on what you read.",
        format = "Single passages (29 questions): 10 texts with 2–4 questions each. Double passages (25 questions): 5 sets with 5 questions each.",
        tips = "Read the questions before reading the text. Find keywords in the question and locate them in the text. Focus on details and logical inference.",
        questionCount = 54,
        timePerQuestion = "~1.5 minutes",
        difficulty = "Hard",
        type = "reading"
    )
)

fun filterToeicDetailBySelectedParts(
    dto: ToeicDetailDto,
    selectedParts: List<Int>
): ToeicDetailDto {
    return dto.copy(
        part1 = if (1 in selectedParts) dto.part1 else emptyList(),
        part2 = if (2 in selectedParts) dto.part2 else emptyList(),
        part3 = if (3 in selectedParts) dto.part3 else emptyList(),
        part4 = if (4 in selectedParts) dto.part4 else emptyList(),
        part5 = if (5 in selectedParts) dto.part5 else emptyList(),
        part6 = if (6 in selectedParts) dto.part6 else emptyList(),
        part7 = if (7 in selectedParts) dto.part7 else emptyList()
    )
}
