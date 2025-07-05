package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ToeicDto(
    val success: Boolean,
    val data: List<ToeicTestDto>,
    val message: String
)

@Serializable
data class ToeicTestDto(
    val _id: String,
    val title: String,
    val description: String
)

@Serializable
data class ToeicDetailResponse(
    val success: Boolean,
    val data: ToeicDetailDto,
    val message: String
)

@Serializable
data class ToeicDetailDto(
    val _id: String,
    val title: String,
    val description: String,
    val fullAudioUrl: String,
    val part1: List<Part1QuestionDto>,
    val part2: List<Part2QuestionDto>,
    val part3: List<Part3BlockDto>,
    val part4: List<Part4BlockDto>,
    val part5: List<Part5QuestionDto>,
    val part6: List<Part6BlockDto>,
    val part7: List<Part7BlockDto>
)

@Serializable
data class Part1QuestionDto(
    val index: Int,
    val imageUrl: String,
    val audioUrl: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)

@Serializable
data class Part2QuestionDto(
    val index: Int,
    val audioUrl: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)

@Serializable
data class Part3BlockDto(
    val indexBlock: Int,
    val imageUrl: String?,
    val audioUrl: String,
    val questions: List<CommonQuestionDto>
)

@Serializable
data class Part4BlockDto(
    val indexBlock: Int,
    val imageUrl: String? = null,
    val audioUrl: String,
    val questions: List<CommonQuestionDto>
)

@Serializable
data class Part5QuestionDto(
    val index: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)

@Serializable
data class Part6BlockDto(
    val indexBlock: Int,
    val imageUrl: String,
    val questions: List<SimpleQuestionDto>
)

@Serializable
data class Part7BlockDto(
    val indexBlock: Int,
    val imageUrl: List<String>,
    val questions: List<CommonQuestionDto>
)

@Serializable
data class CommonQuestionDto(
    val index: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)

@Serializable
data class SimpleQuestionDto(
    val index: Int,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)