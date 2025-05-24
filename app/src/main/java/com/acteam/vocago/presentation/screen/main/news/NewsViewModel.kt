package com.acteam.vocago.presentation.screen.main.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import com.acteam.vocago.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    getLoginStateUseCase: GetLoginStateUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
) : ViewModel() {
    val loginState = getLoginStateUseCase()
    private val _userState = MutableStateFlow<UserDto?>(null)
    val userState: MutableStateFlow<UserDto?> = _userState

    suspend fun loadProfile(){
            if (loginState.value)
                getUserProfileUseCase().collect {
                    _userState.value = it
                }
    }
}