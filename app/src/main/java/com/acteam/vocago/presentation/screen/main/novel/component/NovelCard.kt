package com.acteam.vocago.presentation.screen.main.novel.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
                mobile = 160,
                tabletPortrait = 180,
                tabletLandscape = 180// fix sau
            )
        )
        .aspectRatio(2f / 3f)
        .clip(RoundedCornerShape(16.dp))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                responsiveDP(
                    mobile = 8,
                    tabletPortrait = 12,
                    tabletLandscape = 12
                )
            )
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
            .clickable {
                onClick(novel._id)
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Enhanced image container with subtle gradient overlay
            Box {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(novel.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Novel Cover",
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = imageModifier
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                    RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                strokeWidth = 3.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    error = {
                        Image(
                            painter = painterResource(id = R.drawable.capy_vi),
                            contentDescription = "Novel Cover Error",
                            modifier = imageModifier,
                            contentScale = ContentScale.Crop
                        )
                    },
                )

                // Subtle gradient overlay on image
                Box(
                    modifier = imageModifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        )
                )
            }

            Spacer(
                modifier = Modifier.width(
                    responsiveDP(
                        mobile = 16,
                        tabletPortrait = 20,
                        tabletLandscape = 24
                    )
                )
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Enhanced title with better typography
                Text(
                    text = novel.fictionTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(
                        responsiveDP(
                            mobile = 6,
                            tabletPortrait = 8,
                            tabletLandscape = 10
                        )
                    )
                )

                // Enhanced author with chip-like background
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = novel.author,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(
                        responsiveDP(
                            mobile = 8,
                            tabletPortrait = 10,
                            tabletLandscape = 12
                        )
                    )
                )

                // Enhanced chapter count with icon-like styling
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${novel.totalChapters}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = responsiveSP(
                                    mobile = 16,
                                    tabletPortrait = 18,
                                    tabletLandscape = 20
                                ),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = stringResource(id = R.string.chapters),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                        )
                    )
                }

                Spacer(
                    modifier = Modifier.height(
                        responsiveDP(
                            mobile = 8,
                            tabletPortrait = 10,
                            tabletLandscape = 12
                        )
                    )
                )

                // Enhanced date information with better spacing
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.createdAt),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = ": ${DateDisplayHelper.formatDateString(novel.createdAt)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(0.8f)
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.updatedAt),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = ": ${DateDisplayHelper.formatDateString(novel.updatedAt)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary.copy(0.8f),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}