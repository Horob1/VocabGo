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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

    val listeningParts = getListeningParts()
    val readingParts = getReadingParts()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.text_selection_part),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    rootNavController.popBackStack()
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
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
                    title = stringResource(R.string.listenning),
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
                    title = stringResource(R.string.reading),
                    subtitle = "${stringResource(R.string.reading)} (100 ${stringResource(R.string.text_question)} - 75 ${
                        stringResource(
                            R.string.text_minute
                        )
                    })",
                    icon = "📖",
                    totalQuestions = 100,
                    timeLimit = "75 ${
                        stringResource(
                            R.string.text_minute
                        )
                    }"
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
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            "${stringResource(R.string.text_start_practice)} (${selectedParts.size} ${
                                stringResource(
                                    R.string.text_part
                                )
                            })",
                            color = MaterialTheme.colorScheme.onPrimary
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                icon,
                fontSize = 32.sp,
                modifier = Modifier.padding(end = 20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    subtitle,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$totalQuestions ${stringResource(R.string.text_question)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    timeLimit,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
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
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Selection checkbox
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { onToggleSelect() },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                // Part number
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "P${part.partNumber}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Spacer(Modifier.width(12.dp))

                // Title and basic info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        part.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "${part.questionCount} ${stringResource(R.string.text_question)} • ${part.timePerQuestion} • ${part.difficulty}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    part.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    stringResource(R.string.text_format),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    part.format,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(16.dp))

                // Tips card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                stringResource(R.string.text_tip_and_trick),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                part.tips,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f),
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
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
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

@Composable
fun getListeningParts(): List<TOEICPart> {
    return listOf(
        TOEICPart(
            partNumber = 1,
            title = stringResource(R.string.part1_title),
            subtitle = stringResource(R.string.part1_subtitle),
            description = stringResource(R.string.part1_description),
            format = stringResource(R.string.part1_format),
            tips = stringResource(R.string.part1_tips),
            questionCount = 6,
            timePerQuestion = stringResource(R.string.time_30_seconds),
            difficulty = stringResource(R.string.difficulty_easy),
            type = "listening"
        ),
        TOEICPart(
            partNumber = 2,
            title = stringResource(R.string.part2_title),
            subtitle = stringResource(R.string.part2_subtitle),
            description = stringResource(R.string.part2_description),
            format = stringResource(R.string.part2_format),
            tips = stringResource(R.string.part2_tips),
            questionCount = 25,
            timePerQuestion = stringResource(R.string.time_25_seconds),
            difficulty = stringResource(R.string.difficulty_medium),
            type = "listening"
        ),
        TOEICPart(
            partNumber = 3,
            title = stringResource(R.string.part3_title),
            subtitle = stringResource(R.string.part3_subtitle),
            description = stringResource(R.string.part3_description),
            format = stringResource(R.string.part3_format),
            tips = stringResource(R.string.part3_tips),
            questionCount = 39,
            timePerQuestion = stringResource(R.string.time_1_minute),
            difficulty = stringResource(R.string.difficulty_medium),
            type = "listening"
        ),
        TOEICPart(
            partNumber = 4,
            title = stringResource(R.string.part4_title),
            subtitle = stringResource(R.string.part4_subtitle),
            description = stringResource(R.string.part4_description),
            format = stringResource(R.string.part4_format),
            tips = stringResource(R.string.part4_tips),
            questionCount = 30,
            timePerQuestion = stringResource(R.string.time_1_minute),
            difficulty = stringResource(R.string.difficulty_hard),
            type = "listening"
        )
    )
}

@Composable
fun getReadingParts(): List<TOEICPart> {
    return listOf(
        TOEICPart(
            partNumber = 5,
            title = stringResource(R.string.part5_title),
            subtitle = stringResource(R.string.part5_subtitle),
            description = stringResource(R.string.part5_description),
            format = stringResource(R.string.part5_format),
            tips = stringResource(R.string.part5_tips),
            questionCount = 30,
            timePerQuestion = stringResource(R.string.time_45_seconds),
            difficulty = stringResource(R.string.difficulty_medium),
            type = "reading"
        ),
        TOEICPart(
            partNumber = 6,
            title = stringResource(R.string.part6_title),
            subtitle = stringResource(R.string.part6_subtitle),
            description = stringResource(R.string.part6_description),
            format = stringResource(R.string.part6_format),
            tips = stringResource(R.string.part6_tips),
            questionCount = 16,
            timePerQuestion = stringResource(R.string.time_1_5_minutes),
            difficulty = stringResource(R.string.difficulty_hard),
            type = "reading"
        ),
        TOEICPart(
            partNumber = 7,
            title = stringResource(R.string.part7_title),
            subtitle = stringResource(R.string.part7_subtitle),
            description = stringResource(R.string.part7_description),
            format = stringResource(R.string.part7_format),
            tips = stringResource(R.string.part7_tips),
            questionCount = 54,
            timePerQuestion = stringResource(R.string.time_1_5_minutes),
            difficulty = stringResource(R.string.difficulty_hard),
            type = "reading"
        )
    )
}

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
