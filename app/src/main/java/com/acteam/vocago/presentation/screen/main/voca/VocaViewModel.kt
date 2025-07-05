package com.acteam.vocago.presentation.screen.main.voca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.local.entity.VocaListEntity
import com.acteam.vocago.domain.usecase.CreateVocaListUseCase
import com.acteam.vocago.domain.usecase.DeleteVocaListUseCase
import com.acteam.vocago.domain.usecase.GetAllVocaListUseCase
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import com.acteam.vocago.domain.usecase.SyncVocaFromServeUseCase
import com.acteam.vocago.domain.usecase.SyncVocaToServerUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VocaViewModel(
    getAllVocaListUseCase: GetAllVocaListUseCase,
    private val createVocaListUseCase: CreateVocaListUseCase,
    getLoginStateUseCase: GetLoginStateUseCase,
    private val deleteVocaListUseCase: DeleteVocaListUseCase,
    private val syncVocaToServerUseCase: SyncVocaToServerUseCase,
    private val syncVocaFromServerUseCase: SyncVocaFromServeUseCase,
) : ViewModel() {
    val isLogin = getLoginStateUseCase()
    val vocaLists = getAllVocaListUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun createVocaList(name: String) {
        viewModelScope.launch {
            try {
                createVocaListUseCase(
                    VocaListEntity(
                        title = name
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteVocaList(vocaList: VocaListEntity) {
        viewModelScope.launch {
            try {
                deleteVocaListUseCase(vocaList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun syncVocaToServer() {
        viewModelScope.launch {
            try {
                syncVocaToServerUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun syncVocaFromServer() {
        viewModelScope.launch {
            try {
                syncVocaFromServerUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}