package com.acteam.vocago.presentation.screen.readnovel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ChapterDto
import com.acteam.vocago.data.model.NovelDetailDto
import com.acteam.vocago.domain.usecase.GetChapterUseCase
import com.acteam.vocago.domain.usecase.GetNovelDetailUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReadNovelViewModel(
    private val getNovelDetailUseCase: GetNovelDetailUseCase,
    private val getChapterUseCase: GetChapterUseCase,
) : ViewModel() {

    private val _novelDetail = MutableStateFlow<UIState<NovelDetailDto>>(UIState.UILoading)
    val novelDetail: StateFlow<UIState<NovelDetailDto>> = _novelDetail

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
                } else {
                    onError()
                    return@launch
                }
            }

            if (result.nextChapter != null) {
                val nextResult = _chapters.value.find { it.chapter._id == result.nextChapter._id }
                    ?: getChapterUseCase(result.nextChapter._id)
                if (nextResult != null) {
                    temp.add(nextResult)
                } else {
                    onError()
                    return@launch
                }
            }
            _chapters.value = temp
        }
    }
}
