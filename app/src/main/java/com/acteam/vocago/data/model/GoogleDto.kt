package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GoogleSearchDataResponse(
    val items: List<GoogleImage>,
)

@Serializable
data class GoogleImage(
    val link: String,
)