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
import androidx.navigation.toRoute
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.auth.forgotpassword.ForgotPasswordScreen
import com.acteam.vocago.presentation.screen.auth.forgotpassword.ForgotPasswordViewModel
import com.acteam.vocago.presentation.screen.auth.login.LoginScreen
import com.acteam.vocago.presentation.screen.auth.login.LoginViewModel
import com.acteam.vocago.presentation.screen.auth.register.RegisterScreen
import com.acteam.vocago.presentation.screen.auth.register.RegisterViewModel
import com.acteam.vocago.presentation.screen.auth.resetpassword.ResetPasswordScreen
import com.acteam.vocago.presentation.screen.auth.resetpassword.ResetPasswordViewModel
import com.acteam.vocago.presentation.screen.auth.verify2fa.VerifyTwoFAScreen
import com.acteam.vocago.presentation.screen.auth.verify2fa.VerifyTwoFAViewModel
import com.acteam.vocago.presentation.screen.auth.verifyemail.VerifyEmailScreen
import com.acteam.vocago.presentation.screen.auth.verifyemail.VerifyEmailViewModel
import org.koin.compose.viewmodel.koinViewModel

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
                val loginViewModel = koinViewModel<LoginViewModel>()
                LoginScreen(
                    viewModel = loginViewModel,
                    rootNavController = rootNavController,
                    authNavController = authNavController
                )
            }
            composable<NavScreen.RegisterNavScreen> {
                val registerViewModel = koinViewModel<RegisterViewModel>()
                RegisterScreen(
                    viewModel = registerViewModel,
                    authNavController = authNavController
                )
            }
            composable<NavScreen.ForgotPasswordNavScreen> {
                val forgotPasswordViewModel = koinViewModel<ForgotPasswordViewModel>()
                ForgotPasswordScreen(
                    viewModel = forgotPasswordViewModel,
                    authNavController = authNavController
                )
            }
            composable<NavScreen.ResetPasswordNavScreen> {
                val resetPasswordViewModel = koinViewModel<ResetPasswordViewModel>()
                val arg = it.toRoute<NavScreen.ResetPasswordNavScreen>()
                ResetPasswordScreen(
                    email = arg.email,
                    viewModel = resetPasswordViewModel,
                    authNavController = authNavController
                )
            }
            composable<NavScreen.VerifyEmailNavScreen> {
                val verifyEmailViewModel = koinViewModel<VerifyEmailViewModel>()
                val arg = it.toRoute<NavScreen.VerifyEmailNavScreen>()
                VerifyEmailScreen(
                    email = arg.email,
                    viewModel = verifyEmailViewModel,
                    authNavController = authNavController
                )
            }
            composable<NavScreen.VerifyTwoFANavScreen> {
                val verifyTwoFAViewModel = koinViewModel<VerifyTwoFAViewModel>()
                val arg = it.toRoute<NavScreen.VerifyTwoFANavScreen>()
                VerifyTwoFAScreen(
                    email = arg.email,
                    viewModel = verifyTwoFAViewModel,
                    authNavController = authNavController,
                    rootNavController = rootNavController
                )
            }
        }
    }

}