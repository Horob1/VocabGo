package com.acteam.vocago.data.local.mapping

import com.acteam.vocago.data.local.entity.NewsEntity
import com.acteam.vocago.data.local.entity.NewsEntityWord
import com.acteam.vocago.data.model.NewsDto

fun toNewsEntity(
    data: NewsDto,
    page: Int,
): NewsEntity {

    val words = NewsEntityWord(
        a1 = data.words.count { it.level == "A1" },
        a2 = data.words.count { it.level == "A2" },
        b1 = data.words.count { it.level == "B1" },
        b2 = data.words.count { it.level == "B2" },
    )

    return NewsEntity(
        _id = data._id,
        category = data.category,
        title = data.title,
        url = data.url,
        coverImage = data.coverImage,
        views = data.views,
        level = data.level,
        words = words,
        createdAt = data.createdAt,
        page = page,
    )
}