package com.acteam.vocago.domain.repository

import kotlinx.coroutines.flow.Flow

interface ReadNovelSettingRepository {
    fun getSelectedTheme(): Flow<String>
    suspend fun setSelectedTheme(themeName: String)

    fun getFontSize(): Flow<Float>
    suspend fun setFontSize(size: Float)

    fun getFontName(): Flow<String>
    suspend fun setFontName(fontName: String)
}
