package com.acteam.vocago.presentation.screen.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.local.entity.VocaEntity
import com.acteam.vocago.data.local.entity.VocaListWithVocas
import com.acteam.vocago.domain.usecase.GetVocaListDetailUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class LearnViewModel(
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
        .flatMapLatest { id -> getVocaListDetailUseCase(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    init {
        viewModelScope.launch {
            vocaListDetailData
                .filterNotNull()
                .collect { vocaListWithVocas ->
                    val vocas = vocaListWithVocas.vocas
                    val generatedQuestions = generateQuestions(vocas)
                    _questions.value = generatedQuestions
                }
        }
    }

    private fun generateQuestions(vocas: List<VocaEntity>): List<Question> {
        val questionList = mutableListOf<Question>()
        val shuffledWords = vocas.shuffled()

        for (word in shuffledWords) {
            if (word.word.isBlank() || word.meaning.isBlank()) continue

            val correctAnswer = word.meaning
            val incorrectAnswers = vocas
                .filter { it.id != word.id && it.meaning.isNotBlank() }
                .shuffled()
                .take(3)
                .map { it.meaning }

            if (incorrectAnswers.size < 3) continue

            val answers = (incorrectAnswers + correctAnswer).shuffled()
            val correctIndex = answers.indexOf(correctAnswer)

            questionList.add(
                Question(
                    word = word,
                    question = "What is the meaning of \"${word.word}\"?",
                    options = answers,
                    correctAnswerIndex = correctIndex,
                    type = 0
                )
            )
        }

        return questionList.shuffled()
    }
}

data class Question(
    val word: VocaEntity,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val type: Int = 0
)
