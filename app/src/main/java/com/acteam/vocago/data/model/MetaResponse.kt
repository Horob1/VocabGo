package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MetaResponse(val total: Int, val limit: Int, val page: Int, val totalPages: Int)