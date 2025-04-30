package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.WelcomeRepository
import com.acteam.vocago.presentation.navigation.NavScreen
import kotlinx.coroutines.flow.Flow

class GetStartScreenUseCase(
    private val welcomeRepository: WelcomeRepository
) {
    suspend operator fun invoke(): NavScreen {
        return if (welcomeRepository.getOnBoardingState()) {
            NavScreen.HomeNavScreen
        } else {
            NavScreen.WelcomeNavScreen
        }
    }

}