package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.repository.SystemConfigRepository
import kotlinx.coroutines.flow.Flow

class GetDynamicColorUseCase(
    private val systemConfigRepository: SystemConfigRepository,
) {
    operator fun invoke(): Flow<Boolean> {
        return systemConfigRepository.getDynamicColor()
    }

}