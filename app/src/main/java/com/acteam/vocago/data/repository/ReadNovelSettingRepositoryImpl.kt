package com.acteam.vocago.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.acteam.vocago.domain.repository.ReadNovelSettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "READ_NOVEL_SETTING"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class ReadNovelSettingRepositoryImpl(
    private val context: Context,
) : ReadNovelSettingRepository {

    companion object {
        private val THEME_KEY = stringPreferencesKey("NOVEL_THEME")
        private val FONT_SIZE_KEY = floatPreferencesKey("FONT_SIZE")

        private val FONT_NAME_KEY = stringPreferencesKey("FONT_NAME")
    }

    override fun getSelectedTheme(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: "Light" // default theme
        }
    }

    override suspend fun setSelectedTheme(themeName: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeName
        }
    }

    override fun getFontSize(): Flow<Float> {
        return context.dataStore.data.map { preferences ->
            preferences[FONT_SIZE_KEY] ?: 16f
        }
    }

    override suspend fun setFontSize(size: Float) {
        context.dataStore.edit { preferences ->
            preferences[FONT_SIZE_KEY] = size
        }
    }

    override fun getFontName(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[FONT_NAME_KEY] ?: "Roboto Serif"
        }
    }

    override suspend fun setFontName(fontName: String) {
        context.dataStore.edit { preferences ->
            preferences[FONT_NAME_KEY] = fontName
        }
    }
}