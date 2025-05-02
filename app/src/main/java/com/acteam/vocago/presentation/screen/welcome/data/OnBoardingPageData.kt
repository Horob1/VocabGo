package com.acteam.vocago.presentation.screen.welcome.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.acteam.vocago.R

sealed class OnBoardingPageData(
    @DrawableRes
    val image: Int,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int
) {
    object First : OnBoardingPageData(
        image = R.drawable.onboarding_1,
        title = R.string.onboarding_title_1,
        description = R.string.onboarding_detail_1
    )

    object Second : OnBoardingPageData(
        image = R.drawable.onboarding_2,
        title = R.string.onboarding_title_2,
        description = R.string.onboarding_detail_2
    )

    object Third : OnBoardingPageData(
        image = R.drawable.onboarding_3,
        title = R.string.onboarding_title_3,
        description = R.string.onboarding_detail_3
    )

    object Fourth : OnBoardingPageData(
        image = 0,
        title = 0,
        description = 0
    )

    object Fifth : OnBoardingPageData(
        image = R.drawable.onboarding_5,
        title = R.string.onboarding_title_5,
        description = R.string.onboarding_detail_5
    )
}