package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.NewsRemoteDataSource

class ToggleBookmarkNewsUseCase(
    private val newsRepository: NewsRemoteDataSource,
) {
    suspend operator fun invoke(newsId: String, isBookmarked: Boolean) =
        newsRepository.toggleBookmark(newsId, isBookmarked)
}