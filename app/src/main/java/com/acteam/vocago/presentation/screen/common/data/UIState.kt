package com.acteam.vocago.presentation.screen.common.data

sealed class UIState {
    data object UILoading : UIState()
    data object UISuccess : UIState()
    data class UIError(val errorType: UIErrorType) : UIState()
}