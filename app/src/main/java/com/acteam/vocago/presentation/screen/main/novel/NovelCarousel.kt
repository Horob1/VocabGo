package com.acteam.vocago.presentation.screen.main.novel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun NovelCarousel(
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        "https://lh3.googleusercontent.com/pw/AP1GczOBBMpnlwmU1GA7TI-h_DpHyDQu2Nb7m2B8tm4ryQQL3XL-cO93XgNQK31C1cY0SsGRc69NeLQvtLlgoA7_qq5C2umsl1YQrFTc0UCdDL85IHE5DBTvSwpdtY4sZo1GsIVpvI8vPFPjra_ped343sV2=w215-h322-s-no-gm?authuser=4",
        "https://lh3.googleusercontent.com/pw/AP1GczOBBMpnlwmU1GA7TI-h_DpHyDQu2Nb7m2B8tm4ryQQL3XL-cO93XgNQK31C1cY0SsGRc69NeLQvtLlgoA7_qq5C2umsl1YQrFTc0UCdDL85IHE5DBTvSwpdtY4sZo1GsIVpvI8vPFPjra_ped343sV2=w215-h322-s-no-gm?authuser=4",
        "https://lh3.googleusercontent.com/pw/AP1GczOBBMpnlwmU1GA7TI-h_DpHyDQu2Nb7m2B8tm4ryQQL3XL-cO93XgNQK31C1cY0SsGRc69NeLQvtLlgoA7_qq5C2umsl1YQrFTc0UCdDL85IHE5DBTvSwpdtY4sZo1GsIVpvI8vPFPjra_ped343sV2=w215-h322-s-no-gm?authuser=4"
    )
    val realSize = items.size
    val startIndex = Int.MAX_VALUE / 2 // Start from middle to allow left/right scroll

    val pageState = rememberPagerState(initialPage = startIndex) {
        Int.MAX_VALUE
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000L)
            val nextPage = pageState.currentPage + 1
            pageState.animateScrollToPage(nextPage)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        HorizontalPager(
            state = pageState,
            contentPadding = PaddingValues(
                horizontal = responsiveDP(
                    mobile = 80,
                    tabletPortrait = 260,
                    tabletLandscape = 0
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) { index ->
            val realIndex = index % realSize

            Box(
                modifier = Modifier
                    .fillMaxSize() // Page vẫn chiếm full width
                    .wrapContentSize(Alignment.Center) // Đặt item ở giữa
            ) {
                NovelCarouselItem(
                    index = index,
                    pageState = pageState,
                    url = items[realIndex],
                )
            }
        }
        val currentPage = pageState.currentPage % realSize

        Spacer(
            modifier = Modifier.size(
                responsiveDP(
                    mobile = 12,
                    tabletPortrait = 16,
                    tabletLandscape = 0
                )
            )
        )

        Text(
            "The Fellowship Of The Ring $currentPage",
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = responsiveSP(
                    mobile = 16,
                    tabletPortrait = 18,
                    tabletLandscape = 0
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(
                responsiveDP(
                    mobile = 240,
                    tabletPortrait = 320,
                    tabletLandscape = 0
                )
            )
        )

        Spacer(
            modifier = Modifier.size(
                responsiveDP(
                    mobile = 4,
                    tabletPortrait = 8,
                    tabletLandscape = 0
                )
            )
        )

        Text(
            "J.R.R Token", style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    0.7f
                ),
                textAlign = TextAlign.Center
            )
        )

        Spacer(
            modifier = Modifier.size(
                responsiveDP(
                    mobile = 12,
                    tabletPortrait = 16,
                    tabletLandscape = 0
                )
            )
        )

        Button(
            onClick = {},
            modifier = Modifier
                .clip(
                    MaterialTheme.shapes.medium
                )
                .width(
                    responsiveDP(
                        mobile = 240,
                        tabletPortrait = 320,
                        tabletLandscape = 0
                    )
                )
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                )

        ) {
            Text("Read Now")
        }

        Spacer(
            modifier = Modifier.size(
                responsiveDP(
                    mobile = 12,
                    tabletPortrait = 20,
                    tabletLandscape = 0
                )
            )
        )
    }
}

