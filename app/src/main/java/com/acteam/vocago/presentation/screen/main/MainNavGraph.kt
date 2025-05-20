package com.acteam.vocago.presentation.screen.main


import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acteam.vocago.presentation.navigation.NavScreen

@Composable
fun SetupMainNavGraph(rootNavController: NavController) {
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            Button(onClick = { rootNavController.navigate(NavScreen.AuthNavScreen) }) {
                Text(text = "Go to Auth")
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = NavScreen.NewsNavScreen,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
        ) {
            composable<NavScreen.NewsNavScreen> {}
        }
    }
}