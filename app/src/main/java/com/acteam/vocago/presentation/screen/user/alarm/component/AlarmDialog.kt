package com.acteam.vocago.presentation.screen.user.alarm.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.user.alarm.AlarmViewModel
import java.util.UUID

sealed class DialogMode {
    data object ADD : DialogMode()
    data object EDIT : DialogMode()
}

@Composable
fun AlarmDialog(
    onDismiss: () -> Unit,
    onSave: (Alarm) -> Unit,
    viewModel: AlarmViewModel,
    mode: DialogMode = DialogMode.ADD,
) {

    val alarm by viewModel.alarmUiState.collectAsState()
    var showTimePicker by remember { mutableStateOf(false) }
    var hour by remember { mutableIntStateOf(0) }
    var minute by remember { mutableIntStateOf(0) }
    var enabled by remember { mutableStateOf(false) }
    var label by remember { mutableStateOf("") }
    var isLoop by remember { mutableStateOf(false) }

    LaunchedEffect(alarm) {
        if (alarm is UIState.UISuccess) {
            val alarmData = (alarm as UIState.UISuccess<Alarm>).data
            hour = alarmData.hour
            minute = alarmData.minute
            enabled = alarmData.enabled
            label = alarmData.label
        }
    }

    if (showTimePicker && alarm is UIState.UISuccess) {
        val alarmData = (alarm as UIState.UISuccess<Alarm>).data
        TimePickerDialogSample(
            initialHour = alarmData.hour,
            initialMinute = alarmData.minute,
            onTimeSelected = { h, m ->
                hour = h
                minute = m
                showTimePicker = false
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }



    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    Alarm(
                        id = if (mode is DialogMode.ADD) UUID.randomUUID()
                            .toString() else (alarm as UIState.UISuccess<Alarm>).data.id,
                        hour = hour,
                        minute = minute,
                        enabled = enabled,
                        label = label,
                        isLoop = isLoop
                    )
                )
            }) {
                Text(stringResource(R.string.btn_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel))
            }
        },
        title = {
            if (mode is DialogMode.ADD) Text(stringResource(R.string.title_add_alarm)) else Text(
                stringResource(R.string.title_edit_alarm)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showTimePicker = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "%02d:%02d".format(hour, minute),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }

                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text(stringResource(R.string.title_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        stringResource(R.string.title_enable_alarm),
                        modifier = Modifier.weight(1f)
                    )
                    Switch(checked = enabled, onCheckedChange = { enabled = it })
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.title_loop_alarm), modifier = Modifier.weight(1f))
                    Switch(checked = isLoop, onCheckedChange = { isLoop = it })
                }
            }
        }
    )
}