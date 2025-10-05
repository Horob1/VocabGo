package com.acteam.vocago.presentation.screen.main.toeictest.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.acteam.vocago.data.model.TestResultListDto
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.main.toeictest.ToeicViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToeicResultsScreen(
    id: String,
    viewModel: ToeicViewModel,
    rootNavController: NavController,
) {
    LaunchedEffect(id) {
        viewModel.getToeicResult(id)
    }
    val resultState by viewModel.resultState.collectAsState()

    var selectedFilter by remember { mutableStateOf("all") }
    var sortOrder by remember { mutableStateOf("newest") }

    if (resultState is UIState.UILoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val testList = (resultState as? UIState.UISuccess)?.data ?: emptyList()

    val filteredResults = remember(testList, selectedFilter, sortOrder) {
        testList
            .filter {
                when (selectedFilter) {
                    "all" -> true
                    "fulltest" -> it.parts.size >= 7
                    "practice" -> it.parts.size < 7
                    else -> true
                }
            }
            .sortedByDescending {
                when (sortOrder) {
                    "newest" -> Instant.parse(it.submittedAt).toEpochMilli()
                    "score" -> it.totalScore.toLong()
                    else -> 0L
                }
            }
            .distinctBy { it._id }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
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
                    stringResource(R.string.text_complete),
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
            actions = {
                IconButton(onClick = { /* Hiện modal lọc nếu cần */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Lọc")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        StatisticsSummary(
            results = testList,
            modifier = Modifier.padding(16.dp)
        )

//        FilterSortBar(
//            selectedFilter = selectedFilter,
//            onFilterChanged = { selectedFilter = it },
//            sortOrder = sortOrder,
//            onSortChanged = { sortOrder = it },
//            modifier = Modifier.padding(horizontal = 16.dp)
//        )

        Spacer(Modifier.height(16.dp))

        if (filteredResults.isEmpty()) {
            EmptyResultsState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    filteredResults,
                    key = { it._id }
                ) { testDto ->
                    TestResultCard(
                        testDto = testDto,
                        onItemClick = {
                            rootNavController.navigate(NavScreen.ToeicResultDetailNavScreen(testDto._id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticsSummary(
    results: List<TestResultListDto>,
    modifier: Modifier = Modifier
) {
    val totalTests = results.size
    val avgScore = if (results.isNotEmpty())
        results.map { it.totalScore }.average().toInt()
    else 0
    val bestScore = results.maxOfOrNull { it.totalScore } ?: 0
    val totalStudyTime = results.sumOf { it.completionTime }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.text_general_static),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Quiz,
                    label = stringResource(R.string.text_total_score),
                    value = totalTests.toString(),
                    color = MaterialTheme.colorScheme.primary
                )

                StatItem(
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    label = stringResource(R.string.text_avg_score),
                    value = avgScore.toString(),
                    color = MaterialTheme.colorScheme.tertiary
                )

                StatItem(
                    icon = Icons.Default.Star,
                    label = stringResource(R.string.text_max_score),
                    value = bestScore.toString(),
                    color = MaterialTheme.colorScheme.secondary
                )

                StatItem(
                    icon = Icons.Default.AccessTime,
                    label = stringResource(R.string.text_minute),
                    value = "${totalStudyTime / 60}",
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun TestResultCard(
    testDto: TestResultListDto,
    onItemClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val date = Instant.parse(testDto.submittedAt).atZone(ZoneId.systemDefault())

    val correctAnswers = testDto.parts.sumOf { it.correctAnswers }
    val totalQuestions = testDto.parts.sumOf { it.totalQuestions }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onItemClick()
                }),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        stringResource(R.string.text_test),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        date.format(formatter),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        date.format(timeFormatter),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreDisplay(
                    stringResource(R.string.text_total_score),
                    testDto.totalScore,
                    990,
                    MaterialTheme.colorScheme.primary,
                    true
                )
                ScoreDisplay(
                    stringResource(R.string.listenning),
                    testDto.listeningScore,
                    495,
                    MaterialTheme.colorScheme.tertiary
                )
                ScoreDisplay(
                    stringResource(R.string.reading),
                    testDto.readingScore,
                    495,
                    MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${testDto.completionTime / 60}p ${testDto.completionTime % 60}s",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "$correctAnswers/$totalQuestions ${stringResource(R.string.text_correct)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ScoreDisplay(
    label: String,
    score: Int,
    maxScore: Int,
    color: Color,
    isMain: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = if (isMain) 14.sp else 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isMain) FontWeight.Medium else FontWeight.Normal
        )

        Text(
            text = score.toString(),
            fontSize = if (isMain) 24.sp else 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )

        if (!isMain) {
            Text(
                text = "/$maxScore",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyResultsState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📚",
            fontSize = 48.sp
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.text_no_result_yet),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.text_no_practice),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}