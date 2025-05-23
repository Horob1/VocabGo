package com.acteam.vocago.presentation.screen.auth.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.acteam.vocago.utils.responsiveDP

@Composable
fun AuthImageCard(
    @DrawableRes image: Int,
    width: Float = 0.6f,
    shape: Shape = RoundedCornerShape(responsiveDP(40, 24, 24))
) {

    Box(
        modifier = Modifier
            .fillMaxWidth(width)
            .aspectRatio(
                ratio = 1f
            )
            .background(MaterialTheme.colorScheme.background)
            .shadow(8.dp, shape = shape)
            .clip(shape)
            .padding(8.dp)
            .clip(shape)

    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Auth Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

