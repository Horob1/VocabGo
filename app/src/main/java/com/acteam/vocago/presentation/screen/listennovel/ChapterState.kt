package com.acteam.vocago.presentation.screen.listennovel

interface ChapterPlayerState {
    object Loading : ChapterPlayerState
    data class Playing(
        val state: NovelPlayerState,
        val isPlaying: Boolean,
    ) : ChapterPlayerState
}

data class NovelPlayerState(
    val _id: String,
    val chapterNumber: Int,
    val chapterTitle: String,
    val paragraphs: List<String>,
    val nextChapterId: String?,
    val previousChapterId: String?,
    val currentParagraphIndex: Int = 0,
    val totalParagraphs: Int,
)