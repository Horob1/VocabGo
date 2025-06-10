package com.acteam.vocago.presentation.screen.newshistory.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun NewsHistoryTopBar(
    modifier: Modifier = Modifier,
    isBookmark: Boolean,
    onToggleBookmark: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    val verticalPadding = responsiveDP(mobile = 8, tabletPortrait = 12, tabletLandscape = 16)
    val superTitleFontSize = responsiveSP(
        mobile = 20,
        tabletPortrait = 24,
        tabletLandscape = 28
    )
    Row(
        modifier = modifier
            .padding(
                vertical = verticalPadding
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


        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.world_news),
                contentDescription = "Logo news",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = if (isBookmark) stringResource(R.string.title_bookmark) else stringResource(R.string.title_history),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = superTitleFontSize,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                ),
            )
        }

        IconButton(
            onClick = {
                onToggleBookmark(!isBookmark)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Bookmark",
                tint = if (isBookmark) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
