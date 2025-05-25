package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.local.NewsLocalDataSource
import com.acteam.vocago.domain.model.NewsLevel

class UpdateChosenNewsLevelUseCase(
    private val newsLocalDataSource: NewsLocalDataSource,
) {
    suspend operator fun invoke(level: NewsLevel) =
        newsLocalDataSource.updateChosenLevel(level.value)

}