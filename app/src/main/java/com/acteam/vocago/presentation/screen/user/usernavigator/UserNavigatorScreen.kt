package com.acteam.vocago.presentation.screen.user.usernavigator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.acteam.vocago.presentation.screen.user.common.UserTopBar
import com.acteam.vocago.utils.responsiveDP

@Composable
fun UserNavigatorScreen(
    rootNavController: NavController,
    userNavController: NavController,
    viewModel: UserNavigatorViewModel,
) {
    val user by viewModel.userState.collectAsState()
    Column(
        modifier = Modifier
            .padding(
                horizontal = responsiveDP(
                    mobile = 16,
                    tabletPortrait = 24,
                    tabletLandscape = 32
                )
            )
            .fillMaxSize()
    ) {
        

    }
}