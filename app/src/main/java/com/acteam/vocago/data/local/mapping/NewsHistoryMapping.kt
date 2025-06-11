package com.acteam.vocago.data.local.mapping

import com.acteam.vocago.data.local.entity.NewsHistoryEntity
import com.acteam.vocago.data.model.NewsHistoryDto

fun toNewsHistoryEntity(
    data: NewsHistoryDto,
    page: Int,
): NewsHistoryEntity {
    return NewsHistoryEntity(
        _id = data._id,
        news = data.news,
        score = data.score,
        isBookmarked = data.isBookmarked,
        updatedAt = data.updatedAt,
        createdAt = data.createdAt,
        page = page,
    )
}