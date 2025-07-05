package com.acteam.vocago.data.model


import kotlinx.serialization.Serializable

@Serializable
data class TestResultResponse(
    val success: Boolean,
    val data: List<TestResultListDto>,
    val message: String
)

@Serializable
data class TestResultDetailResponse(
    val success: Boolean,
    val data: TestResultListDto,
    val message: String
)

@Serializable
data class TestResultListDto(
    val _id: String,
    val testId: String,
    val userId: String,
    val totalScore: Int,
    val listeningScore: Int,
    val readingScore: Int,
    val completionTime: Int,
    val submittedAt: String,
    val startTime: String,
    val endTime: String,
    val parts: List<PartResult>
)
