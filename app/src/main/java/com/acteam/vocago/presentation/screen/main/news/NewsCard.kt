package com.acteam.vocago.presentation.screen.main.news

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.data.local.entity.NewsEntity
import com.acteam.vocago.utils.DateDisplayHelper
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun BigNewsCard(
    modifier: Modifier = Modifier,
    news: NewsEntity,
    onItemClick: () -> Unit,
) {

    val loadingErrorPainter = painterResource(id = R.drawable.loading_news)


    val keywordStats by remember(news.words) {
        derivedStateOf {
            listOf(
                Triple("A1", news.words.a1, Color(0xFF81C784)), // pastel green
                Triple("A2", news.words.a2, Color(0xFF64B5F6)), // pastel blue
                Triple("B1", news.words.b1, Color(0xFFFFF176)), // pastel yellow
                Triple("B2", news.words.b2, Color(0xFFEF9A9A))  // pastel red
            )
        }
    }

    val totalWords by remember(news.words) {
        derivedStateOf {
            news.words.a1 + news.words.a2 + news.words.b1 + news.words.b2
        }
    }


    val formattedDate by remember(news.createdAt) {
        derivedStateOf { DateDisplayHelper.formatDateString(news.createdAt) }
    }

    ElevatedCard(
        onClick = onItemClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        responsiveDP(
                            mobile = 200,
                            tabletPortrait = 300,
                            tabletLandscape = 350
                        )
                    )
                    .clip(MaterialTheme.shapes.medium)
            ) {
                val context = LocalContext.current

                val imageRequest = remember(news.coverImage) {
                    ImageRequest.Builder(context)
                        .data(news.coverImage)
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED) // Good: Caching enabled
                        .memoryCachePolicy(CachePolicy.ENABLED) // Good: Caching enabled
                        .build()
                }
                SubcomposeAsyncImage(
                    model = imageRequest,
                    contentDescription = "News Image",
                    loading = {
                        Image(
                            painter = loadingErrorPainter,
                            contentDescription = "Loading",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    error = {
                        Image(
                            painter = loadingErrorPainter,
                            contentDescription = "Error Loading Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = news.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.surface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Image(
                            painter = painterResource(id = R.drawable.vi_flag),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                if (totalWords > 0) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .height(6.dp)
                    ) {
                        keywordStats.forEach { (_, count, color) ->
                            val ratio = if (count > 0) count.toFloat() / totalWords else 0f
                            if (ratio > 0f) { // Only add a Box if it will be visible
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(ratio)
                                        .background(color)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = responsiveSP( // Assuming responsiveSP is lightweight
                        mobile = 18,
                        tabletPortrait = 20,
                        tabletLandscape = 22
                    )
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        DateDisplayHelper.ConvertToTimeAgo(
                            dateTime = news.createdAt,

                            )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RemoveRedEye,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = news.views.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = formattedDate, // Use pre-calculated value
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

}

@Composable
fun SmallNewsCard(
    modifier: Modifier = Modifier,
    news: NewsEntity,
    onItemClick: () -> Unit = {},
) {
    val loadingErrorPainter = painterResource(id = R.drawable.loading_news)

    val keywordStats by remember(news.words) {
        derivedStateOf {
            listOf(
                Triple("A1", news.words.a1, Color(0xFF81C784)),
                Triple("A2", news.words.a2, Color(0xFF64B5F6)),
                Triple("B1", news.words.b1, Color(0xFFFFF176)),
                Triple("B2", news.words.b2, Color(0xFFEF9A9A))
            )
        }
    }
    val totalWords by remember(news.words) {
        derivedStateOf {
            news.words.a1 + news.words.a2 + news.words.b1 + news.words.b2
        }
    }

    val formattedDate by remember(news.createdAt) {
        derivedStateOf { DateDisplayHelper.formatDateString(news.createdAt) }
    }


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
                val imageRequest = remember(news.coverImage) {
                    ImageRequest.Builder(context)
                        .data(news.coverImage)
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
                        text = news.title,
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
                        // Optimization 4: Improved logic for keyword stats bar ratio
                        if (totalWords > 0) {
                            Row(
                                modifier = Modifier
                                    .weight(1f) // Ensure this row takes available space before timeAgo
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            ) {
                                keywordStats.forEach { (_, count, color) ->
                                    val ratio =
                                        if (count > 0) count.toFloat() / totalWords else 0f
                                    if (ratio > 0f) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .weight(ratio)
                                                .background(color)
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(Modifier.weight(1f)) // Maintain layout if bar is not shown
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            DateDisplayHelper.ConvertToTimeAgo(
                                dateTime = news.createdAt,
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = news.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.vi_flag),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RemoveRedEye,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = news.views.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = formattedDate, // Use pre-calculated value
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}