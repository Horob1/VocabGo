package com.acteam.vocago.presentation.screen.newshistory.component

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.data.local.entity.NewsHistoryEntity
import com.acteam.vocago.utils.DateDisplayHelper
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun NewsHistoryItemCard(
    modifier: Modifier = Modifier,
    newsHistory: NewsHistoryEntity,
    onItemClick: () -> Unit,
) {
    val loadingErrorPainter = painterResource(id = R.drawable.loading_news)

    val rowHeight = responsiveDP(mobile = 80, tabletPortrait = 100, tabletLandscape = 120)

    val imageWidth =
        responsiveDP(mobile = 120, tabletPortrait = 140, tabletLandscape = 140)

    val titleFontSize =
        responsiveSP(mobile = 16, tabletPortrait = 18, tabletLandscape = 20)

    val titleLineHeight =
        responsiveSP(mobile = 18, tabletPortrait = 20, tabletLandscape = 22)


    ElevatedCard(
        onClick = onItemClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val context = LocalContext.current
                val imageRequest = remember(newsHistory.news.coverImage) {
                    ImageRequest.Builder(context)
                        .data(newsHistory.news.coverImage)
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build()
                }
                SubcomposeAsyncImage(
                    model = imageRequest,
                    contentDescription = "News Image",
                    loading = {
                        Image(
                            painter = loadingErrorPainter, // Use optimized painter
                            contentDescription = "Loading",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    error = {
                        Image(
                            painter = loadingErrorPainter, // Use optimized painter
                            contentDescription = "Error Loading Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(height = rowHeight, width = imageWidth) // Use remembered values
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.height(rowHeight), // Use remembered value
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = newsHistory.news.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = titleFontSize, // Use remembered value
                            lineHeight = titleLineHeight // Use remembered value
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (newsHistory.isBookmarked) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(R.string.title_bookmark),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            DateDisplayHelper.ConvertToTimeAgo(
                                dateTime = newsHistory.createdAt,
                            )
                        }

                    }


                }
            }
        }
    }

}