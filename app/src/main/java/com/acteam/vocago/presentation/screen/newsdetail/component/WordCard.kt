package com.acteam.vocago.presentation.screen.newsdetail.component

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.newsdetail.NewsDetailViewModel
import com.acteam.vocago.utils.responsiveDP
import java.util.Locale

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    word: String,
    rootNavController: NavController,
    viewModel: NewsDetailViewModel,
    onHideCard: () -> Unit,
) {
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
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = remember {
        listOf(
            Triple("Vi", R.drawable.vietnam, "Vietnamese"),
            Triple("En", R.drawable.unitedkingdom, "English"),
        )
    }
    val iconSize = remember { 18.dp }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = title.second),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(title.first)
                            }
                        }
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        word, style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                        },
                        enabled = tts != null
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                            contentDescription = "speak",
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (wordUiState is UIState.UISuccess) {
                                onHideCard()
                                rootNavController.navigate(NavScreen.ChooseVocaListNavScreen(word))
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "add",
                            modifier = Modifier.size(iconSize)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(word))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = "copy",
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 8.dp)
                    .height(
                        if (wordUiState !is UIState.UIError) responsiveDP(
                            120,
                            200,
                            250
                        ) else 0.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 16.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    when (wordUiState) {
                        is UIState.UILoading -> {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }

                        }

                        is UIState.UISuccess -> {

                            val wordDetail = (wordUiState as UIState.UISuccess<WordDto>).data

                            Text(
                                text = wordDetail.phonetics.joinToString("  ") { "${if (it.countryCode == "US") "\uD83C\uDDFA\uD83C\uDDF8" else "\uD83C\uDDEC\uD83C\uDDE7"} : ${it.pronunciation}" },
                                style = MaterialTheme.typography.bodyLarge
                            )

                            wordDetail.types.find { it.targetLanguage == tabTitles[selectedTabIndex].third }?.type?.joinToString(
                                "  "
                            )?.let {
                                Text(
                                    text = "⭐ $it",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }


                            wordDetail.translations.find { it.targetLanguage == tabTitles[selectedTabIndex].third }?.translation?.let {
                                Text(
                                    text = "⭐ $it",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            wordDetail.examples.forEach { example ->
                                Text(
                                    text = "🔷 ${example.definition}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                example.translations.filter { it.targetLanguage == tabTitles[selectedTabIndex].third }
                                    .map { it.translation }.forEach {
                                        Text(
                                            text = " - $it",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                            }
                        }

                        else -> {

                        }
                    }
                }
                val scrollRatio =
                    scrollState.value.toFloat() / (scrollState.maxValue.toFloat() + 0.01f)

                val scrollbarHeight = 30.dp

                val scrollbarOffset = with(LocalDensity.current) {
                    (scrollRatio * (100.dp.toPx() - scrollbarHeight.toPx())).toDp()
                }

                if (scrollState.maxValue > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(y = scrollbarOffset)
                            .width(4.dp)
                            .height(scrollbarHeight)
                            .background(Color.Gray, shape = RoundedCornerShape(2.dp))
                    )
                }
            }

            if (wordUiState is UIState.UISuccess)
                Button(
                    onClick = {
                        onHideCard()
                        rootNavController.navigate(NavScreen.WordDetailNavScreen(word))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(stringResource(R.string.more_content))
                }
        }
    }
}
