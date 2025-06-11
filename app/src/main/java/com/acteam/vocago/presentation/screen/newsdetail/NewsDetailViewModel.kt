package com.acteam.vocago.presentation.screen.newsdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.NewsDetailDto
import com.acteam.vocago.data.model.NewsDetailLogQsaDto
import com.acteam.vocago.data.model.WordDto
import com.acteam.vocago.domain.usecase.AnswerNewsUseCase
import com.acteam.vocago.domain.usecase.GetDictionaryWordUseCase
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import com.acteam.vocago.domain.usecase.GetNewsDetailUseCase
import com.acteam.vocago.domain.usecase.ToggleBookmarkNewsUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.newsdetail.data.PracticeData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsDetailViewModel(
    private val getNewsDetailUseCase: GetNewsDetailUseCase,
    getLoginStateUseCase: GetLoginStateUseCase,
    private val answerNewsUseCase: AnswerNewsUseCase,
    private val toggleBookmarkNewsUseCase: ToggleBookmarkNewsUseCase,
    private val getDictionaryWordUseCase: GetDictionaryWordUseCase,
) : ViewModel() {
    val isAuth = getLoginStateUseCase()

    private val _isShowTranslate = MutableStateFlow(false)
    val isShowTranslate = _isShowTranslate

    private val _uiState = MutableStateFlow<UIState<NewsDetailDto>>(UIState.UILoading)
    val uiState = _uiState

    private val _practiceData = MutableStateFlow(PracticeData())
    val practiceData = _practiceData

    private val _wordUiState = MutableStateFlow<UIState<WordDto>>(UIState.UILoading)
    val wordUiState = _wordUiState

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

    fun chooseAnswer(questionIndex: Int, answerIndex: Int) {
        if (questionIndex >= _practiceData.value.selectedOptions.size) return
        _practiceData.value = _practiceData.value.copy(
            selectedOptions = _practiceData.value.selectedOptions.toMutableList().apply {
                this[questionIndex] = answerIndex
            }
        )
    }

    fun toggleTranslate() {
        _isShowTranslate.value = !_isShowTranslate.value
    }

    fun toggleBookmark(isBookmarked: Boolean) {
        if (isAuth.value && _uiState.value is UIState.UISuccess) {
            //make local change
            _uiState.value = (_uiState.value as UIState.UISuccess<NewsDetailDto>).copy(
                data = (_uiState.value as UIState.UISuccess<NewsDetailDto>).data.copy(
                    log = (_uiState.value as UIState.UISuccess<NewsDetailDto>).data.log?.copy(
                        isBookmarked = isBookmarked
                    )
                )
            )

            val id = (_uiState.value as UIState.UISuccess<NewsDetailDto>).data._id
            viewModelScope.launch {
                val result = toggleBookmarkNewsUseCase(id, isBookmarked)
                if (result.isFailure) {
                    //rollback local change
                    _uiState.value = (_uiState.value as UIState.UISuccess<NewsDetailDto>).copy(
                        data = (_uiState.value as UIState.UISuccess<NewsDetailDto>).data.copy(
                            log = (_uiState.value as UIState.UISuccess<NewsDetailDto>).data.log?.copy(
                                isBookmarked = !isBookmarked
                            )
                        )
                    )
                }
            }
        }
    }

    fun toggleShowHint() {
        _practiceData.value = _practiceData.value.copy(
            isShowHint = !_practiceData.value.isShowHint
        )
    }

    fun submitPractice() {
        //handle local data
        _practiceData.value = _practiceData.value.copy(isSubmitted = true)

        calculateScore()

        //handle remote data
        if (isAuth.value && uiState.value is UIState.UISuccess) {
            val id = (_uiState.value as UIState.UISuccess<NewsDetailDto>).data._id
            val questionLogs =
                _practiceData.value.selectedOptions.mapIndexed { index, answerIndex ->
                    NewsDetailLogQsaDto(
                        qsIndex = index,
                        chosenAnswer = answerIndex
                    )
                }
            viewModelScope.launch {
                answerNewsUseCase(id, _practiceData.value.score, questionLogs)
            }
        }
    }

    private fun calculateScore() {
        if (_uiState.value !is UIState.UISuccess) return
        var correctCount = 0
        val questions = (_uiState.value as UIState.UISuccess<NewsDetailDto>).data.questions
        _practiceData.value.selectedOptions.forEachIndexed { index, answerIndex ->
            if (questions[index].correctAnswerIndex == answerIndex) {
                correctCount++
            }
        }
        _practiceData.value = _practiceData.value.copy(score = correctCount)
    }

    fun retryPractice() {
        _practiceData.value = PracticeData()
    }

    fun nextQuestion() {
        _practiceData.value = _practiceData.value.copy(
            currentQuestionIndex = _practiceData.value.currentQuestionIndex + 1
        )
    }

    fun prevQuestion() {
        _practiceData.value = _practiceData.value.copy(
            currentQuestionIndex = _practiceData.value.currentQuestionIndex - 1
        )
    }

    suspend fun getNewsDetail(id: String) {
        _uiState.value = UIState.UILoading
        delay(500L)
        val newsDetail = getNewsDetailUseCase(id)
        if (newsDetail == null) {
            _uiState.value = UIState.UIError(
                UIErrorType.NotFoundError
            )
            return
        }
        retryPractice()
        _isShowTranslate.value = false
        _uiState.value = UIState.UISuccess(newsDetail)
    }
}