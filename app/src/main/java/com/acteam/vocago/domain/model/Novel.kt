package com.acteam.vocago.domain.model

data class Novel(
    val _id: String,
    val fictionNumber: Int,
    val fictionTitle: String,
    val totalChapters: Int,
    val image: String,
    val author: String,
    val isPublish: Boolean,
    val createdAt: String,
    val updatedAt: String,
)
