package com.acteam.vocago.domain.repository

interface WelcomeRepository {
    suspend fun saveOnBoardingState(completed: Boolean)
    suspend fun getOnBoardingState(): Boolean
}