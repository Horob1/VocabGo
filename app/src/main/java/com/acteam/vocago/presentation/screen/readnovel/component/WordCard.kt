package com.acteam.vocago.presentation.screen.readnovel.component

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.utils.responsiveDP
import java.util.Locale

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    word: String,
    wordUiState: UIState<WordDto>,
    rootNavController: NavController,
    onHideCard: () -> Unit,
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    // Init TTS
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    // Dispose TTS when not needed
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabTitles = remember {
        listOf(
            Triple("Vi", R.drawable.vietnam, "Vietnamese"),
            Triple("En", R.drawable.unitedkingdom, "English"),
        )
    }

    val iconSize = 20.dp
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
    )

    Popup(
        onDismissRequest = onHideCard,
        alignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(gradientColors),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                // Tabs với design đẹp hơn
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                ) {
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.Transparent,
                        indicator = { },
                        divider = { }
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (selectedTabIndex == index)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else Color.Transparent
                                    ),
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = title.second),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                        )
                                        Text(
                                            text = title.first,
                                            fontWeight = if (selectedTabIndex == index)
                                                FontWeight.Bold else FontWeight.Medium,
                                            color = if (selectedTabIndex == index)
                                                MaterialTheme.colorScheme.onPrimaryContainer
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                // Word title + action buttons với design mới
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = word,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Surface(
                                onClick = {
                                    tts?.speak(
                                        word,
                                        TextToSpeech.QUEUE_FLUSH,
                                        null,
                                        null
                                    )
                                },
                                enabled = tts != null,
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ) {
                                IconButton(
                                    onClick = {
                                        tts?.speak(
                                            word,
                                            TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            null
                                        )
                                    },
                                    enabled = tts != null
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                        contentDescription = "speak",
                                        modifier = Modifier.size(iconSize),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                onClick = {
                                    if (wordUiState is UIState.UISuccess) {
                                        rootNavController.navigate(
                                            NavScreen.ChooseVocaListNavScreen(
                                                word = word,
                                                pronunciation = wordUiState.data.phonetics.joinToString(
                                                    " "
                                                ) { it.pronunciation },
                                                meaning = wordUiState.data.translations.find {
                                                    it.targetLanguage == tabTitles[selectedTabIndex].third
                                                }?.translation ?: "",
                                                type = wordUiState.data.types.find {
                                                    it.targetLanguage == tabTitles[selectedTabIndex].third
                                                }?.type?.joinToString(", ") ?: ""
                                            )
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                            ) {
                                IconButton(
                                    onClick = {
                                        if (wordUiState is UIState.UISuccess) {
                                            rootNavController.navigate(
                                                NavScreen.ChooseVocaListNavScreen(
                                                    word = word,
                                                    pronunciation = wordUiState.data.phonetics.joinToString(
                                                        " "
                                                    ) { it.pronunciation },
                                                    meaning = wordUiState.data.translations.find {
                                                        it.targetLanguage == tabTitles[selectedTabIndex].third
                                                    }?.translation ?: "",
                                                    type = wordUiState.data.types.find {
                                                        it.targetLanguage == tabTitles[selectedTabIndex].third
                                                    }?.type?.joinToString(", ") ?: ""
                                                )
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AddCircle,
                                        contentDescription = "add",
                                        modifier = Modifier.size(iconSize),
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }

                            Surface(
                                onClick = { clipboardManager.setText(AnnotatedString(word)) },
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                            ) {
                                IconButton(
                                    onClick = { clipboardManager.setText(AnnotatedString(word)) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentCopy,
                                        contentDescription = "copy",
                                        modifier = Modifier.size(iconSize),
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }
                        }
                    }
                }

                // Content box với design mới
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(
                                if (wordUiState !is UIState.UIError) responsiveDP(120, 200, 250)
                                else 0.dp
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(end = 8.dp)
                                .verticalScroll(scrollState),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            when (wordUiState) {
                                is UIState.UILoading -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.primary,
                                            strokeWidth = 3.dp
                                        )
                                    }
                                }

                                is UIState.UISuccess -> {
                                    val wordDetail = wordUiState.data

                                    if (wordDetail.word == word) {
                                        // Phonetics với design mới
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = wordDetail.phonetics.joinToString("  ") {
                                                    val flag =
                                                        if (it.countryCode == "US") "🇺🇸" else "🇬🇧"
                                                    "$flag ${it.pronunciation}"
                                                },
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.Medium
                                                ),
                                                modifier = Modifier.padding(12.dp),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        // Type với design mới
                                        wordDetail.types.find {
                                            it.targetLanguage == tabTitles[selectedTabIndex].third
                                        }?.type?.joinToString("  ")?.let { typeText ->
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = MaterialTheme.colorScheme.secondary.copy(
                                                    alpha = 0.1f
                                                )
                                            ) {
                                                Text(
                                                    text = "🏷️ $typeText",
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Medium
                                                    ),
                                                    modifier = Modifier.padding(12.dp),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }

                                        // Translation với design mới
                                        wordDetail.translations.find {
                                            it.targetLanguage == tabTitles[selectedTabIndex].third
                                        }?.translation?.let { translation ->
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = MaterialTheme.colorScheme.tertiary.copy(
                                                    alpha = 0.1f
                                                )
                                            ) {
                                                Text(
                                                    text = "💡 $translation",
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Medium
                                                    ),
                                                    modifier = Modifier.padding(12.dp),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }

                                        // Examples với design mới
                                        wordDetail.examples.forEach { example ->
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = MaterialTheme.colorScheme.surfaceVariant.copy(
                                                    alpha = 0.7f
                                                )
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(12.dp),
                                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Text(
                                                        text = "📝 ${example.definition}",
                                                        style = MaterialTheme.typography.bodyLarge.copy(
                                                            fontWeight = FontWeight.Medium
                                                        ),
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    example.translations.filter {
                                                        it.targetLanguage == tabTitles[selectedTabIndex].third
                                                    }.forEach { translation ->
                                                        Text(
                                                            text = "   → ${translation.translation}",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                else -> {}
                            }
                        }

                        // Custom scrollbar với design mới
                        val scrollRatio =
                            scrollState.value.toFloat() / (scrollState.maxValue.toFloat() + 0.01f)
                        val scrollbarHeight = 40.dp
                        val scrollbarOffset = with(LocalDensity.current) {
                            (scrollRatio * (100.dp.toPx() - scrollbarHeight.toPx())).toDp()
                        }

                        if (scrollState.maxValue > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(y = scrollbarOffset)
                                    .width(6.dp)
                                    .height(scrollbarHeight)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            listOf(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                                            )
                                        ),
                                        shape = RoundedCornerShape(3.dp)
                                    )
                            )
                        }
                    }
                }

                // Button với design mới
                if (wordUiState is UIState.UISuccess) {
                    Button(
                        onClick = {
                            rootNavController.navigate(NavScreen.WordDetailNavScreen(word))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.more_content),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}