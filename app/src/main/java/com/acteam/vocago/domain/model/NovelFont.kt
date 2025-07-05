package com.acteam.vocago.domain.model

import androidx.compose.ui.text.font.FontFamily
import com.acteam.vocago.presentation.ui.theme.IBMFontFamily
import com.acteam.vocago.presentation.ui.theme.JetBrainsMonoFontFamily
import com.acteam.vocago.presentation.ui.theme.LiterataFontFamily
import com.acteam.vocago.presentation.ui.theme.NotoSerifFontFamily
import com.acteam.vocago.presentation.ui.theme.RobotoSerifFontFamily

data class NovelFont(val name: String, val fontFamily: FontFamily) {
    companion object {
        val RobotoSerif = NovelFont("Roboto Serif", RobotoSerifFontFamily)
        val NotoSerif = NovelFont("Noto Serif", NotoSerifFontFamily)
        val Literata = NovelFont("Literata", LiterataFontFamily)

        val JetBrainsMono = NovelFont("JetBrains Mono", JetBrainsMonoFontFamily)

        val IBM = NovelFont("IBM Plex Mono", IBMFontFamily)

        val allFonts = listOf(
            RobotoSerif,
            NotoSerif,
            Literata,
            JetBrainsMono,
            IBM
        )

        fun getFontByName(name: String): NovelFont {
            return allFonts.find { it.name == name } ?: allFonts[0]
        }
    }
}