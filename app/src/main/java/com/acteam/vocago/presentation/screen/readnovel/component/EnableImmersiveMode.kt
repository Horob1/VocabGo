package com.acteam.vocago.presentation.screen.readnovel.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.acteam.vocago.presentation.ui.theme.findActivity

@Composable
fun EnableImmersiveMode(
    enabled: Boolean,
) {
    val view = LocalView.current

    DisposableEffect(enabled) {
        val window = view.context.findActivity().window
        val controller = WindowInsetsControllerCompat(window, view)


        // Tắt status bar khi enabled
        if (enabled) {
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            controller.show(WindowInsetsCompat.Type.statusBars())
        }
    }
}
