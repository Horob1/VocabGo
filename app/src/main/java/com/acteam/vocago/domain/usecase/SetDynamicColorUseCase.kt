package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.SystemConfigRepository

class SetDynamicColorUseCase(
    private val repository: SystemConfigRepository,
) {
    suspend operator fun invoke(dynamicColor: Boolean) {
        repository.setDynamicColor(dynamicColor)
    }
}