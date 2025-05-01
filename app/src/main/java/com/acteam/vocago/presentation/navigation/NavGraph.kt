package com.acteam.vocago.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acteam.vocago.presentation.screen.auth.LoginScreen
import com.acteam.vocago.presentation.screen.home.Home
import com.acteam.vocago.presentation.screen.welcome.WelcomeScreen
import com.acteam.vocago.presentation.screen.welcome.WelcomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: NavScreen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
    ) {
        composable<NavScreen.WelcomeNavScreen> {
            val welcomeViewModel = koinViewModel<WelcomeViewModel>()
            WelcomeScreen(
                viewModel = welcomeViewModel,
                onClickToHome = {
                    navController.popBackStack()
                    navController.navigate(NavScreen.HomeNavScreen)
                },
                onClickToLogin = {
                    navController.popBackStack()
                    navController.navigate(NavScreen.AuthNavScreen)
                }
            )
        }
        navigation<NavScreen.AuthNavScreen>(
            startDestination = NavScreen.LoginNavScreen
        ) {
            composable<NavScreen.LoginNavScreen> {
                LoginScreen()
            }
            composable<NavScreen.RegisterNavScreen> {}
        }
        composable<NavScreen.HomeNavScreen> {
            Home()
        }
    }
}