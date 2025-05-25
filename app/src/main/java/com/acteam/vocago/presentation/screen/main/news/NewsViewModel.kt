package com.acteam.vocago.presentation.screen.main.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.model.NewsLevel
import com.acteam.vocago.domain.usecase.GetChosenNewsCategoriesUseCase
import com.acteam.vocago.domain.usecase.GetChosenNewsLevelUseCase
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import com.acteam.vocago.domain.usecase.SyncProfileUseCase
import com.acteam.vocago.domain.usecase.UpdateChosenCategoriesUseCase
import com.acteam.vocago.domain.usecase.UpdateChosenNewsLevelUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsViewModel(
    getLoginStateUseCase: GetLoginStateUseCase,
    getLocalUserProfileUseCase: GetLocalUserProfileUseCase,
    private val syncProfileUseCase: SyncProfileUseCase,
    private val updateChosenCategoriesUseCase: UpdateChosenCategoriesUseCase,
    getChosenNewsCategoriesUseCase: GetChosenNewsCategoriesUseCase,
    getChosenNewsLevelUseCase: GetChosenNewsLevelUseCase,
    private val updateChosenNewsLevelUseCase: UpdateChosenNewsLevelUseCase,
) : ViewModel() {
    val loginState = getLoginStateUseCase()
    val chosenCategories = getChosenNewsCategoriesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )
    val chosenLevel = getChosenNewsLevelUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NewsLevel.ALL
    )
    val userState = getLocalUserProfileUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    suspend fun syncProfile() {
        if (loginState.value) {
            syncProfileUseCase()
        }
    }

    fun updateChosenCategories(categories: List<String>) {
        viewModelScope.launch {
            updateChosenCategoriesUseCase(categories)
        }
    }

    fun updateChosenLevel(level: NewsLevel) {
        viewModelScope.launch {
            updateChosenNewsLevelUseCase(level)
        }
    }
}