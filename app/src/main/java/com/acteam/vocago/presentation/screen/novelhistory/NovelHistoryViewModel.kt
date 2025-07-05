package com.acteam.vocago.presentation.screen.novelhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.acteam.vocago.domain.usecase.GetReadNovelPagingFlowUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class NovelHistoryViewModel(
    private val getNovelPagingFlowUseCase: GetReadNovelPagingFlowUseCase,
) : ViewModel() {
    val novelPagingFlow = getNovelPagingFlowUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PagingData.empty()
    )
}