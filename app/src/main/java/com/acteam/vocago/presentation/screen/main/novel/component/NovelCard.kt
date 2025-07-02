package com.acteam.vocago.presentation.screen.main.novel.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.Novel
import com.acteam.vocago.utils.DateDisplayHelper
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun NovelCard(
    modifier: Modifier = Modifier,
    novel: Novel,
    onClick: (String) -> Unit,
) {
    val imageModifier = Modifier
        .height(
            responsiveDP(
                mobile = 100,
                tabletPortrait = 120,
                tabletLandscape = 140// fix sau
            )
        )
        .aspectRatio(2f / 3f)

        .clip(
            MaterialTheme.shapes.medium
        )

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                responsiveDP(
                    mobile = 8,
                    tabletPortrait = 12,
                    tabletLandscape = 12
                )
            )
            .clickable {
                onClick(novel._id)
            }
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(novel.image)
                .crossfade(true)
                .build(),
            contentDescription = "User Avatar",
            modifier = imageModifier,
            contentScale = ContentScale.Crop,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            error = {
                Image(
                    painter = painterResource(id = R.drawable.capy_vi),
                    contentDescription = "User Avatar Error",
                    modifier = imageModifier
                )
            },
        )

        Spacer(
            modifier = Modifier.width(
                responsiveDP(
                    mobile = 12,
                    tabletPortrait = 16,
                    tabletLandscape = 16
                )
            )
        )

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                novel.fictionTitle,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                novel.author, style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        0.7f
                    )
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
                "${novel.totalChapters} ${stringResource(id = R.string.chapters)}",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = responsiveSP(
                        mobile = 14,
                        tabletPortrait = 16,
                        tabletLandscape = 18
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        0.7f
                    )
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
                "${stringResource(R.string.createdAt)}: ${
                    DateDisplayHelper.formatDateString(
                        novel.createdAt
                    )
                }",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        0.7f
                    )
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
                "${stringResource(R.string.updatedAt)}: ${
                    DateDisplayHelper.formatDateString(
                        novel.updatedAt
                    )
                }",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        0.7f
                    )
                )
            )
        }
    }
}