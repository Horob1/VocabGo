package com.acteam.vocago.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.safeClickable(
    key: String,
    threshold: Long = 800L,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    this.then(
        Modifier.clickable(enabled = enabled) {
            if (ClickGuard.canClick(key, threshold)) {
                onClick()
            }
        }
    )
}
