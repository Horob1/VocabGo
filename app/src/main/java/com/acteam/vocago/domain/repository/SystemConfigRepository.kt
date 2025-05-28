package com.acteam.vocago.domain.repository

import com.acteam.vocago.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface SystemConfigRepository {
    fun getTheme(): Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
    fun getDynamicColor(): Flow<Boolean>
    suspend fun setDynamicColor(dynamicColor: Boolean)
}