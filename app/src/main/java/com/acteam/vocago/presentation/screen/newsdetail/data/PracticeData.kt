package com.acteam.vocago.presentation.screen.newsdetail.data

data class PracticeData(
    val isSubmitted: Boolean = false,
    val selectedOptions: List<Int> = List(5) { -1 },
    val currentQuestionIndex: Int = 0,
    val isShowHint: Boolean = false,
    val score: Int = 0,
)