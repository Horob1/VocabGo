package com.acteam.vocago.presentation.screen.dictionary

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "search_history")

object SearchHistoryManager {
    private val KEY = stringPreferencesKey("recent_words")

    suspend fun saveSearch(context: Context, word: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[KEY]?.split(",")?.toMutableList() ?: mutableListOf()
            current.remove(word)
            current.add(0, word)
            preferences[KEY] = current.take(5).joinToString(",")
        }
    }

    suspend fun getRecentSearches(context: Context): List<String> {
        val preferences = context.dataStore.data.first()
        return preferences[KEY]?.split(",")?.take(5) ?: emptyList()
    }
}
