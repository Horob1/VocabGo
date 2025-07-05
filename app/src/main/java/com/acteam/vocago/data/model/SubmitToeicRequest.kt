package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SubmitRequest(
    val testId: String,
    val userId: String,
    val startTime: String,
    val endTime: String,
    val timeSpent: Int,
    val userAnswers: Map<String, Map<String, String>>
)

