package com.acteam.vocago.presentation.screen.welcome.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun OnBoardingImageCard(
    @DrawableRes
    image: Int,
    maxWith: Float = 0.9f,
    maxHigh: Float = 0.7f
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(maxWith)
            .fillMaxHeight(maxHigh)
            .background(MaterialTheme.colorScheme.background)
            .shadow(8.dp, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .padding(8.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = image),
            contentDescription = "Pager Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}