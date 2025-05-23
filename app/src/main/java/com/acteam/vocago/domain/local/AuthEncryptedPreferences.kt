package com.acteam.vocago.domain.local

import kotlinx.coroutines.flow.Flow

interface AuthEncryptedPreferences {
    fun clearCredentials()
    fun saveCredentials(accessToken: String, refreshToken: String, credentialId: String)
    fun refreshToken(refreshToken: String, accessToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun getCredentialId(): String?
    val isAuth: Flow<Boolean>

}