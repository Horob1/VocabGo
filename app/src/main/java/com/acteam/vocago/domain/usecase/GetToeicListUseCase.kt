package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.ToeicDto
import com.acteam.vocago.domain.remote.ToeicRemoteDataSource

class GetToeicListUseCase(
    private val toeicRemoteDataSource: ToeicRemoteDataSource
) {
    suspend operator fun invoke(): Result<ToeicDto> =
        toeicRemoteDataSource.getToeicList()
}