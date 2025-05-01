package com.acteam.vocago.presentation.screen.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.model.AppLanguage
import com.acteam.vocago.domain.usecase.ChooseLanguageUserCase
import com.acteam.vocago.domain.usecase.GetLanguageUseCase
import com.acteam.vocago.domain.usecase.SaveOnBoardingStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val saveOnBoardingStateUseCase: SaveOnBoardingStateUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val saveLanguageUseCase: ChooseLanguageUserCase
) : ViewModel() {
    private val _appLanguage = MutableStateFlow<AppLanguage>(AppLanguage.System)
    val appLanguage = _appLanguage

    init {
        initAppLanguage()
    }

    fun completeOnBoarding() {
        viewModelScope.launch {
            saveOnBoardingStateUseCase(completed = true)
        }
    }

    private fun initAppLanguage() {
        val lang = getLanguageUseCase()
        when (lang) {
            AppLanguage.English.languageCode -> _appLanguage.value = AppLanguage.English
            AppLanguage.Vietnamese.languageCode -> _appLanguage.value = AppLanguage.Vietnamese
            else -> _appLanguage.value = AppLanguage.System
        }
    }

    fun changeLanguage(language: AppLanguage) {
        _appLanguage.value = language
    }

    fun saveLanguage() {
        saveLanguageUseCase(_appLanguage.value)
    }
}