package com.acteam.vocago.presentation.screen.auth.verifyemail

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.presentation.screen.auth.common.data.OtpState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VerifyEmailViewModel : ViewModel() {
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


}