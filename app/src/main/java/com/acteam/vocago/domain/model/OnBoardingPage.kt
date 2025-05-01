package com.acteam.vocago.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.acteam.vocago.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int
) {
    object First : OnBoardingPage(
        image = R.drawable.onboarding_1,
        title = R.string.onboarding_title_1,
        description = R.string.onboarding_detail_1
    )

    object Second : OnBoardingPage(
        image = R.drawable.onboarding_2,
        title = R.string.onboarding_title_2,
        description = R.string.onboarding_detail_2
    )

    object Third : OnBoardingPage(
        image = R.drawable.onboarding_3,
        title = R.string.onboarding_title_3,
        description = R.string.onboarding_detail_3
    )

    object Fourth : OnBoardingPage(
        image = 0,
        title = 0,
        description = 0
    )

    object Fifth : OnBoardingPage(
        image = R.drawable.onboarding_5,
        title = R.string.onboarding_title_5,
        description = R.string.onboarding_detail_5
    )
}