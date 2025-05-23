package com.acteam.vocago.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GTranslate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class FeatureBarItem(
    val onItemClick: () -> Unit,
    val icon: ImageVector,
    val desc: String,
    val color: androidx.compose.ui.graphics.Color? = null,
) {

    data class TranslateCam(
        val onClick: () -> Unit,
    ) : FeatureBarItem(
        onItemClick = {
            onClick()
        },
        icon = Icons.Default.CameraAlt,
        desc = "Camera"
    )

    data class BookMark(
        val onClick: () -> Unit,
    ) : FeatureBarItem(
        onItemClick = {
            onClick()
        },
        icon = Icons.Default.Favorite,
        desc = "Favorite",
        color = androidx.compose.ui.graphics.Color.Red
    )

    data class History(
        val onClick: () -> Unit,
    ) : FeatureBarItem(
        onItemClick = {
            onClick()
        },
        icon = Icons.Default.History,
        desc = "History"
    )

    data class Dictionary(
        val onClick: () -> Unit,
    ) : FeatureBarItem(
        onItemClick = {
            onClick()
        },
        icon = Icons.Default.GTranslate,
        desc = "Dictionary",
    )

    data class Setting(
        val onClick: () -> Unit,
    ) : FeatureBarItem(
        onItemClick = {
            onClick()
        },
        icon = Icons.Default.Settings,
        desc = "Setting"
    )

}