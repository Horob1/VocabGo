package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SuggestionResponse(
    val suggestions: List<String>
)
