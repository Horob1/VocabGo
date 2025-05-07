package com.acteam.vocago.presentation.screen.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.domain.usecase.ForgotPasswordUseCase
import com.acteam.vocago.presentation.screen.auth.forgotpassword.data.ForgotPasswordState
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {
    private val _forgotPasswordFormState = MutableStateFlow(ForgotPasswordState())
    val forgotPasswordFormState = _forgotPasswordFormState

    private val _forgotPasswordUIState = MutableStateFlow<UIState>(UIState.UISuccess)
    val forgotPasswordUIState = _forgotPasswordUIState

    fun setEmail(email: String) {
        _forgotPasswordFormState.value = _forgotPasswordFormState.value.copy(
            email = email,
            isForgotPasswordButtonEnabled = email.isNotBlank()
        )
    }

    fun forgotPassword(afterForgotPasswordSuccess: () -> Unit) {
        if (!_forgotPasswordFormState.value.isForgotPasswordButtonEnabled) return
        viewModelScope.launch {
            _forgotPasswordUIState.value = UIState.UILoading
            try {
                forgotPasswordUseCase(
                    email = _forgotPasswordFormState.value.email
                )
                afterForgotPasswordSuccess()
                _forgotPasswordUIState.value = UIState.UISuccess
            } catch (e: Exception) {
                if (e is ApiException) {
                    _forgotPasswordUIState.value = UIState.UIError(
                        when (e.code) {
                            404 -> UIErrorType.NotFoundError
                            403 -> UIErrorType.ForbiddenError
                            429 -> UIErrorType.TooManyRequestsError
                            else -> UIErrorType.UnknownError
                        }
                    )
                } else {
                    _forgotPasswordUIState.value = UIState.UIError(UIErrorType.UnknownError)
                }
            }
        }

    }

    fun clearUIState() {
        _forgotPasswordUIState.value = UIState.UISuccess
    }
}