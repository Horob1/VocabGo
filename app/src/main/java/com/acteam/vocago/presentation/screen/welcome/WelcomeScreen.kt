package com.acteam.vocago.presentation.screen.welcome

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acteam.vocago.domain.model.OnBoardingPage
import com.acteam.vocago.presentation.screen.welcome.component.ChooseLanguagePage
import com.acteam.vocago.presentation.screen.welcome.component.PageController
import com.acteam.vocago.presentation.screen.welcome.component.PagerIndicator
import com.acteam.vocago.presentation.screen.welcome.component.PagerScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel,
    onClickToLogin: () -> Unit,
    onClickToHome: () -> Unit

) {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third,
        OnBoardingPage.Fourth,
        OnBoardingPage.Fifth
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { pages.size }
    )

    val scope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HorizontalPager(
                modifier = Modifier.weight(10f),
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { position ->
                if (position == 3) {
                    ChooseLanguagePage(
                        viewModel
                    )
                } else
                    PagerScreen(onBoardingPage = pages[position])
            }
            PagerIndicator(pages.size, pagerState.currentPage, Modifier.fillMaxWidth())
            Spacer(
                modifier = Modifier
                    .height(
                        24.dp
                    )
            )
            PageController(
                pageSize = pages.size,
                current = pagerState.currentPage,
                onNext = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                onPrev = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                onSkip = {
                    viewModel.completeOnBoarding()
                    onClickToHome()
                },
                onClickLogin = {
                    viewModel.completeOnBoarding()
                    onClickToLogin()
                },
            )
        }
    }
}
