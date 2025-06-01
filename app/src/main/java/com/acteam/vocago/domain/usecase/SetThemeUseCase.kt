package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.AppTheme
import com.acteam.vocago.domain.repository.SystemConfigRepository

class SetThemeUseCase(
    val repository: SystemConfigRepository,
) {
    suspend operator fun invoke(theme: AppTheme) {
        repository.setTheme(theme)
    }
}