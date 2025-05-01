package com.acteam.vocago.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.acteam.vocago.R

sealed class ChooseLanguageData(
    @StringRes val languageName: Int,
    @DrawableRes val flag: Int,
    val language: AppLanguage
) {
    object English :
        ChooseLanguageData(R.string.english, R.drawable.uk_flag, AppLanguage.English)

    object Vietnamese :
        ChooseLanguageData(R.string.vietnamese, R.drawable.vi_flag, AppLanguage.Vietnamese)

    object System :
        ChooseLanguageData(R.string.system, R.drawable.capybara_avatar, AppLanguage.System)
}