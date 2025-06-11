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
import com.acteam.vocago.presentation.screen.choosevoca.ChooseVocaListScreen
import com.acteam.vocago.presentation.screen.dictionary.DictionaryScreen
import com.acteam.vocago.presentation.screen.main.SetupMainNavGraph
import com.acteam.vocago.presentation.screen.main.chat.ChatViewModel
import com.acteam.vocago.presentation.screen.main.chat.component.CommonChatScreen
import com.acteam.vocago.presentation.screen.main.chat.component.VideoCallScreen
import com.acteam.vocago.presentation.screen.newsdetail.NewsDetailScreen
import com.acteam.vocago.presentation.screen.newsdetail.NewsDetailViewModel
import com.acteam.vocago.presentation.screen.newshistory.NewsHistoryScreen
import com.acteam.vocago.presentation.screen.newshistory.NewsHistoryViewModel
import com.acteam.vocago.presentation.screen.setting.SettingScreen
import com.acteam.vocago.presentation.screen.setting.SettingViewModel
import com.acteam.vocago.presentation.screen.welcome.WelcomeScreen
import com.acteam.vocago.presentation.screen.welcome.WelcomeViewModel
import com.acteam.vocago.presentation.screen.worddetail.WordDetailScreen
import com.acteam.vocago.presentation.screen.worddetail.WordDetailViewModel
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
                newsId = arg.newsId,
                rootNavController = navController
            )
        }

        composable<NavScreen.NewsHistoryNavScreen> {
            val arg = it.toRoute<NavScreen.NewsHistoryNavScreen>()
            val isBookmark = arg.isBookmark
            val newsHistoryViewModel = koinViewModel<NewsHistoryViewModel>()
            newsHistoryViewModel.setIsBookmark(isBookmark)
            NewsHistoryScreen(
                viewModel = newsHistoryViewModel,
                rootNavController = navController
            )
        }

        composable<NavScreen.WordDetailNavScreen> {
            val arg = it.toRoute<NavScreen.WordDetailNavScreen>()
            val word = arg.word
            val wordDetailViewModel = koinViewModel<WordDetailViewModel>()
            WordDetailScreen(
                word = word,
                rootNavController = navController,
                viewModel = wordDetailViewModel,
            )
        }

        composable<NavScreen.ChooseVocaListNavScreen> {
            val arg = it.toRoute<NavScreen.ChooseVocaListNavScreen>()
            val word = arg.word
            ChooseVocaListScreen(
                word = word,
                rootNavController = navController
            )
        }

        composable<NavScreen.DictionaryNavScreen> {
            DictionaryScreen()
        }

        composable<NavScreen.CameraNavScreen> {

        }
        composable<NavScreen.VideoCallNavScreen> {
            val arg = it.toRoute<NavScreen.VideoCallNavScreen>()
            VideoCallScreen(
                receivedName = arg.receivedName,
                avatarResId = arg.avatarResId,
                videoResId = arg.videoResId,
                rootNavController = navController
            )
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