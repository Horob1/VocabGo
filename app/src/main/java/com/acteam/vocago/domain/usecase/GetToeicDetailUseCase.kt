package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.ToeicDetailDto
import com.acteam.vocago.domain.remote.ToeicRemoteDataSource

class GetToeicDetailUseCase(
    private val toeicRemoteDataSource: ToeicRemoteDataSource
) {
    suspend operator fun invoke(id: String): Result<ToeicDetailDto> =
        toeicRemoteDataSource.getToeicDetail(id)
}
