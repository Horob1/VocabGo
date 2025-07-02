package com.acteam.vocago.presentation.screen.noveldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.NovelDetailDto
import com.acteam.vocago.domain.usecase.GetNovelDetailUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NovelDetailViewModel(
    val getNovelDetailUseCase: GetNovelDetailUseCase,
) : ViewModel() {
    private val _novelDetail = MutableStateFlow<UIState<NovelDetailDto>>(UIState.UILoading)
    val novelDetail = _novelDetail

    fun loadNovel(id: String) {
        _novelDetail.value = UIState.UILoading
        viewModelScope.launch {
            val result = getNovelDetailUseCase(id)
            _novelDetail.value =
                if (result == null) UIState.UIError(UIErrorType.UnknownError) else UIState.UISuccess(
                    result
                )
        }
    }
}