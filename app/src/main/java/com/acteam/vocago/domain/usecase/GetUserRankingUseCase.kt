package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.GetUserRankingResponse
import com.acteam.vocago.domain.remote.UserRemoteDataSource

class GetUserRankingUseCase(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    suspend operator fun invoke(): GetUserRankingResponse {
        return userRemoteDataSource.getUserRanking()
    }
}