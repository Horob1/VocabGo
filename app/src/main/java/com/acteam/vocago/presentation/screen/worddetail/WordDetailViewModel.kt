package com.acteam.vocago.presentation.screen.worddetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.domain.usecase.GetDictionaryWordUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WordDetailViewModel(
    private val getDictionaryWordUseCase: GetDictionaryWordUseCase,
) : ViewModel() {
    private val _wordUiState = MutableStateFlow<UIState<WordDto>>(UIState.UILoading)
    val wordUiState = _wordUiState

    private val _chosenTargetLanguage = MutableStateFlow(0)
    val chosenTargetLanguage = _chosenTargetLanguage

    fun setChosenTargetLanguage(language: Int) {
        _chosenTargetLanguage.value = language
    }

    fun getWordDetail(word: String) {
        viewModelScope.launch {
            _wordUiState.value = UIState.UILoading
            val result = getDictionaryWordUseCase(word)
            if (result.isSuccess && result.getOrNull() != null) {
                _wordUiState.value = UIState.UISuccess(result.getOrNull()!!)
            } else {
                _wordUiState.value = UIState.UIError(UIErrorType.NotFoundError)
            }
        }
    }
}