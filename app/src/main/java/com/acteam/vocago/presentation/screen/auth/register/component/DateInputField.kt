package com.acteam.vocago.presentation.screen.auth.register.component

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.register.RegisterViewModel
import com.acteam.vocago.utils.responsiveSP
import java.util.Calendar

@Composable
fun DateInputField(
    viewModel: RegisterViewModel
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedDate by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val textFieldFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)

    if (showDialog) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val calendarPicked = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                selectedDate = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                val isoFormat =
                    java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val isoDateString = isoFormat.format(calendarPicked.time)
                viewModel.setDateOfBirth(isoDateString)
                showDialog = false // Close the dialog after selection
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
                showDialog = false // Keep dialog closed if Cancel is clicked
            }
        }.show()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .clickable { showDialog = true }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (selectedDate.isNotEmpty()) selectedDate else stringResource(R.string.input_enter_dob),
                    color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onSurface else Color.Gray,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = textFieldFontSize
                )
            }
        }
    }
}
