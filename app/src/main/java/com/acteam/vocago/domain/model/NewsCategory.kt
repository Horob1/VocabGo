package com.acteam.vocago.domain.model

import androidx.annotation.StringRes
import com.acteam.vocago.R

enum class NewsCategory(val value: String, @StringRes val stringResId: Int) {
    NEWS("News", R.string.enum_news_category_news),
    TRAVEL("Travel", R.string.enum_news_category_travel),
    BUSINESS("Business", R.string.enum_news_category_business),
    LIFE("Life", R.string.enum_news_category_life),
    TECH("Tech", R.string.enum_news_category_tech),
    SPORT("Sports", R.string.enum_news_category_sport),
    WORLD("World", R.string.enum_news_category_world),
    PERPECTIVES("Perpectives", R.string.enum_news_category_perpectives)
}