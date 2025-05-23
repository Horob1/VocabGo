package com.acteam.vocago.presentation.screen.auth.verify2fa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.domain.usecase.VerifyTwoFAUseCase
import com.acteam.vocago.presentation.screen.auth.common.data.OtpState
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class VerifyTwoFAViewModel(
    private val verifyTwoFAUseCase: VerifyTwoFAUseCase,
) : ViewModel() {
    private val _otpState = MutableStateFlow(OtpState())
    val otpState = _otpState

    private val _verifyTwoFAUIState = MutableStateFlow<UIState>(UIState.UISuccess)
    val verifyTwoFAUIState = _verifyTwoFAUIState

    fun setOtpValue(otp: String) {
        _otpState.value = _otpState.value.copy(otp = otp)
    }

    fun verifyTwoFA(email: String, afterVerifySuccess: () -> Unit) {
        viewModelScope.launch {
            _verifyTwoFAUIState.value = UIState.UILoading
            try {
                verifyTwoFAUseCase(email, _otpState.value.otp)
                afterVerifySuccess()
                _verifyTwoFAUIState.value = UIState.UISuccess
            } catch (e: Exception) {
                if (e is ApiException) {
                    when (e.code) {
                        404 -> _verifyTwoFAUIState.value =
                            UIState.UIError(UIErrorType.NotFoundError)

                        400 -> _verifyTwoFAUIState.value =
                            UIState.UIError(UIErrorType.BadRequestError)

                        500 -> _verifyTwoFAUIState.value = UIState.UIError(UIErrorType.ServerError)
                        else -> _verifyTwoFAUIState.value =
                            UIState.UIError(UIErrorType.UnknownError)
                    }
                } else {
                    _verifyTwoFAUIState.value = UIState.UIError(UIErrorType.UnknownError)
                }
            }
        }
    }

    fun clearUIState() {
        _verifyTwoFAUIState.value = UIState.UISuccess
    }

}