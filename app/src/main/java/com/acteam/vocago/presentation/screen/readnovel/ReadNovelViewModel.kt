package com.acteam.vocago.presentation.screen.readnovel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ChapterDto
import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.domain.model.NovelDetail
import com.acteam.vocago.domain.model.NovelFont
import com.acteam.vocago.domain.model.NovelTheme
import com.acteam.vocago.domain.usecase.GetChapterUseCase
import com.acteam.vocago.domain.usecase.GetDictionaryWordUseCase
import com.acteam.vocago.domain.usecase.GetFontFamilyUseCase
import com.acteam.vocago.domain.usecase.GetNovelDetailUseCase
import com.acteam.vocago.domain.usecase.GetReadNovelFontSizeUseCase
import com.acteam.vocago.domain.usecase.GetReadNovelThemeUseCase
import com.acteam.vocago.domain.usecase.SetFontFamilyUseCase
import com.acteam.vocago.domain.usecase.SetNovelThemeUseCase
import com.acteam.vocago.domain.usecase.SetReadNovelFontSizeUseCase
import com.acteam.vocago.domain.usecase.UpdateReadChapterUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReadNovelViewModel(
    private val getNovelDetailUseCase: GetNovelDetailUseCase,
    private val getChapterUseCase: GetChapterUseCase,
    getReadNovelThemeUseCase: GetReadNovelThemeUseCase,
    getReadNovelFontSizeUseCase: GetReadNovelFontSizeUseCase,
    private val setReadNovelFontSizeUseCase: SetReadNovelFontSizeUseCase,
    private val setReadNovelThemeUseCase: SetNovelThemeUseCase,
    private val setFontFamilyUseCase: SetFontFamilyUseCase,
    getFontFamilyUseCase: GetFontFamilyUseCase,
    private val getDictionaryWordUseCase: GetDictionaryWordUseCase,
    private val updateReadChapterUseCase: UpdateReadChapterUseCase,
) : ViewModel() {
    val fontFamily = getFontFamilyUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NovelFont.RobotoSerif
    )


    private val _wordUiState = MutableStateFlow<UIState<WordDto>>(UIState.UILoading)
    val wordUiState = _wordUiState

    private val _novelDetail = MutableStateFlow<UIState<NovelDetail>>(UIState.UILoading)
    val novelDetail: StateFlow<UIState<NovelDetail>> = _novelDetail

    val theme = getReadNovelThemeUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NovelTheme.Light
    )
    val fontSize = getReadNovelFontSizeUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 16f
    )

    private val _chapters = MutableStateFlow<List<ChapterDto>>(emptyList())
    val chapters: StateFlow<List<ChapterDto>> = _chapters

    fun loadNovel(id: String) {
        _novelDetail.value = UIState.UILoading
        viewModelScope.launch {
            val result = getNovelDetailUseCase(id)
            _novelDetail.value = result?.let {
                UIState.UISuccess(it)
            } ?: UIState.UIError(UIErrorType.UnknownError)
        }
    }

    fun loadChapters(id: String, onError: () -> Unit) {
        viewModelScope.launch {

            val temp = mutableListOf<ChapterDto>()
            val result = _chapters.value.find { it.chapter._id == id } ?: getChapterUseCase(id)
            if (result == null) {
                onError()
                return@launch
            }
            temp.add(result)

            if (result.previousChapter != null) {
                val previousResult =
                    _chapters.value.find { it.chapter._id == result.previousChapter._id }
                        ?: getChapterUseCase(result.previousChapter._id)
                if (previousResult != null) {
                    temp.add(previousResult)
                }
            }

            if (result.nextChapter != null) {
                val nextResult = _chapters.value.find { it.chapter._id == result.nextChapter._id }
                    ?: getChapterUseCase(result.nextChapter._id)
                if (nextResult != null) {
                    temp.add(nextResult)
                }
            }
            _chapters.value = temp
        }
    }

    fun setFontSize(size: Float, onError: () -> Unit) {
        if (size < 10 || size > 30) {
            onError()
            return
        }
        viewModelScope.launch {
            try {
                setReadNovelFontSizeUseCase(size)
            } catch (_: Exception) {
                // handle error
            }
        }
    }

    fun setTheme(themeName: String) {
        viewModelScope.launch {
            try {
                setReadNovelThemeUseCase(themeName)
            } catch (_: Exception) {
                // handle error
            }
        }
    }

    fun setFontFamily(fontFamilyName: String) {
        viewModelScope.launch {
            try {
                setFontFamilyUseCase(fontFamilyName)
            } catch (_: Exception) {
                // handle error
            }
        }
    }

    fun getWordDetail(word: String) {
        viewModelScope.launch {
            _wordUiState.value = UIState.UILoading
            val result = getDictionaryWordUseCase(word)
            if (result.isSuccess && result.getOrNull() != null) {
                _wordUiState.value = UIState.UISuccess(result.getOrNull()!!)
            } else {
                _wordUiState.value = UIState.UIError(UIErrorType.NotFoundError)
            }
        }
    }

    fun updateReadChapter(chapterId: String, novelId: String) {
        viewModelScope.launch {
            updateReadChapterUseCase(novelId, chapterId)
        }
    }
}
