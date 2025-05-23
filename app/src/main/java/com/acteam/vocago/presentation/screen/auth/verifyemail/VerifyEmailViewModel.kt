package com.acteam.vocago.presentation.screen.auth.verifyemail

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.domain.usecase.ResendVerifyEmailUseCase
import com.acteam.vocago.domain.usecase.VerifyEmailUseCase
import com.acteam.vocago.presentation.screen.auth.common.data.OtpState
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

class VerifyEmailViewModel(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val resendVerifyEmailUseCase: ResendVerifyEmailUseCase
) : ViewModel() {
    private val _verifyEmailUIState = MutableStateFlow<UIState>(UIState.UISuccess)
    val verifyEmailUIState = _verifyEmailUIState

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

    fun setOtpValue(otp: String) {
        _otpState.value = _otpState.value.copy(otp = otp)
    }

    fun verifyEmail(email: String, afterVerifySuccess: () -> Unit) {
        viewModelScope.launch {
            _verifyEmailUIState.value = UIState.UILoading
            try {
                verifyEmailUseCase(email, _otpState.value.otp)
                afterVerifySuccess()
                _verifyEmailUIState.value = UIState.UISuccess
            } catch (e: Exception) {
                if (e is ApiException) {
                    when (e.code) {
                        404 -> _verifyEmailUIState.value =
                            UIState.UIError(UIErrorType.NotFoundError)

                        403 -> _verifyEmailUIState.value =
                            UIState.UIError(UIErrorType.ForbiddenError)

                        400 -> _verifyEmailUIState.value =
                            UIState.UIError(UIErrorType.BadRequestError)

                        else -> _verifyEmailUIState.value =
                            UIState.UIError(UIErrorType.UnknownError)
                    }
                } else {
                    _verifyEmailUIState.value = UIState.UIError(UIErrorType.UnknownError)
                }

            }
        }
    }

    fun resendVerifyEmail(email: String) {
        viewModelScope.launch {
            _verifyEmailUIState.value = UIState.UILoading

            try {
                resendVerifyEmailUseCase(email)
                _verifyEmailUIState.value = UIState.UISuccess
                _otpState.value = _otpState.value.copy(timeLeft = 300)
                startOtpCountdown(_otpState.value.timeLeft)
            } catch (e: Exception) {
                if (e is ApiException) {
                    when (e.code) {
                        404 -> _verifyEmailUIState.value =
                            UIState.UIError(UIErrorType.NotFoundError)

                        403 -> _verifyEmailUIState.value =
                            UIState.UIError(UIErrorType.ForbiddenError)

                        400 -> _verifyEmailUIState.value =
                            UIState.UIError(UIErrorType.BadRequestError)

                        429 -> _verifyEmailUIState.value =
                            UIState.UIError(UIErrorType.TooManyRequestsError)

                        else -> _verifyEmailUIState.value =
                            UIState.UIError(UIErrorType.UnknownError)
                    }
                }
            }
        }
    }

    fun clearUIState() {
        _verifyEmailUIState.value = UIState.UISuccess
    }

}