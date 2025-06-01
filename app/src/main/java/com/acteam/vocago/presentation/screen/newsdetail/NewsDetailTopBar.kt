package com.acteam.vocago.presentation.screen.newsdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.safeClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailTopBar(
    modifier: Modifier = Modifier,
    backToHome: () -> Unit,
    translate: () -> Unit,
    toggleBookmark: (Boolean) -> Unit,
    speak: () -> Unit,
    stop: () -> Unit,
    isSpeaking: Boolean,
    backgroundAlpha: Float,
    alwaysOnTop: Boolean = true,
    isBookmark: Boolean = true,
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopBarIconButton(
                    onClick = backToHome,
                    contentDescription = "Back",
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                    )
                }
                Spacer(modifier = Modifier.weight(1f, true))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopBarIconButton(
                        onClick = {
                            toggleBookmark(!isBookmark)
                        },
                        contentDescription = "Bookmark",
                    ) {
                        if (!isBookmark) Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Love",
                        )
                        else Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Love",
                            tint = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    TopBarIconButton(
                        onClick = {
                            if (isSpeaking) stop() else speak()
                        },
                        contentDescription = "Speak",
                    ) {
                        if (isSpeaking) Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop Listen",
                        )
                        else Icon(
                            imageVector = Icons.Default.Headset,
                            contentDescription = "Listen",
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    TopBarIconButton(
                        onClick = translate,
                        contentDescription = "Translate",
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Translate",
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                }

            }

        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = backgroundAlpha),
        ),
        modifier = if (alwaysOnTop) modifier.zIndex(1f) else modifier
    )
}

@Composable
fun TopBarIconButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val boxSize = responsiveDP(mobile = 32, tabletPortrait = 36, tabletLandscape = 40)
    Box(
        modifier = modifier
            .size(boxSize)
            .shadow(
                elevation = 2.dp,
                shape = CircleShape,
                clip = false
            )
            .clip(
                CircleShape
            )
            .safeClickable(key = "top_bar_icon_button_$contentDescription") { onClick() }
            .background(
                MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
