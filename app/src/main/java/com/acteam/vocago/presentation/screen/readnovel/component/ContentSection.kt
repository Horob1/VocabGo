package com.acteam.vocago.presentation.screen.readnovel.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.acteam.vocago.data.model.ChapterDto
import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.domain.model.NovelTheme
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.utils.DateDisplayHelper

@Composable
fun ContentSection(
    modifier: Modifier = Modifier,
    chapter: ChapterDto,
    fontSize: Float = 16f,
    theme: NovelTheme = NovelTheme.Light,
    author: String = "Author",
    fontFamily: FontFamily,
    wordUiState: UIState<WordDto>,
    setSelectedWord: (String) -> Unit,
    navController: NavController,
) {
    var selectedWord by remember { mutableStateOf<String?>(null) }

    val chapterData = chapter.chapter

    val contents = remember(chapterData.content) {
        chapterData.content.split("\n")
    }

    val lineHeight = (fontSize + 4).sp
    val authorFontSize = (fontSize - 4).coerceAtLeast(10f).sp

    LazyColumn(modifier = modifier) {
        item {
            Spacer(modifier = Modifier.height(fontSize.dp))
        }

        itemsIndexed(contents) { _, line ->
            var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

            val annotatedText = buildAnnotatedString {
                val words = line.split(" ")
                words.forEachIndexed { index, word ->
                    val cleanedWord = word.trim()
                    val originWord =
                        cleanedWord.replace(Regex("^[^\\p{L}\\p{N}]+|[^\\p{L}\\p{N}]+$"), "")
                    val leadingTrim =
                        Regex("^[^\\p{L}\\p{N}]+").find(cleanedWord)?.value?.length ?: 0

                    val start = this.length + leadingTrim
                    append(word)
                    val end = start + originWord.length

                    addStringAnnotation("WORD", originWord, start, end)

                    if (index != words.lastIndex) append("  ")
                }
            }

            Text(
                text = annotatedText,
                fontSize = fontSize.sp,
                lineHeight = lineHeight,
                fontFamily = fontFamily,
                color = theme.textColor,
                onTextLayout = { textLayoutResult = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            textLayoutResult?.let { layout ->
                                val position = layout.getOffsetForPosition(offset)
                                annotatedText.getStringAnnotations("WORD", position, position)
                                    .firstOrNull()
                                    ?.let { annotation ->
                                        selectedWord = annotation.item
                                        setSelectedWord(annotation.item)
                                    }
                            }
                        }
                    }
            )
        }

        item {
            Spacer(modifier = Modifier.height(fontSize.dp))
        }

        item {
            Text(
                text = "$author - ${DateDisplayHelper.formatDateString(chapterData.createdAt)}",
                fontSize = authorFontSize,
                lineHeight = lineHeight,
                color = theme.textColor,
                textDecoration = TextDecoration.Underline,
                fontFamily = fontFamily,
                fontStyle = FontStyle.Italic
            )
        }

        item {
            Spacer(modifier = Modifier.height(fontSize.dp))
        }
    }

    selectedWord?.let {
        WordCard(
            word = it,
            modifier = Modifier.fillMaxWidth(),
            onHideCard = {
                selectedWord = null
            },
            wordUiState = wordUiState,
            rootNavController = navController
        )
    }
}
