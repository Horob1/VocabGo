package com.acteam.vocago.presentation.screen.main.toeictest.component

import com.acteam.vocago.data.model.CommonQuestionDto
import com.acteam.vocago.data.model.Part1QuestionDto
import com.acteam.vocago.data.model.Part2QuestionDto
import com.acteam.vocago.data.model.Part3BlockDto
import com.acteam.vocago.data.model.Part4BlockDto
import com.acteam.vocago.data.model.Part5QuestionDto
import com.acteam.vocago.data.model.Part6BlockDto
import com.acteam.vocago.data.model.Part7BlockDto
import com.acteam.vocago.data.model.SimpleQuestionDto
import com.acteam.vocago.data.model.ToeicDetailDto
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part1Question
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part2Question
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part3Block
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part3Question
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part4Block
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part4Question
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part5Question
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part6Block
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part6Question
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part7Block
import com.acteam.vocago.presentation.screen.main.toeictest.data.Part7Question
import com.acteam.vocago.presentation.screen.main.toeictest.data.TOEICTest

fun ToeicDetailDto.toTest(): TOEICTest {
    return TOEICTest(
        _id = _id,
        title = title,
        description = description,
        fullAudioUrl = fullAudioUrl,
        part1 = part1.map { it.toPart1Question() },
        part2 = part2.map { it.toPart2Question() },
        part3 = part3.map { it.toPart3Block() },
        part4 = part4.map { it.toPart4Block() },
        part5 = part5.map { it.toPart5Question() },
        part6 = part6.map { it.toPart6Block() },
        part7 = part7.map { it.toPart7Block() },
    )
}

// Part 1
fun Part1QuestionDto.toPart1Question() = Part1Question(
    index, imageUrl, audioUrl, answers, correctAnswer, explanation
)

// Part 2
fun Part2QuestionDto.toPart2Question() = Part2Question(
    index, audioUrl, answers, correctAnswer, explanation
)

// Part 3
fun Part3BlockDto.toPart3Block() = Part3Block(
    indexBlock,
    imageUrl,
    audioUrl,
    questions.map { it.toPart3Question() }
)

fun CommonQuestionDto.toPart3Question() = Part3Question(
    index, question, answers, correctAnswer, explanation
)

// Part 4
fun Part4BlockDto.toPart4Block() = Part4Block(
    indexBlock,
    audioUrl,
    questions.map { it.toPart4Question() }
)

fun CommonQuestionDto.toPart4Question() = Part4Question(
    index, question, answers, correctAnswer, explanation
)

// Part 5
fun Part5QuestionDto.toPart5Question() = Part5Question(
    index, question, answers, correctAnswer, explanation
)

// Part 6
fun Part6BlockDto.toPart6Block() = Part6Block(
    indexBlock,
    imageUrl,
    questions.map { it.toPart6Question() }
)

fun SimpleQuestionDto.toPart6Question() = Part6Question(
    index, answers, correctAnswer, explanation
)

// Part 7
fun Part7BlockDto.toPart7Block() = Part7Block(
    indexBlock,
    imageUrl,
    questions.map { it.toPart7Question() }
)

fun CommonQuestionDto.toPart7Question() = Part7Question(
    index, question, answers, correctAnswer, explanation
)
