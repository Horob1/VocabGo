package com.acteam.vocago.presentation.screen.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.domain.usecase.LoginGoogleUseCase
import com.acteam.vocago.domain.usecase.LoginUseCase
import com.acteam.vocago.presentation.screen.auth.login.data.LoginFormState
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginGoogleUseCase: LoginGoogleUseCase
) : ViewModel() {
    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState = _loginFormState

    private val _loginUIState = MutableStateFlow<UIState<Unit>>(UIState.UISuccess(Unit))
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

    fun login(afterLoginSuccess: suspend () -> Unit) {
        if (!_loginFormState.value.isLoginButtonEnabled) {
            Log.d("login", "❌ Login button disabled → abort login")
            return
        }

        viewModelScope.launch {
            _loginUIState.value = UIState.UILoading
            val username = _loginFormState.value.username
            val password = _loginFormState.value.password

            try {
                loginUseCase(username = username, password = password)
                afterLoginSuccess()
                _loginUIState.value = UIState.UISuccess(Unit)

            } catch (e: Exception) {
                if (e is ApiException) {
                    _loginUIState.value = when (e.code) {
                        422 -> UIState.UIError(UIErrorType.UnexpectedEntityError)
                        400 -> UIState.UIError(UIErrorType.BadRequestError)
                        401 -> UIState.UIError(UIErrorType.UnauthorizedError)
                        404 -> UIState.UIError(UIErrorType.NotFoundError)
                        403 -> UIState.UIError(UIErrorType.ForbiddenError)
                        428 -> UIState.UIError(UIErrorType.PreconditionFailedError)
                        else -> UIState.UIError(UIErrorType.UnknownError)
                    }
                } else {
                    _loginUIState.value = UIState.UIError(UIErrorType.UnknownError)
                }
            }
        }
    }


    fun loginWithGoogle(token: String, afterLoginSuccess: suspend () -> Unit) {
        viewModelScope.launch {
            _loginUIState.value = UIState.UILoading
            try {
                val response = loginGoogleUseCase(token)
                Log.d("login response", response.toString())
                afterLoginSuccess()
                _loginUIState.value = UIState.UISuccess(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is ApiException) {
                    when (e.code) {
                        // truyền lên sai định dạng
                        422 -> _loginUIState.value =
                            UIState.UIError(UIErrorType.UnexpectedEntityError)
                        // Tài khoản chưa verify
                        400 -> _loginUIState.value =
                            UIState.UIError(UIErrorType.BadRequestError)
                        // Sai mật khẩu
                        401 -> _loginUIState.value =
                            UIState.UIError(UIErrorType.UnauthorizedError)
                        // Tài khoản or mật khẩu ko tồn tại :V
                        404 -> _loginUIState.value =
                            UIState.UIError(UIErrorType.NotFoundError)
                        // User bị ban rồi
                        403 -> _loginUIState.value = UIState.UIError(UIErrorType.ForbiddenError)
                        // Xac thuc 2 buoc
//                        428 -> _loginUIState.value =
//                            UIState.UIError(UIErrorType.PreconditionFailedError)

                        else -> _loginUIState.value = UIState.UIError(UIErrorType.UnknownError)
                    }
                } else {
                    _loginUIState.value = UIState.UIError(UIErrorType.UnknownError)
                }
            }
        }
    }

    fun clearUIState() {
        _loginUIState.value = UIState.UISuccess(Unit)
    }
}