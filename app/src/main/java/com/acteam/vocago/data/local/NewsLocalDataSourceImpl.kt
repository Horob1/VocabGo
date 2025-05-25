package com.acteam.vocago.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.acteam.vocago.domain.local.NewsLocalDataSource
import com.acteam.vocago.domain.model.NewsLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.newsDataStore by preferencesDataStore(name = NewsLocalDataSourceImpl.PREFS_NAME)

class NewsLocalDataSourceImpl(context: Context) : NewsLocalDataSource {

    private val dataStore = context.newsDataStore

    private object PreferencesKey {
        val chosenCategory = stringSetPreferencesKey(KEY_CHOSEN_CATEGORIES)
        val chosenLevel = stringPreferencesKey(KEY_CHOSEN_LEVEL)
    }

    companion object {
        const val PREFS_NAME = "NEWS_PREFS"
        private const val KEY_CHOSEN_CATEGORIES = "CHOSEN_CATEGORIES"
        private const val KEY_CHOSEN_LEVEL = "CHOSEN_LEVEL"
    }

    override fun getChosenCategories(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.chosenCategory]?.toList() ?: emptyList()
        }
    }

    override suspend fun updateChosenCategories(categories: List<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.chosenCategory] = categories.toSet()
        }
    }

    override fun getChosenLevel(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.chosenLevel] ?: NewsLevel.ALL.value
        }
    }

    override suspend fun updateChosenLevel(level: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.chosenLevel] = level
        }
    }

}