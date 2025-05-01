package com.acteam.vocago.domain.repository

interface WelcomeRepository {
    fun saveOnBoardingState(completed: Boolean)
    fun getOnBoardingState(): Boolean
}