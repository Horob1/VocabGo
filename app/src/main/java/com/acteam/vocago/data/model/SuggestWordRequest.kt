package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SuggestWordRequest(
    val query: String
)
