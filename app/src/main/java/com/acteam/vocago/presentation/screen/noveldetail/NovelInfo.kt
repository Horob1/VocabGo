package com.acteam.vocago.presentation.screen.noveldetail

import java.io.Serializable

data class NovelInfo(
    val name: String,
    val url: String,
    val chapterList: List<ChapterInfo>,
    val _id: String,
) : Serializable

data class ChapterInfo(
    val _id: String,
    val number: Int,
) : Serializable