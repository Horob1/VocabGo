package com.acteam.vocago.domain.local

import kotlinx.coroutines.flow.StateFlow

interface AuthLocalDataSource {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearTokens()
    suspend fun getCredentialId(): String?
    suspend fun getTokens(): Pair<String?, String?>
    val isAuth: StateFlow<Boolean>
    suspend fun refreshTokens(accessToken: String, refreshToken: String)
    suspend fun saveCredential(credentialId: String, accessToken: String, refreshToken: String)


}