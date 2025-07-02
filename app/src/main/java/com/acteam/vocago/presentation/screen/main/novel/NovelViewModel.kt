package com.acteam.vocago.presentation.screen.main.novel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.usecase.GetNovelFirstPageUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NovelViewModel(
    private val getNovelFirstPageUseCase: GetNovelFirstPageUseCase,
) : ViewModel() {
    private val _connectMode = MutableStateFlow<Boolean>(true)
    val connectMode = _connectMode
    fun setConnectMode(mode: Boolean) {
        _connectMode.value = mode
    }

    private val _novelFirstPage = MutableStateFlow<UIState<List<Novel>>>(UIState.UILoading)
    val novelFirstPage = _novelFirstPage

    fun loadNovelFirstPage() {
        _novelFirstPage.value = UIState.UILoading
        viewModelScope.launch {
            val result = getNovelFirstPageUseCase()
            if (result.isSuccess) {
                _novelFirstPage.value = UIState.UISuccess(result.getOrDefault(emptyList()))
            } else {
                _novelFirstPage.value = UIState.UIError(
                    UIErrorType.UnknownError
                )
            }
        }
    }
}