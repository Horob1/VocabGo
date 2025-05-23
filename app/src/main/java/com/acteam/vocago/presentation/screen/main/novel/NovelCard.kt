package com.acteam.vocago.presentation.screen.main.novel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun NovelCard() {
    val url =
        "https://lh3.googleusercontent.com/pw/AP1GczOBBMpnlwmU1GA7TI-h_DpHyDQu2Nb7m2B8tm4ryQQL3XL-cO93XgNQK31C1cY0SsGRc69NeLQvtLlgoA7_qq5C2umsl1YQrFTc0UCdDL85IHE5DBTvSwpdtY4sZo1GsIVpvI8vPFPjra_ped343sV2=w215-h322-s-no-gm?authuser=4"
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                responsiveDP(
                    mobile = 8,
                    tabletPortrait = 12,
                    tabletLandscape = 12
                )
            )
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
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
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "The Fellowship Of The Ring",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = responsiveSP(
                        mobile = 14,
                        tabletPortrait = 16,
                        tabletLandscape = 18
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 2,
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
                "J.R.R Token", style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        0.7f
                    )
                )
            )
        }
    }
}