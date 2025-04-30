package com.acteam.vocago.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acteam.vocago.presentation.screen.home.Home
import com.acteam.vocago.presentation.screen.login.LoginScreen
import com.acteam.vocago.presentation.screen.welcome.WelcomeScreen

@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: NavScreen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<NavScreen.WelcomeNavScreen> {
            WelcomeScreen(
                onClickFinished = {
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