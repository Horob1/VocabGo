package com.acteam.vocago.presentation.screen.common.data

sealed class UIErrorType {
    object NetworkError : UIErrorType()
    object ServerError : UIErrorType()
    object UnknownError : UIErrorType()
}