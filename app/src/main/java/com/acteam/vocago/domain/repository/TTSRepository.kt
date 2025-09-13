package com.acteam.vocago.domain.repository

import com.acteam.vocago.domain.model.TTSConfig
import kotlinx.coroutines.flow.Flow

interface TTSRepository {
    fun getConfiguration(): Flow<TTSConfig>

    suspend fun saveConfiguration(config: TTSConfig)
}