package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.RankingResponse
import com.acteam.vocago.domain.remote.UserRemoteDataSource

class GetRankingUseCase(
    private val userRemoteDataSource: UserRemoteDataSource
) {
    suspend operator fun invoke(): RankingResponse {
        return userRemoteDataSource.getRanking()
    }
}