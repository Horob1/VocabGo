package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.NewsDetailLogQsaDto
import com.acteam.vocago.domain.remote.NewsRemoteDataSource

class AnswerNewsUseCase(
    private val newsRepository: NewsRemoteDataSource,
) {
    suspend operator fun invoke(
        newsId: String,
        score: Int,
        questionLogs: List<NewsDetailLogQsaDto>,
    ) = newsRepository.submitPractice(newsId, score, questionLogs)
}