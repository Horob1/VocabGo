package com.acteam.vocago.presentation.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.model.AppLanguage
import com.acteam.vocago.domain.model.AppTheme
import com.acteam.vocago.domain.usecase.ChooseLanguageUserCase
import com.acteam.vocago.domain.usecase.GetDynamicColorUseCase
import com.acteam.vocago.domain.usecase.GetLanguageUseCase
import com.acteam.vocago.domain.usecase.GetThemeUseCase
import com.acteam.vocago.domain.usecase.SetDynamicColorUseCase
import com.acteam.vocago.domain.usecase.SetThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    getDynamicColorUseCase: GetDynamicColorUseCase,
    getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setDynamicColorUseCase: SetDynamicColorUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val setLanguageUseCase: ChooseLanguageUserCase,
) : ViewModel() {
    val dynamicColor = getDynamicColorUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val theme = getThemeUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppTheme.SYSTEM
    )

    private val _appLanguage = MutableStateFlow<AppLanguage>(AppLanguage.System)
    val appLanguage = _appLanguage

    init {
        initAppLanguage()
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
        setLanguageUseCase(_appLanguage.value)
    }

    fun setDynamicColor(dynamicColor: Boolean) {
        viewModelScope.launch {
            setDynamicColorUseCase(dynamicColor)
        }
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            setThemeUseCase(theme)
        }
    }
}