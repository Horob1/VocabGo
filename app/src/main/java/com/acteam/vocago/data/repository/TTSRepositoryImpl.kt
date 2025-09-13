package com.acteam.vocago.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.acteam.vocago.domain.model.TTSConfig
import com.acteam.vocago.domain.repository.TTSRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.ttsDatastore: DataStore<Preferences> by preferencesDataStore(name = TTSRepositoryImpl.NAME)

class TTSRepositoryImpl(context: Context) : TTSRepository {
    companion object {
        const val NAME = "tts_config"

    }

    private val dataStore = context.ttsDatastore

    private object PreferencesKey {
        val speedKey = floatPreferencesKey(name = "speed")
        val pitchKey = floatPreferencesKey(name = "pitch")
        val voiceKey = stringPreferencesKey(name = "voice")
    }

    override fun getConfiguration(): Flow<TTSConfig> {
        return dataStore.data.map {
            it[PreferencesKey.voiceKey]
            TTSConfig(
                speed = it[PreferencesKey.speedKey] ?: 1f,
                pitch = it[PreferencesKey.pitchKey] ?: 1f,
                voice = it[PreferencesKey.voiceKey] ?: ""
            )
        }
    }

    override suspend fun saveConfiguration(config: TTSConfig) {
        dataStore.edit {
            it[PreferencesKey.speedKey] = config.speed
            it[PreferencesKey.pitchKey] = config.pitch
            it[PreferencesKey.voiceKey] = config.voice
        }
    }
}