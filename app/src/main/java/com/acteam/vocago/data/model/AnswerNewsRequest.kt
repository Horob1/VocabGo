package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AnswerNewsRequest(
    val score: Int,
    val qsLog: List<NewsDetailLogQsaDto>,
)