@Composable
fun TabletCarousel() {

    val items = listOf(
        "https://lh3.googleusercontent.com/pw/AP1GczOBBMpnlwmU1GA7TI-h_DpHyDQu2Nb7m2B8tm4ryQQL3XL-cO93XgNQK31C1cY0SsGRc69NeLQvtLlgoA7_qq5C2umsl1YQrFTc0UCdDL85IHE5DBTvSwpdtY4sZo1GsIVpvI8vPFPjra_ped343sV2=w215-h322-s-no-gm?authuser=4",
        "https://lh3.googleusercontent.com/pw/AP1GczOBBMpnlwmU1GA7TI-h_DpHyDQu2Nb7m2B8tm4ryQQL3XL-cO93XgNQK31C1cY0SsGRc69NeLQvtLlgoA7_qq5C2umsl1YQrFTc0UCdDL85IHE5DBTvSwpdtY4sZo1GsIVpvI8vPFPjra_ped343sV2=w215-h322-s-no-gm?authuser=4",
        "https://lh3.googleusercontent.com/pw/AP1GczOBBMpnlwmU1GA7TI-h_DpHyDQu2Nb7m2B8tm4ryQQL3XL-cO93XgNQK31C1cY0SsGRc69NeLQvtLlgoA7_qq5C2umsl1YQrFTc0UCdDL85IHE5DBTvSwpdtY4sZo1GsIVpvI8vPFPjra_ped343sV2=w215-h322-s-no-gm?authuser=4"
    )
    val realSize = items.size
    val startIndex = Int.MAX_VALUE / 2 // Start from middle to allow left/right scroll

    val pageState = rememberPagerState(initialPage = startIndex) {
        Int.MAX_VALUE
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000L)
            val nextPage = pageState.currentPage + 1
            pageState.animateScrollToPage(nextPage)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(580.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.novel),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            pageSpacing = 40.dp
        ) { index ->
            val realIndex = index % realSize
            val context = LocalContext.current
            val pageOffset =
                ((pageState.currentPage - index) + pageState.currentPageOffsetFraction).absoluteValue

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imageModifier = Modifier
                    .height(360.dp)
                    .width(240.dp)
                    .clip(RoundedCornerShape(12.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .graphicsLayer {
                            rotationY = -8f // Nghiêng 3D nhẹ
                            shadowElevation = 16f
                            shape = RoundedCornerShape(12.dp)
                            clip = true
                        }
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        )
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(items[realIndex])
                            .crossfade(true)
                            .build(),
                        contentDescription = "Book Image",
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
                                contentDescription = "Error Image",
                                modifier = imageModifier
                            )
                        }
                    )
                }

                // Thông tin sách (Glass style card)
                GlassCard(modifier = Modifier.width(360.dp)) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "The Fellowship of the Ring $realIndex",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.primary
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "J.R.R. Tolkien",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        Button(
                            onClick = { /* TODO */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Read Now")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NovelCarouselItem(
    index: Int,
    pageState: PagerState,
    url: String,
) {
    val pageOffset = (pageState.currentPage - index) + pageState.currentPageOffsetFraction

    val rotationY = lerp(
        start = if (pageOffset < 0) 15f else -15f,
        stop = 0f,
        fraction = (1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
    )

    val imageModifier = Modifier
        .height(
            responsiveDP(
                mobile = 340,
                tabletPortrait = 460,
                tabletLandscape = 0
            )
        )
        .width(
            responsiveDP(
                mobile = 240,
                tabletPortrait = 320,
                tabletLandscape = 0
            )
        )



    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .graphicsLayer {
                scaleX = lerp(0.85f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
                scaleY = lerp(0.85f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
                alpha = lerp(0.5f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
                this.rotationY = rotationY
                cameraDistance = 12 * density
                clip = false
            }
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
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp),
        content = content
    )
}