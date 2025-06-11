package com.acteam.vocago.presentation.screen.newshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.acteam.vocago.domain.usecase.GetNewsHistoryPagingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class NewsHistoryViewModel(
    private val getNewsHistoryPagingUseCase: GetNewsHistoryPagingUseCase,
) : ViewModel() {

    val newsHistory = getNewsHistoryPagingUseCase().cachedIn(viewModelScope).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PagingData.empty()
    )

    private val _isBookmark = MutableStateFlow(false)
    val isBookmark = _isBookmark

    fun setIsBookmark(isBookmark: Boolean) {
        _isBookmark.value = isBookmark
    }
}