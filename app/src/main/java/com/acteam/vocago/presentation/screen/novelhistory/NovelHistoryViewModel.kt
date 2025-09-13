package com.acteam.vocago.presentation.screen.novelhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.usecase.GetReadNovelFlowUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class NovelHistoryViewModel(
    private val getNovelPagingFlowUseCase: GetReadNovelFlowUseCase,
) : ViewModel() {
    val novelPagingFlow = getNovelPagingFlowUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )
}