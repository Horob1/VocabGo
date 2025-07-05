package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SubmitToeicResponse(
    val success: Boolean,
    val data: TestResultDto,
    val message: String
)

@Serializable
data class TestResultDto(
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

@Serializable
data class PartResult(
    val partNumber: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val score: Int,
    val questions: List<QuestionResult>
)

@Serializable
data class QuestionResult(
    val questionIndex: Int,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val explanation: String,
    val _id: String
)
