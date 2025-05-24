package com.acteam.vocago.data.local


import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.acteam.vocago.domain.local.AuthLocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthLocalDataSourceImpl(context: Context) : AuthLocalDataSource {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(AUTH_PREF_NAME, Context.MODE_PRIVATE)

    private val mutex = Mutex()
    private var accessToken: String? = null
    private var refreshToken: String? = null
    private var credentialId: String? = null
    private val _isAuth = MutableStateFlow(false)
    override val isAuth: StateFlow<Boolean> = _isAuth

    companion object {
        private const val AUTH_PREF_NAME = "AUTH_PREF_NAME"
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
        private const val CREDENTIAL_ID_KEY = "CREDENTIAL_ID_KEY"
    }

    init {
        accessToken = prefs.getString(ACCESS_TOKEN_KEY, null)
        refreshToken = prefs.getString(REFRESH_TOKEN_KEY, null)
        credentialId = prefs.getString(CREDENTIAL_ID_KEY, null)
        _isAuth.value = accessToken != null
    }

    override suspend fun getAccessToken(): String? = mutex.withLock {
        accessToken ?: prefs.getString(ACCESS_TOKEN_KEY, null)
    }

    override suspend fun getRefreshToken(): String? = mutex.withLock {
        refreshToken ?: prefs.getString(REFRESH_TOKEN_KEY, null)
    }

    override suspend fun clearTokens() = mutex.withLock {
        accessToken = null
        refreshToken = null
        credentialId = null
        _isAuth.value = false
        prefs.edit {
            remove(ACCESS_TOKEN_KEY)
            remove(REFRESH_TOKEN_KEY)
            remove(CREDENTIAL_ID_KEY)
        }
    }

    override suspend fun getCredentialId(): String? = mutex.withLock {
        credentialId ?: prefs.getString(CREDENTIAL_ID_KEY, null)
    }

    override suspend fun getTokens(): Pair<String?, String?> = mutex.withLock {
        Pair(
            accessToken ?: prefs.getString(ACCESS_TOKEN_KEY, null),
            refreshToken ?: prefs.getString(REFRESH_TOKEN_KEY, null)
        )
    }


    override suspend fun refreshTokens(accessToken: String, refreshToken: String) = mutex.withLock {
        this.refreshToken = refreshToken
        this.accessToken = accessToken
        prefs.edit {
            putString(ACCESS_TOKEN_KEY, accessToken)
            putString(REFRESH_TOKEN_KEY, refreshToken)
        }
    }

    override suspend fun saveCredential(
        credentialId: String,
        accessToken: String,
        refreshToken: String,
    ) = mutex.withLock {
        this.credentialId = credentialId
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        _isAuth.value = true
        prefs.edit {
            putString(CREDENTIAL_ID_KEY, credentialId)
            putString(ACCESS_TOKEN_KEY, accessToken)
            putString(REFRESH_TOKEN_KEY, refreshToken)
        }
    }

}