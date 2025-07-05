package com.acteam.vocago.presentation.screen.worddetail

import android.speech.tts.TextToSpeech
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailScreen(
    word: String,
    viewModel: WordDetailViewModel,
    rootNavController: NavController,
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    // Khởi tạo TextToSpeech
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    // Đảm bảo dọn bộ nhớ khi không dùng nữa
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }
    val wordUiState by viewModel.wordUiState.collectAsState()
    val scrollState = rememberScrollState()
    val tabTitles = remember {
        listOf(
            Pair("definition", R.string.tab_definition),
            Pair("idioms", R.string.tab_idioms),
            Pair("synonyms", R.string.tab_synonyms),
            Pair("antonyms", R.string.tab_antonyms),
        )
    }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val targetLanguages = remember {
        listOf(
            Triple("English", R.drawable.unitedkingdom, R.string.english),
            Triple("Vietnamese", R.drawable.vietnam, R.string.vietnamese),
        )
    }
    val chosenTargetLanguage by viewModel.chosenTargetLanguage.collectAsState()

    LaunchedEffect(Unit) {
        if (wordUiState !is UIState.UISuccess) {
            viewModel.getWordDetail(word)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = word,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { rootNavController.popBackStack() },
                        modifier = Modifier
                            .padding(4.dp)
                            .shadow(2.dp, RoundedCornerShape(12.dp))
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { it ->
        when (wordUiState) {
            is UIState.UILoading -> {
                // Show loading
                LoadingSurface(
                    modifier = Modifier.padding(it)
                )
            }

            is UIState.UISuccess -> {
                // Show word detail
                val wordDetail = (wordUiState as UIState.UISuccess<WordDto>).data
                val tgLang = if (chosenTargetLanguage == 1) "English" else "Vietnamese"
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    // Header Card with gradient background
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                        )
                                    )
                                )
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Spacer(modifier = Modifier.height(48.dp))
                                // Word title with enhanced styling
                                Text(
                                    text = word,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 42.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Word type and translation in cards
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    wordDetail.types.find { value ->
                                        value.targetLanguage == tgLang
                                    }?.type?.joinToString(", ")?.let {
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.secondary.copy(
                                                    alpha = 0.2f
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text(
                                                text = "($it)",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Medium
                                                ),
                                                color = MaterialTheme.colorScheme.onSurface.copy(
                                                    0.8f
                                                ),
                                                modifier = Modifier.padding(
                                                    horizontal = 12.dp,
                                                    vertical = 4.dp
                                                )
                                            )
                                        }
                                    }

                                    wordDetail.translations.find {
                                        it.targetLanguage == tgLang
                                    }?.translation?.let {
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                                    alpha = 0.3f
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(
                                                    horizontal = 16.dp,
                                                    vertical = 8.dp
                                                )
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Phonetics with enhanced styling
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(
                                            alpha = 0.3f
                                        )
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = wordDetail.phonetics.joinToString("  ") { "${if (it.countryCode == "US") "\uD83C\uDDFA\uD83C\uDDF8" else "\uD83C\uDDEC\uD83C\uDDE7"} ${it.pronunciation}" },
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Action buttons with enhanced styling
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    // Speak button
                                    Card(
                                        modifier = Modifier
                                            .clickable {
                                                tts?.speak(
                                                    word,
                                                    TextToSpeech.QUEUE_FLUSH,
                                                    null,
                                                    null
                                                )
                                            },
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.1f
                                            )
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                            contentDescription = "speak",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }

                                    // Add button
                                    Card(
                                        modifier = Modifier
                                            .clickable {
                                                rootNavController.navigate(
                                                    NavScreen.ChooseVocaListNavScreen(
                                                        word = word,
                                                        pronunciation = wordDetail.phonetics.joinToString(
                                                            " "
                                                        ) { it.pronunciation },
                                                        meaning = wordDetail.translations.find {
                                                            it.targetLanguage == tgLang
                                                        }?.translation ?: "",
                                                        type = wordDetail.types.find {
                                                            it.targetLanguage == tgLang
                                                        }?.type?.joinToString(", ") ?: ""
                                                    )
                                                )
                                            },
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.secondary.copy(
                                                alpha = 0.1f
                                            )
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.AddCircle,
                                            contentDescription = "add",
                                            tint = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }

                                    // Copy button
                                    Card(
                                        modifier = Modifier
                                            .clickable {
                                                clipboardManager.setText(AnnotatedString(word))
                                            },
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.tertiary.copy(
                                                alpha = 0.1f
                                            )
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ContentCopy,
                                            contentDescription = "copy",
                                            tint = MaterialTheme.colorScheme.tertiary,
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }
                            }

                            // Language selector with enhanced styling
                            Card(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .clickable {
                                        viewModel.setChosenTargetLanguage(
                                            if (chosenTargetLanguage == 0) 1 else 0
                                        )
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface.copy(0.1f)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = targetLanguages.find { value ->
                                            value.first == tgLang
                                        }?.third?.let { lgInt ->
                                            stringResource(lgInt)
                                        } ?: "",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Image(
                                        painter = painterResource(
                                            id = targetLanguages.find { value ->
                                                value.first == tgLang
                                            }?.second ?: R.drawable.vi_flag
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Tabs with enhanced styling
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        ScrollableTabRow(
                            selectedTabIndex = selectedTabIndex,
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            indicator = { },
                            divider = { }
                        ) {
                            tabTitles.forEachIndexed { index, title ->
                                val selected = selectedTabIndex == index
                                Tab(
                                    selected = selected,
                                    onClick = { selectedTabIndex = index },
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    text = {
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (selected)
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                                else
                                                    MaterialTheme.colorScheme.surface
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text(
                                                text = stringResource(title.second),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                                ),
                                                color = if (selected)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.onSurface.copy(0.7f),
                                                maxLines = 1,
                                                modifier = Modifier.padding(
                                                    horizontal = 16.dp,
                                                    vertical = 8.dp
                                                )
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // Content area with enhanced styling
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth()
                                .verticalScroll(scrollState),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            when (selectedTabIndex) {
                                0 -> {
                                    // Definition
                                    wordDetail.examples.forEach { example ->
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                                    alpha = 0.3f
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = "🔷 ${example.definition}",
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                                example.translations.filter { it.targetLanguage == targetLanguages[chosenTargetLanguage].first }
                                                    .map { it.translation }.forEach {
                                                        Text(
                                                            text = "• $it",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(
                                                                0.8f
                                                            )
                                                        )
                                                    }

                                                example.examples.map {
                                                    Card(
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = MaterialTheme.colorScheme.secondary.copy(
                                                                alpha = 0.1f
                                                            )
                                                        ),
                                                        shape = RoundedCornerShape(8.dp)
                                                    ) {
                                                        Column(
                                                            modifier = Modifier.padding(12.dp)
                                                        ) {
                                                            Text(
                                                                text = "💬 ${it.original}",
                                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                                    fontWeight = FontWeight.Medium
                                                                )
                                                            )
                                                            if (tgLang != "English") {
                                                                Text(
                                                                    text = "→ ${it.translations.translation}",
                                                                    style = MaterialTheme.typography.bodySmall,
                                                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                                                        0.7f
                                                                    )
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                1 -> {
                                    // Idioms
                                    wordDetail.idioms.forEach { idiom ->
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(
                                                    alpha = 0.3f
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = "🔷 ${idiom.idiom}",
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = MaterialTheme.colorScheme.tertiary
                                                )
                                                idiom.translations.filter { it.targetLanguage == tgLang }
                                                    .map { it.translation }.forEach {
                                                        Text(
                                                            text = "• $it",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(
                                                                0.8f
                                                            )
                                                        )
                                                    }
                                            }
                                        }
                                    }
                                }

                                2 -> {
                                    // Synonyms
                                    wordDetail.synonyms.forEach { synonym ->
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                                    alpha = 0.3f
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            ) {
                                                Text(
                                                    text = "🔷 $synonym",
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.weight(1f)
                                                )

                                                // Speak button
                                                Card(
                                                    modifier = Modifier
                                                        .clickable {
                                                            tts?.speak(
                                                                synonym,
                                                                TextToSpeech.QUEUE_FLUSH,
                                                                null,
                                                                null
                                                            )
                                                        },
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.secondary.copy(
                                                            alpha = 0.2f
                                                        )
                                                    ),
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                                        contentDescription = "speak",
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .size(20.dp),
                                                        tint = MaterialTheme.colorScheme.secondary
                                                    )
                                                }

                                                // Search button
                                                Card(
                                                    modifier = Modifier
                                                        .clickable {
                                                            rootNavController.navigate(
                                                                NavScreen.WordDetailNavScreen(
                                                                    synonym
                                                                )
                                                            )
                                                        },
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.tertiary.copy(
                                                            alpha = 0.2f
                                                        )
                                                    ),
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Search,
                                                        contentDescription = "search",
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .size(20.dp),
                                                        tint = MaterialTheme.colorScheme.tertiary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                3 -> {
                                    // Antonyms
                                    wordDetail.antonyms.forEach { antonym ->
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(
                                                    alpha = 0.3f
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            ) {
                                                Text(
                                                    text = "🔷 $antonym",
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.weight(1f)
                                                )

                                                // Speak button
                                                Card(
                                                    modifier = Modifier
                                                        .clickable {
                                                            tts?.speak(
                                                                antonym,
                                                                TextToSpeech.QUEUE_FLUSH,
                                                                null,
                                                                null
                                                            )
                                                        },
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.secondary.copy(
                                                            alpha = 0.2f
                                                        )
                                                    ),
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                                        contentDescription = "speak",
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .size(20.dp),
                                                        tint = MaterialTheme.colorScheme.secondary
                                                    )
                                                }

                                                // Search button
                                                Card(
                                                    modifier = Modifier
                                                        .clickable {
                                                            rootNavController.navigate(
                                                                NavScreen.WordDetailNavScreen(
                                                                    antonym
                                                                )
                                                            )
                                                        },
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.tertiary.copy(
                                                            alpha = 0.2f
                                                        )
                                                    ),
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Search,
                                                        contentDescription = "search",
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .size(20.dp),
                                                        tint = MaterialTheme.colorScheme.tertiary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is UIState.UIError -> {
                // Show error
            }
        }
    }
}