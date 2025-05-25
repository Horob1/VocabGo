package com.acteam.vocago.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.acteam.vocago.domain.usecase.GetDynamicColorUseCase
import com.acteam.vocago.domain.usecase.GetStartScreenUseCase
import com.acteam.vocago.domain.usecase.GetThemeUseCase
import com.acteam.vocago.presentation.navigation.SetupNavGraph
import com.acteam.vocago.presentation.ui.theme.VocaGoTheme
import com.acteam.vocago.utils.isTablet
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

class MainActivity : AppCompatActivity(), KoinComponent {
    private val getOnBoardingStateUseCase: GetStartScreenUseCase by inject()

    @SuppressLint("SourceLockedOrientationActivity")
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!this.isTablet()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        installSplashScreen()
        enableEdgeToEdge()
        val getThemeUseCase = get<GetThemeUseCase>()
        val getDynamicColorUseCase = get<GetDynamicColorUseCase>()
        setContent {
            VocaGoTheme(
                getTheme = getThemeUseCase,
                getDynamicColor = getDynamicColorUseCase
            ) {
                val startDestination = getOnBoardingStateUseCase()
                val navController = rememberNavController()
                SetupNavGraph(navController = navController, startDestination = startDestination)
            }
        }
    }
}
