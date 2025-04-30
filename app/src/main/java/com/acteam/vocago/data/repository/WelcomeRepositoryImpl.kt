package com.acteam.vocago.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.acteam.vocago.domain.repository.WelcomeRepository
import com.acteam.vocago.presentation.navigation.NavScreen

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "on_boarding_pref")

class WelcomeRepositoryImpl(context: Context): WelcomeRepository {

    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
    }

    private val dataStore = context.dataStore

    override suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onBoardingKey] = completed
        }
    }

    override suspend fun getOnBoardingState(): Boolean {
        return try {
            dataStore.data
                .map { preferences -> preferences[PreferencesKey.onBoardingKey] ?: false }
                .first()
        } catch (e: IOException) {
            false
        }
    }

}
