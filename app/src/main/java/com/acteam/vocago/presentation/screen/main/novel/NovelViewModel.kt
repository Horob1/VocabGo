package com.acteam.vocago.presentation.screen.main.novel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.usecase.GetNovelFirstPageUseCase
import com.acteam.vocago.domain.usecase.GetReadNovelFirstPageUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NovelViewModel(
    private val getNovelFirstPageUseCase: GetNovelFirstPageUseCase,
    private val getReadNovelFirstPageUseCase: GetReadNovelFirstPageUseCase,
) : ViewModel() {

    private val _novelFirstPage = MutableStateFlow<UIState<List<Novel>>>(UIState.UILoading)
    val novelFirstPage = _novelFirstPage

    private val _readNovelFirstPage = MutableStateFlow<UIState<List<Novel>>>(UIState.UILoading)
    val readNovelFirstPage = _readNovelFirstPage

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

    fun loadReadNovelFirstPage() {
        _readNovelFirstPage.value = UIState.UILoading
        viewModelScope.launch {
            val result = getReadNovelFirstPageUseCase()

            _readNovelFirstPage.value = UIState.UISuccess(result)
        }
    }

}