package com.acteam.vocago.presentation.screen.user.alarm.component

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun TimePickerDialogSample(
    initialHour: Int = 5,
    initialMinute: Int = 30,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val dialog = TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                onTimeSelected(hour, minute)
            },
            initialHour,
            initialMinute,
            true // 24-hour view
        )

        dialog.setOnCancelListener {
            onDismiss()
        }

        dialog.show()
    }
}

