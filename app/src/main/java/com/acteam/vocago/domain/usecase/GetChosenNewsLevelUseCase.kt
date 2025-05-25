package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.NewsLocalDataSource
import com.acteam.vocago.domain.model.NewsLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetChosenNewsLevelUseCase(
    private val newsLocalDataSource: NewsLocalDataSource,
) {
    operator fun invoke(): Flow<NewsLevel> = newsLocalDataSource.getChosenLevel().map {
        when (it) {
            "easy" -> NewsLevel.EASY
            "medium" -> NewsLevel.MEDIUM
            "hard" -> NewsLevel.HARD
            else -> NewsLevel.ALL
        }
    }
}