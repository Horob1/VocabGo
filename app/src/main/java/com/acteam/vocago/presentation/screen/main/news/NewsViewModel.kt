package com.acteam.vocago.presentation.screen.main.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase
import com.acteam.vocago.domain.usecase.GetUserProfileUseCase
import com.acteam.vocago.presentation.screen.common.data.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    getLoginStateUseCase: GetLoginStateUseCase,
    getUserProfileUseCase: GetUserProfileUseCase,
) : ViewModel() {
    val loginState = getLoginStateUseCase()
    private val _userState = MutableStateFlow<Resource<UserDto>>(Resource.Loading)
    val userState: MutableStateFlow<Resource<UserDto>> = _userState

    init {
        viewModelScope.launch {
            getUserProfileUseCase().collect {
                _userState.value = it
            }
        }
    }
}