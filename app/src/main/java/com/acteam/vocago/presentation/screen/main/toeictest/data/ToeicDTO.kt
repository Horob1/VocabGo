package com.acteam.vocago.presentation.screen.main.toeictest.data

data class TOEICTest(
    val _id: String,
    val title: String,
    val description: String,
    val fullAudioUrl: String,
    val part1: List<Part1Question>,
    val part2: List<Part2Question>,
    val part3: List<Part3Block>,
    val part4: List<Part4Block>,
    val part5: List<Part5Question>,
    val part6: List<Part6Block>,
    val part7: List<Part7Block>
)

data class Part1Question(
    val index: Int,
    val imageUrl: String,
    val audioUrl: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)

data class Part2Question(
    val index: Int,
    val audioUrl: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)

data class Part3Block(
    val indexBlock: Int,
    val imageUrl: String?,
    val audioUrl: String,
    val questions: List<Part3Question>
)

data class Part3Question(
    val index: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)

data class Part4Block(
    val indexBlock: Int,
    val audioUrl: String,
    val questions: List<Part4Question>
)

data class Part4Question(
    val index: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)

data class Part5Question(
    val index: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)

data class Part6Block(
    val indexBlock: Int,
    val imageUrl: String,
    val questions: List<Part6Question>
)

data class Part6Question(
    val index: Int,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)

data class Part7Block(
    val indexBlock: Int,
    val imageUrl: List<String>,
    val questions: List<Part7Question>
)

data class Part7Question(
    val index: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswer: String,
    val explanation: String
)
