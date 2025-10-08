package com.acteam.vocago.presentation.screen.user.alarm.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.user.alarm.AlarmViewModel

sealed class DialogMode {
    data object ADD : DialogMode()
    data object EDIT : DialogMode()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDialog(
    onDismiss: () -> Unit,
    onSave: (Alarm) -> Unit,
    viewModel: AlarmViewModel,
    mode: DialogMode = DialogMode.ADD,
) {
    val alarm by viewModel.alarmUiState.collectAsState()

    var enabled by remember { mutableStateOf(false) }
    var label by remember { mutableStateOf("") }
    var isLoop by remember { mutableStateOf(false) }

    // Khi edit, nạp dữ liệu từ ViewModel
    LaunchedEffect(alarm) {
        if (mode is DialogMode.EDIT && alarm is UIState.UISuccess) {
            val alarmData = (alarm as UIState.UISuccess<Alarm>).data
            enabled = alarmData.enabled
            label = alarmData.label
            isLoop = alarmData.isLoop
        }
    }

    // Nếu ADD => tạo dữ liệu mặc định, nếu EDIT => lấy từ alarmUiState
    val alarmData = if (mode is DialogMode.EDIT && alarm is UIState.UISuccess) {
        (alarm as UIState.UISuccess<Alarm>).data
    } else {
        Alarm(
            id = 0,
            hour = 7,
            minute = 0,
            enabled = true,
            label = "",
            isLoop = false
        )
    }

    val timePickerState = rememberTimePickerState(
        initialHour = alarmData.hour,
        initialMinute = alarmData.minute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    Alarm(
                        id = if (mode is DialogMode.ADD) 0 else alarmData.id,
                        hour = timePickerState.hour,
                        minute = timePickerState.minute,
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
            Text(
                if (mode is DialogMode.ADD)
                    stringResource(R.string.title_add_alarm)
                else
                    stringResource(R.string.title_edit_alarm)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier.fillMaxWidth()
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
                    Text(
                        stringResource(R.string.title_loop_alarm),
                        modifier = Modifier.weight(1f)
                    )
                    Switch(checked = isLoop, onCheckedChange = { isLoop = it })
                }
            }
        }
    )
}
