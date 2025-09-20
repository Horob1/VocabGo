package com.acteam.vocago.presentation.screen.main.news.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.main.news.NewsViewModel
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP
import com.acteam.vocago.utils.safeClickable

@Composable
fun UserBar(
    navigateToProfile: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: NewsViewModel,
) {


    val isAuth by viewModel.loginState.collectAsState()
    val userProfile by viewModel.userState.collectAsState()


    LaunchedEffect(isAuth) {
        if (isAuth) {
            viewModel.syncProfile()
        }
    }

    val anonymousText = stringResource(R.string.text_anonymous)
    val helloText = stringResource(R.string.text_hello)

    val name by remember(isAuth, userProfile, anonymousText) {
        derivedStateOf {
            when {
                !isAuth -> anonymousText
                userProfile == null -> "..."
                else -> "${userProfile?.firstName ?: ""} ${userProfile?.lastName ?: ""}".trim()
            }
        }
    }

    val titleText = "$helloText, ${name.limit(10)}"

    val horizontalPadding =
        responsiveDP(mobile = 16, tabletPortrait = 24, tabletLandscape = 32)
    val verticalPadding =
        responsiveDP(mobile = 8, tabletPortrait = 12, tabletLandscape = 16)
    val spacerSize = responsiveDP(mobile = 4, tabletPortrait = 8, tabletLandscape = 12)
    val fontSize = responsiveSP(mobile = 20, tabletPortrait = 22, tabletLandscape = 24)

    Row(
        Modifier
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titleText, // Explicitly pass text
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = fontSize,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(
                modifier = Modifier.size(
                    spacerSize
                )
            )
            Text(
                text = stringResource(R.string.text_wellcome_to_vocago),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        0.7f
                    )
                )
            )
        }

        UserAvatar(
            imageUrl = if (isAuth) userProfile?.avatar else null,
            onClick = {
                if (isAuth) navigateToProfile()
                else navigateToLogin()
            }
        )
    }
}

@Composable
fun UserAvatar(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    val context = LocalContext.current

    val placeholderPainter = painterResource(id = R.drawable.capybara_avatar)

    val imageRequest = remember(imageUrl, context) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    val avatarSize = responsiveDP(mobile = 40, tabletPortrait = 48, tabletLandscape = 56)

    Box(
        modifier = modifier
            .size(avatarSize)
            .clip(CircleShape)
            .safeClickable(key = "user_bar_profile") { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNullOrEmpty()) {
            Image(
                painter = placeholderPainter,
                contentDescription = "User Avatar Placeholder",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = "User Avatar Online",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.size(avatarSize * 0.5f)
                    )
                },
                error = {
                    Image(
                        painter = placeholderPainter,
                        contentDescription = "User Avatar Error",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            )
        }
    }
}

fun String.limit(maxLength: Int): String =
    if (this.length > maxLength) this.take(maxLength) + "…" else this