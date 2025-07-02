package com.acteam.vocago.presentation.screen.searchnovel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.domain.usecase.GetSearchNovelPagingFlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class SearchNovelViewModel(
    private val getSearchNovelPagingFlowUseCase: GetSearchNovelPagingFlowUseCase,
) : ViewModel() {

    private val _keySearch = MutableStateFlow("")
    val keySearch = _keySearch

    fun setKeySearch(keySearch: String) {
        _keySearch.value = keySearch
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val novelPagingFlow: Flow<PagingData<Novel>> = _keySearch
        .flatMapLatest { keyword ->
            getSearchNovelPagingFlowUseCase(keyword)
        }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PagingData.empty()
        )
}
