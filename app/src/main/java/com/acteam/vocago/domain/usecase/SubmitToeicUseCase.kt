package com.acteam.vocago.domain.usecase


import com.acteam.vocago.data.model.SubmitRequest
import com.acteam.vocago.data.model.TestResultDto
import com.acteam.vocago.domain.remote.ToeicRemoteDataSource

class SubmitToeicUseCase(
    private val remoteDataSource: ToeicRemoteDataSource
) {
    suspend operator fun invoke(request: SubmitRequest): Result<TestResultDto> {
        return remoteDataSource.submitToeicTest(request)
    }
}
