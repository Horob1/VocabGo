package com.acteam.vocago.presentation.screen.auth

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acteam.vocago.presentation.navigation.NavScreen

@Composable
fun SetupAuthNavGraph(
    rootNavController: NavHostController,
) {
    val authNavController = rememberNavController()
    Scaffold { innerPadding ->
        NavHost(
            navController = authNavController,
            startDestination = NavScreen.LoginNavScreen,
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<NavScreen.LoginNavScreen> {

            }
            composable<NavScreen.RegisterNavScreen> {

            }
            composable<NavScreen.ForgotPasswordNavScreen> {

            }
            composable<NavScreen.ResetPasswordNavScreen> {

            }
            composable<NavScreen.VerifyEmailNavScreen> {

            }
            composable<NavScreen.TwoFACodeNavScreen> {

            }
        }
    }

}