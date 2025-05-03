package com.acteam.vocago.presentation.navigation

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.acteam.vocago.presentation.screen.auth.forgotpassword.ForgotPasswordScreen
import com.acteam.vocago.presentation.screen.auth.login.LoginScreen
import com.acteam.vocago.presentation.screen.auth.register.RegisterScreen
import com.acteam.vocago.presentation.screen.auth.resetpassword.ResetPasswordScreen
import com.acteam.vocago.presentation.screen.auth.verifyemail.VerifyEmailScreen
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
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
    ) {
        composable<NavScreen.WelcomeNavScreen> {
            val welcomeViewModel = koinViewModel<WelcomeViewModel>()
            WelcomeScreen(
                viewModel = welcomeViewModel,
                onClickToHome = {
                    navController.popBackStack()
                    navController.navigate(NavScreen.HomeNavScreen) {
                        launchSingleTop = true
                    }
                },
                onClickToLogin = {
                    navController.popBackStack()
                    navController.navigate(NavScreen.HomeNavScreen) {
                        launchSingleTop = true
                    }
                    // Delay nhỏ để đảm bảo Home được render và thêm vào stack
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(NavScreen.AuthNavScreen)
                    }, 100)
                }
            )
        }
        navigation<NavScreen.AuthNavScreen>(
            startDestination = NavScreen.LoginNavScreen
        ) {
            composable<NavScreen.LoginNavScreen> {
                LoginScreen(
                    {
                        navController.popBackStack()
                    },
                    {
                        navController.navigate(NavScreen.RegisterNavScreen)
                    },
                    {
                        navController.navigate(NavScreen.ForgotPasswordNavScreen)
                    },
                )
            }
            composable<NavScreen.RegisterNavScreen> {
                RegisterScreen(
                    {
                        navController.popBackStack()
                    },
                    {
                        navController.navigate(NavScreen.VerifyEmailNavScreen)
                    }
                )
            }
            composable<NavScreen.ForgotPasswordNavScreen> {
                ForgotPasswordScreen(
                    {
                        navController.popBackStack()
                    },
                    {
                        navController.navigate(NavScreen.ResetPasswordNavScreen)
                    },
                )
            }
            composable<NavScreen.ResetPasswordNavScreen> {
                ResetPasswordScreen(
                    {
                        navController.popBackStack()
                    }, {}, {
                        navController.navigate(NavScreen.LoginNavScreen)
                    }
                )
            }
            composable<NavScreen.VerifyEmailNavScreen> {
                VerifyEmailScreen(
                    {
                        navController.popBackStack()
                    },
                    {

                    })
            }
        }
        composable<NavScreen.HomeNavScreen> {
            Home {
                navController.navigate(NavScreen.AuthNavScreen)
            }
        }
    }
}