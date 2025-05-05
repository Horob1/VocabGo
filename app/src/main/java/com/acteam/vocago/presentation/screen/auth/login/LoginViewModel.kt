package com.acteam.vocago.presentation.screen.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.presentation.screen.auth.login.data.LoginFormState
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState = _loginFormState

    private val _loginUIState = MutableStateFlow<UIState>(UIState.UISuccess)
    val loginUIState = _loginUIState

    fun setUsername(username: String) {
        _loginFormState.value = _loginFormState.value.copy(
            username = username,
            isLoginButtonEnabled = username.isNotBlank()
                    && _loginFormState.value.password.isNotBlank()
        )
    }

    fun setPassword(password: String) {
        _loginFormState.value = _loginFormState.value.copy(
            password = password,
            isLoginButtonEnabled = password.isNotBlank(),
            username = _loginFormState.value.username
        )
    }

    fun togglePasswordVisibility() {
        _loginFormState.value =
            _loginFormState.value.copy(passwordVisible = !_loginFormState.value.passwordVisible)
    }

    fun login() {
        viewModelScope.launch {
            _loginUIState.value = UIState.UILoading
            delay(2000)
            _loginUIState.value = UIState.UISuccess
        }
    }

}