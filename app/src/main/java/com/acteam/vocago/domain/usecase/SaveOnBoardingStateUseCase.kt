package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.WelcomeRepository

class SaveOnBoardingStateUseCase(
    private val welcomeRepository: WelcomeRepository
) {
    suspend operator fun invoke(completed: Boolean): Result<Unit> {
        return try {
            welcomeRepository.saveOnBoardingState(completed)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}