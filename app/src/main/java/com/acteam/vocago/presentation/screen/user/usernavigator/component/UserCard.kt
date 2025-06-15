package com.acteam.vocago.presentation.screen.user.usernavigator.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.utils.responsiveDP

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    user: UserDto?,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val context = LocalContext.current
        val placeholderPainter = painterResource(id = R.drawable.capybara_avatar)
        val imageRequest = remember(user?.avatar, context) {
            ImageRequest.Builder(context)
                .data(user?.avatar)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
        }

        val avatarSize = responsiveDP(mobile = 160, tabletPortrait = 200, tabletLandscape = 200)

        val name = remember(user) {
            if (user !== null)
                user.firstName + " " + user.lastName
            else ""
        }

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (user?.avatar.isNullOrEmpty()) {
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

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                user?.email ?: "Email ...",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
            )
        }
    }
}