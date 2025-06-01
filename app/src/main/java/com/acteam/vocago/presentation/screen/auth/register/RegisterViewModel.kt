package com.acteam.vocago.presentation.screen.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.domain.usecase.RegisterUseCase
import com.acteam.vocago.presentation.screen.auth.register.data.RegisterFormState
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    private val _registerUIState = MutableStateFlow<UIState<Unit>>(UIState.UISuccess(Unit))
    val registerUIState = _registerUIState

    private val _registerFormState = MutableStateFlow(RegisterFormState())
    val registerFormState = _registerFormState

    fun setUsername(username: String) {
        _registerFormState.value = _registerFormState.value.copy(
            username = username
        )
    }

    fun setEmail(email: String) {
        _registerFormState.value = _registerFormState.value.copy(
            email = email
        )
    }

    fun setPassword(password: String) {
        _registerFormState.value = _registerFormState.value.copy(
            password = password
        )
    }

    fun setConfirmPassword(confirmPassword: String) {
        _registerFormState.value = _registerFormState.value.copy(
            confirmPassword = confirmPassword
        )
    }

    fun setFirstName(firstName: String) {
        _registerFormState.value = _registerFormState.value.copy(
            firstName = firstName
        )
    }

    fun setLastName(lastName: String) {
        _registerFormState.value = _registerFormState.value.copy(
            lastName = lastName
        )
    }

    fun setPhone(phone: String) {
        _registerFormState.value = _registerFormState.value.copy(
            phone = phone
        )
    }

    fun setAddress(address: String) {
        _registerFormState.value = _registerFormState.value.copy(
            address = address
        )
    }

    fun setDateOfBirth(dateOfBirth: String) {
        _registerFormState.value = _registerFormState.value.copy(
            dateOfBirth = dateOfBirth
        )
    }

    fun setGender(gender: String) {
        _registerFormState.value = _registerFormState.value.copy(
            gender = gender
        )
    }

    fun register(afterRegisterSuccess: () -> Unit) {
        viewModelScope.launch {
            _registerUIState.value = UIState.UILoading
            try {
                registerUseCase(
                    username = _registerFormState.value.username,
                    email = _registerFormState.value.email,
                    password = _registerFormState.value.password,
                    firstName = _registerFormState.value.firstName,
                    lastName = _registerFormState.value.lastName,
                    phoneNumber = _registerFormState.value.phone,
                    address = _registerFormState.value.address,
                    dob = _registerFormState.value.dateOfBirth,
                    gender = _registerFormState.value.gender,
                )
                afterRegisterSuccess()
                _registerUIState.value = UIState.UISuccess(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is ApiException) {
                    when (e.code) {
                        409 -> _registerUIState.value = UIState.UIError(UIErrorType.ConflictError)
                        500 -> _registerUIState.value =
                            UIState.UIError(UIErrorType.ServerError)

                        else -> _registerUIState.value = UIState.UIError(UIErrorType.UnknownError)
                    }
                } else _registerUIState.value = UIState.UIError(UIErrorType.UnknownError)
            }
        }
    }

    fun clearUIState() {
        _registerUIState.value = UIState.UISuccess(Unit)
    }
}