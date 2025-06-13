package com.acteam.vocago.presentation.screen.user.usernavigator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class UserNavigatorViewModel(
    getLocalUserProfileUseCase: GetLocalUserProfileUseCase,
) : ViewModel() {
    val userState = getLocalUserProfileUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )
}