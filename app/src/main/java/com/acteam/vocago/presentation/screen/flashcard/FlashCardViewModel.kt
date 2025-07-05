package com.acteam.vocago.presentation.screen.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.local.entity.VocaListWithVocas
import com.acteam.vocago.domain.usecase.GetVocaListDetailUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class FlashCardViewModel(
    private val getVocaListDetailUseCase: GetVocaListDetailUseCase,
) : ViewModel() {
    private val _listId = MutableStateFlow<Int?>(null)
    val listId: StateFlow<Int?> = _listId

    fun setId(id: Int) {
        _listId.value = id
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val vocaListDetailData: StateFlow<VocaListWithVocas?> = listId
        .filterNotNull()
        .flatMapLatest { id ->
            getVocaListDetailUseCase(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

}