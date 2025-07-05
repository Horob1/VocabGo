package com.acteam.vocago.presentation.screen.worddetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.data.model.WordOfTheDaySimpleDto
import com.acteam.vocago.domain.usecase.GetDictionaryWordUseCase
import com.acteam.vocago.domain.usecase.GetSuggestWordUseCase
import com.acteam.vocago.domain.usecase.GetWordOfTheDayUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.dictionary.SearchHistoryManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WordDetailViewModel(
    private val getDictionaryWordUseCase: GetDictionaryWordUseCase,
    private val getSuggestWordUseCase: GetSuggestWordUseCase,
    private val getWordOfTheDayUseCase: GetWordOfTheDayUseCase,
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _wordUiState = MutableStateFlow<UIState<WordDto>>(UIState.UILoading)
    val wordUiState = _wordUiState

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions

    private val _selectedWord = MutableStateFlow<String?>(null)
    val selectedWord: StateFlow<String?> = _selectedWord

    private val _recentWords = MutableStateFlow<List<String>>(emptyList())
    val recentWords: StateFlow<List<String>> = _recentWords

    private val _chosenTargetLanguage = MutableStateFlow(0)
    val chosenTargetLanguage = _chosenTargetLanguage

    fun setChosenTargetLanguage(language: Int) {
        _chosenTargetLanguage.value = language
    }

    private val _wordOfTheDayUiState =
        MutableStateFlow<UIState<WordOfTheDaySimpleDto>>(UIState.UILoading)
    val wordOfTheDayUiState: StateFlow<UIState<WordOfTheDaySimpleDto>> = _wordOfTheDayUiState

    private val _wordOfTheDayState =
        MutableStateFlow<UIState<WordOfTheDaySimpleDto>>(UIState.UILoading)
    val wordOfTheDayState: StateFlow<UIState<WordOfTheDaySimpleDto>> = _wordOfTheDayState

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
        fetchSuggestions(newText)
    }

    private fun fetchSuggestions(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isBlank()) {
                _suggestions.value = emptyList()
                return@launch
            }
            val result = getSuggestWordUseCase(query)
            result
                .onSuccess { suggestions ->
                    _suggestions.value = suggestions
                }
                .onFailure { error ->
                    _suggestions.value = emptyList()
                }
        }
    }

    fun onSuggestionClick(context: Context, word: String) {
        _selectedWord.value = word
        getWordDetail(word)
        saveSearchHistory(context, word)
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

    private fun saveSearchHistory(context: Context, word: String) {
        viewModelScope.launch {
            SearchHistoryManager.saveSearch(context, word)
            loadRecentSearches(context)
        }
    }

    fun loadRecentSearches(context: Context) {
        viewModelScope.launch {
            val recent = SearchHistoryManager.getRecentSearches(context)
            _recentWords.value = recent
        }
    }

    fun clearSelectedWord() {
        _selectedWord.value = null
    }

    fun loadWordOfTheDay() {
        viewModelScope.launch {
            _wordOfTheDayState.value = UIState.UILoading
            val result = getWordOfTheDayUseCase()
            result
                .onSuccess {
                    _wordOfTheDayState.value = UIState.UISuccess(it)
                }
                .onFailure {
                    _wordOfTheDayState.value = UIState.UIError(UIErrorType.NetworkError)
                }
        }
    }

}