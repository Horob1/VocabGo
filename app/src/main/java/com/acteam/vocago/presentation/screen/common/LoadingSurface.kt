package com.acteam.vocago.presentation.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.acteam.vocago.R

@Composable
fun LoadingSurface(
    picSize: Int = 180,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(picSize.dp),
            contentAlignment = Alignment.Center
        ) {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.loading)
                    .decoderFactory(GifDecoder.Factory())
                    .size(
                        Size(picSize, picSize)
                    )
                    .build(),
                contentDescription = "Loading...",
                modifier = Modifier
                    .size(picSize.dp)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .size(picSize.dp + 6.dp)
                    .align(Alignment.Center),
                strokeWidth = 8.dp,
            )
        }
    }
}
