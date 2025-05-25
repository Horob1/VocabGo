package com.acteam.vocago.domain.model

import androidx.annotation.StringRes
import com.acteam.vocago.R

enum class NewsLevel(val value: String, @StringRes val stringInt: Int) {
    EASY("easy", R.string.enum_news_level_easy),
    MEDIUM("medium", R.string.enum_news_level_medium),
    HARD("hard", R.string.enum_news_level_hard),
    ALL("all", R.string.enum_news_level_all)
}