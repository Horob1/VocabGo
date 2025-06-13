package com.acteam.vocago.presentation.screen.user

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.user.usernavigator.UserNavigatorScreen
import com.acteam.vocago.presentation.screen.user.usernavigator.UserNavigatorViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupUserNavGraph(
    rootNavController: NavController,
) {
    val userNavController = rememberNavController()
    Scaffold { innerPadding ->
        NavHost(

            navController = userNavController,
            startDestination = NavScreen.UserNavNavigatorScreen,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
        ) {
            composable<NavScreen.UserNavNavigatorScreen> {
                val userNavigatorViewModel: UserNavigatorViewModel = koinViewModel()
                UserNavigatorScreen(
                    rootNavController,
                    userNavController,
                    userNavigatorViewModel
                )
            }

            composable<NavScreen.AlarmNavScreen> { }

            composable<NavScreen.ProfileNavScreen> { }

            composable<NavScreen.BillingNavScreen> { }

            composable<NavScreen.AnalyzeUserNavScreen> { }
        }
    }
}