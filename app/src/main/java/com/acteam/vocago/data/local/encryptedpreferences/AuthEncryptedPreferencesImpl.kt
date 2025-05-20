@file:Suppress("DEPRECATION")

package com.acteam.vocago.data.local.encryptedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.acteam.vocago.domain.local.AuthEncryptedPreferences


class AuthEncryptedPreferencesImpl(context: Context) : AuthEncryptedPreferences {
    companion object {
        private const val PREF_NAME = "auth_encrypted_prefs"
        private const val KEY_ACCESS_TOKEN = "key_access_token"
        private const val KEY_REFRESH_TOKEN = "key_refresh_token"
        private const val KEY_CREDENTIAL_ID = "key_credential_id"
    }

    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREF_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun clearCredentials() {
        prefs.edit { clear() }
    }

    override fun saveCredentials(
        accessToken: String,
        refreshToken: String,
        credentialId: String,
    ) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putString(KEY_CREDENTIAL_ID, credentialId)
        }
    }

    override fun refreshToken(refreshToken: String, accessToken: String) {
        prefs.edit {
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putString(KEY_ACCESS_TOKEN, accessToken)
        }
    }

    override fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    override fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    override fun getCredentialId(): String? {
        return prefs.getString(KEY_CREDENTIAL_ID, null)
    }
}