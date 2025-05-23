package com.acteam.vocago.presentation.screen.main.news

import androidx.lifecycle.ViewModel
import com.acteam.vocago.domain.usecase.GetLoginStateUseCase

class NewsViewModel(
    getLoginStateUseCase: GetLoginStateUseCase,
) : ViewModel() {
    val loginState = getLoginStateUseCase()
}