package com.acteam.vocago.domain.model

import androidx.compose.ui.graphics.Color

data class NovelTheme(
    val name: String,
    val backgroundColor: Color,
    val textColor: Color,
) {
    companion object {
        val Light = NovelTheme(
            name = "Light",
            backgroundColor = Color(0xFFFFFFFF),
            textColor = Color(0xFF000000)
        )

        val Dark = NovelTheme(
            name = "Dark",
            backgroundColor = Color(0xFF121212),
            textColor = Color(0xFFE0E0E0)
        )

        val Sepia = NovelTheme(
            name = "Sepia",
            backgroundColor = Color(0xFFF4ECD8),
            textColor = Color(0xFF5B4636)
        )

        val Midnight = NovelTheme(
            name = "Midnight",
            backgroundColor = Color(0xFF0D1117),
            textColor = Color(0xFF58A6FF)
        )

        val Forest = NovelTheme(
            name = "Forest",
            backgroundColor = Color(0xFFE6F2E6),
            textColor = Color(0xFF1B5E20)
        )

        val Ocean = NovelTheme(
            name = "Ocean",
            backgroundColor = Color(0xFFE0F7FA),
            textColor = Color(0xFF006064)
        )

        val Sand = NovelTheme(
            name = "Sand",
            backgroundColor = Color(0xFFFFF8E1),
            textColor = Color(0xFF6D4C41)
        )

        val Lavender = NovelTheme(
            name = "Lavender",
            backgroundColor = Color(0xFFF3E5F5),
            textColor = Color(0xFF4A148C)
        )

        val NightBlue = NovelTheme(
            name = "Night Blue",
            backgroundColor = Color(0xFF001F3F),
            textColor = Color(0xFF7FDBFF)
        )

        val DimGray = NovelTheme(
            name = "Dim Gray",
            backgroundColor = Color(0xFF2E2E2E),
            textColor = Color(0xFFD3D3D3)
        )

        val Rose = NovelTheme(
            name = "Rose",
            backgroundColor = Color(0xFFFFEBEE),
            textColor = Color(0xFF880E4F)
        )

        val CoolMint = NovelTheme(
            name = "Cool Mint",
            backgroundColor = Color(0xFFE0F2F1),
            textColor = Color(0xFF004D40)
        )

        val allThemes = listOf(
            Light, Dark, Sepia, Midnight, Forest, Ocean,
            Sand, Lavender, NightBlue, DimGray, Rose, CoolMint
        )
    }
}
