package com.acteam.vocago.presentation.screen.common.data

sealed class UIErrorType {
    data object NetworkError : UIErrorType()
    data object ServerError : UIErrorType()
    data object UnknownError : UIErrorType()
    data object UnexpectedEntityError : UIErrorType()
    data object NotFoundError : UIErrorType()
    data object UnauthorizedError : UIErrorType()
    data object ForbiddenError : UIErrorType()
    data object BadRequestError : UIErrorType()
    data object TooManyRequestsError : UIErrorType()
    data object PreconditionFailedError : UIErrorType()
    data object ConflictError : UIErrorType()
    data object LockedError : UIErrorType()
}