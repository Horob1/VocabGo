package com.acteam.vocago.presentation.screen.noveldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.NovelDetailDto
import com.acteam.vocago.domain.usecase.GetLastReadChapterUseCase
import com.acteam.vocago.domain.usecase.GetNovelDetailUseCase
import com.acteam.vocago.domain.usecase.UpdateReadChapterUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NovelDetailViewModel(
    val getNovelDetailUseCase: GetNovelDetailUseCase,
    val updateReadChapterUseCase: UpdateReadChapterUseCase,
    val getReadChapterUseCase: GetLastReadChapterUseCase,
) : ViewModel() {
    private val _novelDetail = MutableStateFlow<UIState<NovelDetailDto>>(UIState.UILoading)

    private val _readChapter = MutableStateFlow<String>("")
    val readChapter = _readChapter
    val novelDetail = _novelDetail

    fun loadNovel(id: String) {
        _novelDetail.value = UIState.UILoading
        viewModelScope.launch {
            val result = getNovelDetailUseCase(id)
            _novelDetail.value =
                if (result == null) UIState.UIError(UIErrorType.UnknownError) else UIState.UISuccess(
                    result
                )
            val readChapter = getReadChapterUseCase(id)
            _readChapter.value = readChapter?.chapterId ?: ""
        }
    }

    fun updateReadChapter(
        novelId: String,
        chapterId: String,
    ) {
        viewModelScope.launch {
            updateReadChapterUseCase(novelId, chapterId)
        }
    }
}