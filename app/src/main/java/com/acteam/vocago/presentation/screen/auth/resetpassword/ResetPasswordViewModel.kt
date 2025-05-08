package com.acteam.vocago.presentation.screen.auth.resetpassword

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.domain.usecase.ResetPasswordUseCase
import com.acteam.vocago.presentation.screen.auth.common.data.OtpState
import com.acteam.vocago.presentation.screen.auth.resetpassword.data.ResetPasswordFormState
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
//    private val email: String,
    private val resetPasswordUseCase: ResetPasswordUseCase
):ViewModel() {

    private val _resetPasswordFormState = MutableStateFlow(ResetPasswordFormState())
    val resetPasswordFormState = _resetPasswordFormState

    private val _resetPasswordUIState = MutableStateFlow<UIState>(UIState.UISuccess)
    val resetPasswordUIState = _resetPasswordUIState

    private val _otpState = MutableStateFlow(OtpState())
    val otpState = _otpState

    @SuppressLint("DefaultLocale")
    val otpCountdown: StateFlow<String> = otpState.map { state ->
        val minutes = state.timeLeft / 60
        val seconds = state.timeLeft % 60
        String.format("%02d:%02d", minutes, seconds)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "05:00"
    )

    private var countdownJob: Job? = null

    fun startOtpCountdown(initialTime: Int = 300) {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            _otpState.value = _otpState.value.copy(timeLeft = initialTime)
            while (_otpState.value.timeLeft > 0) {
                delay(1000L)
                _otpState.value = _otpState.value.copy(timeLeft = _otpState.value.timeLeft - 1)
            }
        }
    }

    fun resetOtpCountdown(initialTime: Int = 300) {
        countdownJob?.cancel()
        _otpState.value = _otpState.value.copy(timeLeft = initialTime)
        startOtpCountdown(initialTime)
    }
    fun setOtpValue(otp: String) {
        _otpState.value = _otpState.value.copy(otp = otp)
    }


    fun setPassword(password: String) {
        _resetPasswordFormState.value = _resetPasswordFormState.value.copy(
            password = password,
            isResetPasswordButtonEnabled = password.isNotBlank()
        )
    }
    fun togglePasswordVisibility() {
        _resetPasswordFormState.value =
            _resetPasswordFormState.value.copy(isPasswordVisible = !_resetPasswordFormState.value.isPasswordVisible)
    }
    fun setConfirmPassword(confirmPassword: String) {
        _resetPasswordFormState.value = _resetPasswordFormState.value.copy(
            confirmPassword = confirmPassword,
            isResetPasswordButtonEnabled = confirmPassword.isNotBlank()
        )
    }
    fun toggleConfirmPasswordVisibility() {
        _resetPasswordFormState.value =
            _resetPasswordFormState.value.copy(isConfirmPasswordVisible = !_resetPasswordFormState.value.isConfirmPasswordVisible)
    }
    fun resetPassword(afterResetPasswordSuccess:() -> Unit) {
        if (!_resetPasswordFormState.value.isResetPasswordButtonEnabled) return
        viewModelScope.launch {
            _resetPasswordUIState.value = UIState.UILoading
            try {
                resetPasswordUseCase(
                    email = _resetPasswordFormState.value.email,
                    password = _resetPasswordFormState.value.password,
                    otp = _otpState.value.otp)
                afterResetPasswordSuccess()
                _resetPasswordUIState.value = UIState.UISuccess
            } catch (e: Exception) {
                if (e is ApiException) {
                    when (e.code) {
                        // truyền lên sai định dạng
                        422 -> _resetPasswordUIState.value =
                            UIState.UIError(UIErrorType.UnexpectedEntityError)
                        // Tài khoản chưa verify
                        400 -> _resetPasswordUIState.value =
                            UIState.UIError(UIErrorType.BadRequestError)
                        // User bị ban rồi
                        403 -> _resetPasswordUIState.value = UIState.UIError(UIErrorType.ForbiddenError)
                        // Email ko tồn tại :V
                        404 -> _resetPasswordUIState.value =
                            UIState.UIError(UIErrorType.NotFoundError)
                    }
                }
                else
                    _resetPasswordUIState.value = UIState.UIError(UIErrorType.UnknownError)
            }
        }
    }
    fun clearUIState() {
        _resetPasswordUIState.value = UIState.UISuccess
    }
}