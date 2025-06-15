package com.acteam.vocago.presentation.screen.user.usernavigator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.domain.usecase.LogoutUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserNavigatorViewModel(
    getLocalUserProfileUseCase: GetLocalUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Unit>>(UIState.UISuccess(Unit))
    val uiState = _uiState

    val userState = getLocalUserProfileUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = UIState.UILoading
            try {
                logoutUseCase()
                onLogoutSuccess()
                delay(500)
                _uiState.value = UIState.UISuccess(Unit)
            } catch (_: Exception) {
                _uiState.value = UIState.UIError(UIErrorType.UnknownError)
            }
        }
    }

    fun clearUiState() {
        _uiState.value = UIState.UISuccess(Unit)
    }
}