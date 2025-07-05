package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.TestResultListDto
import com.acteam.vocago.domain.remote.ToeicRemoteDataSource

class GetTestResultDetailUseCase(
    private val toeicRemoteDataSource: ToeicRemoteDataSource
) {
    suspend operator fun invoke(id: String): Result<List<TestResultListDto>> =
        toeicRemoteDataSource.getToeicResultDetail(id)
}