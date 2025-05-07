package com.acteam.vocago.presentation.screen.common.data

sealed class UIErrorType {
    object NetworkError : UIErrorType()
    object ServerError : UIErrorType()
    object UnknownError : UIErrorType()
    object UnexpectedEntityError : UIErrorType()
    object NotFoundError : UIErrorType()
    object UnauthorizedError : UIErrorType()
    object ForbiddenError : UIErrorType()
    object BadRequestError : UIErrorType()
    object TooManyRequestsError : UIErrorType()

}