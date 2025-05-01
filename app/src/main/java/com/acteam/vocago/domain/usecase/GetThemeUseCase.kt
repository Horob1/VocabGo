package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.AppTheme
import com.acteam.vocago.domain.repository.SystemConfigRepository
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(
    private val repository: SystemConfigRepository,
) {
    operator fun invoke(): Flow<AppTheme> {
        return repository.getTheme()
    }
}