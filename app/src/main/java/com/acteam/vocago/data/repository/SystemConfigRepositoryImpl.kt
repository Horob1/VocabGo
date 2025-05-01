package com.acteam.vocago.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.acteam.vocago.domain.model.AppTheme
import com.acteam.vocago.domain.repository.SystemConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.systemConfigDataStore: DataStore<Preferences> by preferencesDataStore(name = SystemConfigRepositoryImpl.SYSTEM_CONFIG_DATA_STORE_NAME)

class SystemConfigRepositoryImpl(context: Context) : SystemConfigRepository {
    private object PreferencesKey {
        val themeKey = stringPreferencesKey(name = THEME_KEY)
    }

    val dataStore = context.systemConfigDataStore

    override fun getTheme(): Flow<AppTheme> {
        return dataStore.data.map {
            when (it[PreferencesKey.themeKey]) {
                AppTheme.LIGHT.name -> AppTheme.LIGHT
                AppTheme.DARK.name -> AppTheme.DARK
                AppTheme.SYSTEM.name -> AppTheme.SYSTEM
                else -> AppTheme.SYSTEM
            }
        }
    }

    companion object {
        const val THEME_KEY = "THEME"
        const val SYSTEM_CONFIG_DATA_STORE_NAME = "APP_CONFIG_DATA_STORE_NAME"
    }
}