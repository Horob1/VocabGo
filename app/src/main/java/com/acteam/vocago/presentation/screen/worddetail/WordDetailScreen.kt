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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.clip
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
import com.acteam.vocago.utils.responsiveDP
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
                    Text(text = word, style = MaterialTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = { rootNavController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
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
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                responsiveDP(
                                    mobile = 300,
                                    tabletPortrait = 400,
                                    tabletLandscape = 400
                                )
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = word, style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                wordDetail.types.find { value ->
                                    value.targetLanguage == tgLang
                                }?.type?.joinToString(
                                    ", "
                                )?.let {
                                    Text(
                                        text = "($it)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                }
                                wordDetail.translations.find {
                                    it.targetLanguage == tgLang
                                }?.translation?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary.copy(0.7f)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                            Text(
                                text = wordDetail.phonetics.joinToString("  ") { "${if (it.countryCode == "US") "\uD83C\uDDFA\uD83C\uDDF8" else "\uD83C\uDDEC\uD83C\uDDE7"} : ${it.pronunciation}" },
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(
                                            MaterialTheme.shapes.medium
                                        )
                                        .clickable {
                                            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                        }
                                        .background(
                                            MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = 0.3f
                                            ),
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .padding(
                                            responsiveDP(
                                                mobile = 4,
                                                tabletPortrait = 8,
                                                tabletLandscape = 12
                                            )
                                        ),

                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                        contentDescription = "speak",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(
                                            MaterialTheme.shapes.medium
                                        )
                                        .clickable {
                                            rootNavController.navigate(
                                                NavScreen.ChooseVocaListNavScreen(
                                                    word
                                                )
                                            )
                                        }
                                        .background(
                                            MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = 0.3f
                                            ),
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .padding(
                                            responsiveDP(
                                                mobile = 4,
                                                tabletPortrait = 8,
                                                tabletLandscape = 12
                                            )
                                        ),

                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AddCircle,
                                        contentDescription = "add",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                //copy
                                Box(
                                    modifier = Modifier
                                        .clip(
                                            MaterialTheme.shapes.medium
                                        )
                                        .clickable {
                                            clipboardManager.setText(AnnotatedString(word))
                                        }
                                        .background(
                                            MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = 0.3f
                                            ),
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .padding(
                                            responsiveDP(
                                                mobile = 4,
                                                tabletPortrait = 8,
                                                tabletLandscape = 12
                                            )
                                        ),

                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentCopy,
                                        contentDescription = "copy",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .clickable {
                                    viewModel.setChosenTargetLanguage(
                                        if (chosenTargetLanguage == 0) 1 else 0
                                    )
                                }
                                .clip(RoundedCornerShape(8.dp))

                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = targetLanguages.find { value ->
                                        value.first == tgLang
                                    }?.third?.let { lgInt ->
                                        stringResource(
                                            lgInt
                                        )
                                    } ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.surface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Image(
                                    painter = painterResource(
                                        id = targetLanguages.find { value ->
                                            value.first == tgLang
                                        }?.second
                                            ?: R.drawable.vi_flag
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    HorizontalDivider()
                    ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
                        tabTitles.forEachIndexed { index, title ->
                            val selected = selectedTabIndex == index
                            Tab(
                                selected = selected,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Text(
                                            text = stringResource(title.second),
                                            style = MaterialTheme.typography.bodyLarge,
                                            maxLines = 1,
                                        )
                                    }
                                }
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when (selectedTabIndex) {
                            0 -> {
                                // Definition
                                wordDetail.examples.forEach { example ->
                                    Text(
                                        text = "🔷 ${example.definition}",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    example.translations.filter { it.targetLanguage == targetLanguages[chosenTargetLanguage].first }
                                        .map { it.translation }.forEach {
                                            Text(
                                                text = " - $it",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }

                                    example.examples.map {
                                        Text(
                                            text = " • ${it.original}",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        if (tgLang != "English") {
                                            Text(
                                                text = " - ${it.translations.translation}",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                    }
                                }
                            }

                            1 -> {
                                // Idioms
                                wordDetail.idioms.forEach { idiom ->
                                    Text(
                                        text = "🔷 ${idiom.idiom}",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    idiom.translations.filter { it.targetLanguage == tgLang }
                                        .map { it.translation }.forEach {
                                            Text(
                                                text = " - $it",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                }
                            }

                            2 -> {
                                // Synonyms
                                wordDetail.synonyms.forEach { synonym ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Text(
                                            text = "🔷 $synonym",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        //Speak
                                        IconButton(
                                            onClick = {
                                                tts?.speak(
                                                    synonym,
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
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        //navigate to word detail screen
                                        IconButton(
                                            onClick = {
                                                rootNavController.navigate(
                                                    NavScreen.WordDetailNavScreen(
                                                        synonym
                                                    )
                                                )
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Search,
                                                contentDescription = "search",
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            3 -> {
                                wordDetail.antonyms.forEach { antonym ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Text(
                                            text = "🔷 $antonym",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )

                                        //Speak
                                        IconButton(
                                            onClick = {
                                                tts?.speak(
                                                    antonym,
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
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }

                                        //navigate to word detail screen
                                        IconButton(
                                            onClick = {
                                                rootNavController.navigate(
                                                    NavScreen.WordDetailNavScreen(
                                                        antonym
                                                    )
                                                )
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Search,
                                                contentDescription = "search",
                                                modifier = Modifier.size(24.dp)
                                            )
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