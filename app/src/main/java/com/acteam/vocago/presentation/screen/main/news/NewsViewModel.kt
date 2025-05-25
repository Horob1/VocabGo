package com.acteam.vocago.presentation.screen.main.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import com.acteam.vocago.domain.usecase.SyncProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class NewsViewModel(
    getLoginStateUseCase: GetLoginStateUseCase,
    getLocalUserProfileUseCase: GetLocalUserProfileUseCase,
    private val syncProfileUseCase: SyncProfileUseCase,
) : ViewModel() {
    val loginState = getLoginStateUseCase()
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
}