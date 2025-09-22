package com.acteam.vocago.domain.local

import kotlinx.coroutines.flow.StateFlow

interface AuthLocalDataSource {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun clearTokens()
    fun getCredentialId(): String?
    fun getTokens(): Pair<String?, String?>
    val isAuth: StateFlow<Boolean>

    val credentialIdFlow: StateFlow<String?>
    fun refreshTokens(accessToken: String, refreshToken: String)
    fun saveCredential(credentialId: String, accessToken: String, refreshToken: String)
}