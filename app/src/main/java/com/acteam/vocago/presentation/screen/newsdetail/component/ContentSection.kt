package com.acteam.vocago.presentation.screen.newsdetail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.acteam.vocago.data.model.TranslationNewsDto
import com.acteam.vocago.data.model.WordNewsDto
import com.acteam.vocago.presentation.screen.newsdetail.NewsDetailViewModel

@Composable
fun ContentSection(
    modifier: Modifier = Modifier,
    wordList: List<WordNewsDto>,
    translations: List<TranslationNewsDto>,
    content: String,
    rootNavController: NavController,
    viewModel: NewsDetailViewModel,
) {
    val isShowTranslate by viewModel.isShowTranslate.collectAsState()

    val paragraphs by remember(content) {
        derivedStateOf {
            content.split("\n")
        }
    }

    val translationMap by remember(translations) {
        derivedStateOf {
            translations.associateBy { it.targetLanguage }
        }
    }

    val translationVi by remember(translationMap) {
        derivedStateOf {
            translationMap["Vietnamese"]?.translation?.split("\n") ?: emptyList()
        }
    }


    var selectedWord by remember { mutableStateOf<String?>(null) }
    var selectedOffset by remember { mutableStateOf<Int?>(null) }
    var wordOffset by remember { mutableStateOf<Offset?>(null) }
    var rootCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .onGloballyPositioned {
                    rootCoordinates = it
                }
        ) {
            paragraphs.forEachIndexed { index, para ->
                var textCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
                val translatedParagraph = translationVi.getOrNull(index) ?: ""

                ClickableParagraph(
                    paragraph = para,
                    translatedParagraph = translatedParagraph,
                    wordList = wordList,
                    onWordClick = { word, offset, boxOffset ->
                        // Tính tọa độ word relative root Box
                        if (rootCoordinates != null && textCoordinates != null) {
                            val rootPos = rootCoordinates!!.positionInWindow()
                            val textPos = textCoordinates!!.positionInWindow()

                            // relative offset trong root
                            val relativeOffset = textPos + boxOffset - rootPos

                            selectedWord = word
                            selectedOffset = offset
                            wordOffset = relativeOffset

                            viewModel.getWordDetail(word)
                        }
                    },
                    selectedWord = selectedWord,
                    selectedOffset = selectedOffset,
                    onTextCoordinatesReady = { coords ->
                        textCoordinates = coords
                    },
                    isShowTranslate = isShowTranslate
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (wordOffset != null && selectedWord != null) {
            Popup(
                offset =
                    IntOffset(
                        0,
                        wordOffset!!.y.toInt() + 24  // dịch xuống 8dp
                    ),
                onDismissRequest = { selectedWord = null }
            ) {
                WordCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    word = selectedWord!!.lowercase(),
                    rootNavController = rootNavController,
                    viewModel = viewModel,
                    onHideCard = { selectedWord = null }
                )
            }
        }
    }

}

@Composable
fun ClickableParagraph(
    paragraph: String,
    translatedParagraph: String,
    wordList: List<WordNewsDto>,
    onWordClick: (word: String, offset: Int, boxOffset: Offset) -> Unit,
    selectedWord: String?,
    selectedOffset: Int?,
    isShowTranslate: Boolean,
    onTextCoordinatesReady: (LayoutCoordinates) -> Unit,
) {
    val words = paragraph.split(" ")
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val wordMap = remember(wordList) { wordList.associateBy { it.word } }

    val annotatedText = remember(paragraph, selectedWord, selectedOffset) {
        buildAnnotatedString {
            words.forEachIndexed { index, word ->
                val cleanedWord = word.trim()
                val originWord =
                    cleanedWord.replace(Regex("^[^\\p{L}\\p{N}]+|[^\\p{L}\\p{N}]+$"), "")
                val leadingTrim = Regex("^[^\\p{L}\\p{N}]+").find(cleanedWord)?.value?.length ?: 0
                val start = this.length + leadingTrim
                append(word)
                val end = start + originWord.length

                if (originWord == selectedWord && selectedOffset in start until end) {
                    addStyle(
                        SpanStyle(background = onSurfaceColor.copy(alpha = 0.3f)),
                        start,
                        end
                    )
                } else {
                    addStyle(SpanStyle(color = onSurfaceColor), start, end)
                }

                val wordInfo = wordMap[originWord]

                if (word == selectedWord && selectedOffset in start until end) {
                    addStyle(
                        SpanStyle(background = onSurfaceColor.copy(alpha = 0.3f)),
                        start, end
                    )
                } else {
                    addStyle(SpanStyle(color = onSurfaceColor), start, end)
                }

                if (wordInfo != null) {
                    val color = when (wordInfo.level) {
                        "A1" -> Color(0xFF81C784)
                        "A2" -> Color(0xFF64B5F6)
                        "B1" -> Color(0xFF8B8607)
                        "B2" -> Color(0xFFEF9A9A)
                        else -> onSurfaceColor
                    }
                    addStyle(
                        SpanStyle(textDecoration = TextDecoration.Underline, color = color),
                        start,
                        end
                    )
                }

                addStringAnnotation("WORD", word, start, end)

                if (index != words.lastIndex) append("  ")
            }
        }
    }

    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = annotatedText,
        style = TextStyle(fontSize = 16.sp, lineHeight = 30.sp, letterSpacing = 0.5.sp),
        onTextLayout = { layoutResult = it },
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                onTextCoordinatesReady(it)
            }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    layoutResult?.let { layout ->
                        val offset = layout.getOffsetForPosition(tapOffset)

                        annotatedText.getStringAnnotations(
                            tag = "WORD",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let { annotation ->
                            val boundingBox = layout.getBoundingBox(offset)
                            val fallbackBoxOffset = Offset(tapOffset.x, tapOffset.y)
                            val boxOffset = if (
                                boundingBox.width != 0f && boundingBox.height != 0f
                            ) boundingBox.bottomLeft else fallbackBoxOffset

                            val originWord =
                                annotation.item.trim()
                                    .replace(Regex("^[^\\p{L}\\p{N}]+|[^\\p{L}\\p{N}]+$"), "")

                            onWordClick(originWord, offset, boxOffset)
                        }
                    }
                }
            }
    )

    Spacer(modifier = Modifier.height(16.dp))

    AnimatedVisibility(
        visible = isShowTranslate,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Text(
            text = translatedParagraph,
            style = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.small)
        )
    }
}
