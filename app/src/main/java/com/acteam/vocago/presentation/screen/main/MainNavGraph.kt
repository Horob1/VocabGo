package com.acteam.vocago.presentation.screen.main


import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.main.chat.ChatScreen
import com.acteam.vocago.presentation.screen.main.common.NavBottomBar
import com.acteam.vocago.presentation.screen.main.news.NewsScreen
import com.acteam.vocago.presentation.screen.main.news.NewsViewModel
import com.acteam.vocago.presentation.screen.main.novel.NovelScreen
import com.acteam.vocago.presentation.screen.main.novel.NovelViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupMainNavGraph(rootNavController: NavController) {
    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavBottomBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = NavScreen.NewsNavScreen,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
        ) {
            composable<NavScreen.NewsNavScreen> {
                val newsViewModel = koinViewModel<NewsViewModel>()
                NewsScreen(
                    viewModel = newsViewModel,
                    rootNavController = rootNavController,
                    navController = bottomNavController
                )
            }
            composable<NavScreen.NovelNavScreen> {
                val novelViewModel = koinViewModel<NovelViewModel>()
                NovelScreen(
                    viewModel = novelViewModel,
                    rootNavController = rootNavController,
                )
            }
            composable<NavScreen.VocaNavScreen> {
                Text(text = "Voca")
            }
            composable<NavScreen.ToeicNavScreen> {
                Text(text = "Toeic")
            }
            composable<NavScreen.ChatNavScreen> {
                ChatScreen(
                    rootNavController = rootNavController,
                )
            }
        }
    }
}