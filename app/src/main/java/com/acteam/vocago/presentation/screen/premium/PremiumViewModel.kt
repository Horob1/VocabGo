package com.acteam.vocago.presentation.screen.premium

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.PaymentRequest
import com.acteam.vocago.data.model.PaymentResponse
import com.acteam.vocago.domain.usecase.CreatePaymentUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PremiumViewModel(
    private val createPaymentUseCase: CreatePaymentUseCase
) : ViewModel() {

    private val _paymentState = MutableStateFlow<UIState<PaymentResponse>?>(null)
    val paymentState = _paymentState.asStateFlow()


    fun createPayment(request: PaymentRequest) {
        viewModelScope.launch {
            _paymentState.value = UIState.UILoading
            try {
                val response = createPaymentUseCase(request)
                _paymentState.value = UIState.UISuccess(response)
            } catch (e: ApiException) {
                e.printStackTrace()
                _paymentState.value = UIState.UIError(UIErrorType.BadRequestError)
            } catch (e: Exception) {
                e.printStackTrace()
                _paymentState.value = UIState.UIError(UIErrorType.UnknownError)
            }
        }
    }
}
