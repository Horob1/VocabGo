package com.acteam.vocago.presentation.screen.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.usecase.SaveOnBoardingStateUseCase
import kotlinx.coroutines.launch
class WelcomeViewModel(
    private val saveOnBoardingStateUseCase: SaveOnBoardingStateUseCase,
): ViewModel() {


    fun completeOnBoarding() {
        viewModelScope.launch {
            saveOnBoardingStateUseCase(completed = true)
        }
    }
}