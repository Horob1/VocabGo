package com.acteam.vocago.presentation.screen.common.data

sealed class UIState {
    object UILoading : UIState()
    object UISuccess : UIState()
    data class UIError(val errorType: UIErrorType) : UIState()
}