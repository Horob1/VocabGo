package com.acteam.vocago.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.acteam.vocago.presentation.screen.auth.SetupAuthNavGraph
import com.acteam.vocago.presentation.screen.main.SetupMainNavGraph
import com.acteam.vocago.presentation.screen.welcome.WelcomeScreen
import com.acteam.vocago.presentation.screen.welcome.WelcomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: NavScreen,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
    ) {
        composable<NavScreen.WelcomeNavScreen> {
            val welcomeViewModel = koinViewModel<WelcomeViewModel>()
            WelcomeScreen(
                viewModel = welcomeViewModel,
                navController = navController
            )
        }
        composable<NavScreen.AuthNavScreen> {
            SetupAuthNavGraph(
                rootNavController = navController
            )
        }
        composable<NavScreen.MainNavScreen> {
            SetupMainNavGraph(
                rootNavController = navController
            )
        }
        composable<NavScreen.UserNavScreen> {
            
        }
    }
}