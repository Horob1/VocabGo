package com.acteam.vocago.presentation.screen.choosevoca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.local.entity.VocaEntity
import com.acteam.vocago.domain.usecase.GetAllVocaListUseCase
import com.acteam.vocago.domain.usecase.SaveWordToVocaListUseCase
import io.ktor.utils.io.printStack
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChooseVocaViewModel(
    getAllVocaListUseCase: GetAllVocaListUseCase,
    private val saveWordToVocaListUseCase: SaveWordToVocaListUseCase,
) : ViewModel() {
    val vocaLists = getAllVocaListUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun saveWordToVocaList(word: VocaEntity, vocaListId: Int) {
        viewModelScope.launch {
            try {

                saveWordToVocaListUseCase(
                    word.copy(
                        listId = vocaListId
                    )
                )

            } catch (e: Exception) {
                e.printStack()
            }
        }
    }
}