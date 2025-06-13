package com.acteam.vocago.presentation.screen.user.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun UserTopBar(
    onBackClick: () -> Unit,
    @StringRes titleId: Int,
) {
    Row(
        Modifier
            .padding(
                vertical = responsiveDP(
                    mobile = 8,
                    tabletPortrait = 12,
                    tabletLandscape = 16
                )
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onBackClick

        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary
            )
        }


        Text(
            stringResource(titleId),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = responsiveSP(
                    mobile = 20,
                    tabletPortrait = 24,
                    tabletLandscape = 28
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            ),
        )

        Spacer(
            modifier = Modifier
                .padding(
                    8.dp
                )
                .width(20.dp)
        )
    }

}