package com.acteam.vocago.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.acteam.vocago.domain.model.AppTheme
import com.acteam.vocago.domain.usecase.GetDynamicColorUseCase
import com.acteam.vocago.domain.usecase.GetStartScreenUseCase
import com.acteam.vocago.domain.usecase.GetThemeUseCase
import com.acteam.vocago.presentation.navigation.SetupNavGraph
import com.acteam.vocago.presentation.socket.SocketViewModel
import com.acteam.vocago.presentation.ui.theme.VocaGoTheme
import com.acteam.vocago.utils.isTablet
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : AppCompatActivity(), KoinComponent {
    private val getOnBoardingStateUseCase: GetStartScreenUseCase by inject()
    private val getThemeUseCase: GetThemeUseCase by inject()
    private val getDynamicColorUseCase: GetDynamicColorUseCase by inject()
    private var isReady = false

    @SuppressLint("SourceLockedOrientationActivity")
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!this.isTablet()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !isReady }

        enableEdgeToEdge()

        setContent {
            val userTheme by getThemeUseCase().collectAsState(initial = AppTheme.SYSTEM)
            val dynamicColor by getDynamicColorUseCase().collectAsState(initial = false)

            LaunchedEffect(userTheme, dynamicColor) {
                isReady = true
            }

            val startDestination = remember { getOnBoardingStateUseCase() }
            koinViewModel<SocketViewModel>()

            VocaGoTheme(
                userTheme = userTheme,
                dynamicColorEnabled = dynamicColor
            ) {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController, startDestination = startDestination)
            }
        }
    }
}
