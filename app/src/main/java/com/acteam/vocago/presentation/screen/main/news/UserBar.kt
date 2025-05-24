package com.acteam.vocago.presentation.screen.main.news

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP
import com.acteam.vocago.utils.safeClickable


@Composable
fun UserBar(
    isAuth: Boolean,
    userState: UserDto?,
    navigateToProfile: () -> Unit,
    navigateToLogin: () -> Unit,
    onLoadProfile: suspend () -> Unit,
) {

    LaunchedEffect(key1 = isAuth) {
        onLoadProfile()
    }

    val name = when {
        !isAuth -> stringResource(R.string.text_anonymous)
        userState == null -> "..."
        else -> userState.firstName + " " + userState.lastName
    }
    val titleText = "${stringResource(R.string.text_hello)}, $name"

    Row(
        Modifier
            .padding(
                horizontal = responsiveDP(mobile = 16, tabletPortrait = 24, tabletLandscape = 32),
                vertical = responsiveDP(mobile = 8, tabletPortrait = 12, tabletLandscape = 16)
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
                titleText, style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = responsiveSP(
                        mobile = 20,
                        tabletPortrait = 22,
                        tabletLandscape = 24
                    ),
                    color = MaterialTheme.colorScheme.primary

                )
            )
            Spacer(
                modifier = Modifier.size(
                    responsiveDP(
                        mobile = 4,
                        tabletPortrait = 8,
                        tabletLandscape = 12
                    )
                )
            )
            Text(
                stringResource(R.string.text_wellcome_to_vocago),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        0.7f
                    )
                )
            )
        }
        UserAvatar(
            imageUrl = userState?.avatar,
            placeholderRes = R.drawable.capybara_avatar,
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
    placeholderRes: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(responsiveDP(mobile = 40, tabletPortrait = 48, tabletLandscape = 56))
            .clip(CircleShape)
            .safeClickable(key = "user_bar_profile") { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when {

            imageUrl.isNullOrEmpty() -> Image(
                painter = painterResource(id = placeholderRes),
                contentDescription = "User Avatar Default",
                modifier = Modifier.fillMaxSize()
            )

            else -> SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "User Avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    Image(
                        painter = painterResource(id = placeholderRes),
                        contentDescription = "User Avatar Error",
                        modifier = Modifier.fillMaxSize()
                    )
                },
                error = {
                    Image(
                        painter = painterResource(id = placeholderRes),
                        contentDescription = "User Avatar Error",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            )

        }
    }
}