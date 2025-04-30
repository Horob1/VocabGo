package com.acteam.vocago.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.acteam.vocago.domain.usecase.GetStartScreenUseCase
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.ui.theme.VocaGoTheme
import com.acteam.vocago.presentation.navigation.SetupNavGraph
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : ComponentActivity(), KoinComponent {
    private val getOnBoardingStateUseCase: GetStartScreenUseCase by inject()
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        enableEdgeToEdge()
        setContent {
            VocaGoTheme {
                var startDestination: NavScreen by remember { mutableStateOf(NavScreen.HomeNavScreen) }
                LaunchedEffect(Unit) {
                    startDestination = getOnBoardingStateUseCase()
                    delay(100)
                    splashScreen.setKeepOnScreenCondition { false }
                }

                val navController = rememberNavController()
                SetupNavGraph(navController = navController, startDestination = startDestination)
            }
        }
    }
}
