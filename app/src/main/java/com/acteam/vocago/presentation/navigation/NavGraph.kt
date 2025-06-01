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
import androidx.navigation.toRoute
import com.acteam.vocago.presentation.screen.auth.SetupAuthNavGraph
import com.acteam.vocago.presentation.screen.main.SetupMainNavGraph
import com.acteam.vocago.presentation.screen.main.chat.ChatComponent.CommonChatScreen
import com.acteam.vocago.presentation.screen.main.chat.ChatViewModel
import com.acteam.vocago.presentation.screen.newsdetail.NewsDetailScreen
import com.acteam.vocago.presentation.screen.newsdetail.NewsDetailViewModel
import com.acteam.vocago.presentation.screen.setting.SettingScreen
import com.acteam.vocago.presentation.screen.setting.SettingViewModel
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
        composable<NavScreen.CommonChatNavScreen> {
            val chatViewModel = koinViewModel<ChatViewModel>()
            val arg = it.toRoute<NavScreen.CommonChatNavScreen>()
            CommonChatScreen(
                id = arg.id,
                title = arg.title,
                avatarRes = arg.avatarRes,
                viewModel = chatViewModel,
                rootNavController = navController
            )
        }

        composable<NavScreen.NewsDetailNavScreen> {
            val newsDetailViewModel = koinViewModel<NewsDetailViewModel>()
            val arg = it.toRoute<NavScreen.NewsDetailNavScreen>()
            NewsDetailScreen(
                viewModel = newsDetailViewModel,
                newsId = arg.newsId
            )
        }

        composable<NavScreen.NewsHistoryNavScreen> {
            it.toRoute<NavScreen.ResetPasswordNavScreen>()

        }

        composable<NavScreen.DictionaryNavScreen> {

        }

        composable<NavScreen.CameraNavScreen> {

        }

        composable<NavScreen.SettingNavScreen> {
            val settingViewModel = koinViewModel<SettingViewModel>()
            SettingScreen(
                viewModel = settingViewModel,
                rootNavController = navController
            )
        }
    }
}