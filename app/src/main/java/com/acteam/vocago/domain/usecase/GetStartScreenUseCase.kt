package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.WelcomeRepository
import com.acteam.vocago.presentation.navigation.NavScreen

class GetStartScreenUseCase(
    private val welcomeRepository: WelcomeRepository,
) {
    operator fun invoke(): NavScreen {
        return if (welcomeRepository.getOnBoardingState()) {
            NavScreen.MainNavScreen
        } else {
            NavScreen.WelcomeNavScreen
        }
    }

}