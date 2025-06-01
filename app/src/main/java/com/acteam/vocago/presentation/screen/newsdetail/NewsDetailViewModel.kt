package com.acteam.vocago.presentation.screen.newsdetail

import androidx.lifecycle.ViewModel
import com.acteam.vocago.data.model.NewsDetailDto
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import com.acteam.vocago.domain.usecase.GetNewsDetailUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow

class NewsDetailViewModel(
    private val getNewsDetailUseCase: GetNewsDetailUseCase,
    getLoginStateUseCase: GetLoginStateUseCase,
) : ViewModel() {
    val isAuth = getLoginStateUseCase()

    private val _uiState = MutableStateFlow<UIState<NewsDetailDto>>(UIState.UILoading)
    val uiState = _uiState

    suspend fun getNewsDetail(id: String) {
        _uiState.value = UIState.UILoading
        val newsDetail = getNewsDetailUseCase(id)
        if (newsDetail == null) {
            _uiState.value = UIState.UIError(
                UIErrorType.NotFoundError
            )
            return
        }
        _uiState.value = UIState.UISuccess(newsDetail)
    }
}