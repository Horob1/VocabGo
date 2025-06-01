package com.acteam.vocago.presentation.screen.common.data

sealed class UIState<out T> {
    data object UILoading : UIState<Nothing>()
    data class UISuccess<T>(val data: T) : UIState<T>()
    data class UIError(val errorType: UIErrorType) : UIState<Nothing>()
}

