package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.SubmitRequest
import com.acteam.vocago.data.model.TestResultDto
import com.acteam.vocago.data.model.TestResultListDto
import com.acteam.vocago.data.model.ToeicDetailDto
import com.acteam.vocago.data.model.ToeicDto

interface ToeicRemoteDataSource {
    suspend fun getToeicList(): Result<ToeicDto>
    suspend fun getToeicDetail(id: String): Result<ToeicDetailDto>
    suspend fun submitToeicTest(request: SubmitRequest): Result<TestResultDto>
    suspend fun getToeicResult(id: String): Result<List<TestResultListDto>>
    suspend fun getToeicResultDetail(id: String): Result<List<TestResultListDto>>
}