package com.acteam.vocago.presentation.screen.user.alarm

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.user.alarm.component.AlarmDialog
import com.acteam.vocago.presentation.screen.user.alarm.component.AlarmItem
import com.acteam.vocago.presentation.screen.user.alarm.component.DialogMode
import com.acteam.vocago.presentation.screen.user.common.UserTopBar

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val alarmList by viewModel.alarmListUiState.collectAsState()
    var isShowDialog by remember { mutableStateOf(false) }
    var dialogMode by remember { mutableStateOf<DialogMode>(DialogMode.ADD) }
    var isShowDeleteDialog by remember { mutableStateOf(false) }
    val chosenAlarm by viewModel.alarmUiState.collectAsState()
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Do nothing
        } else {
            Toast.makeText(
                context,
                "Vui lòng cấp quyền thông báo để xử dụng cn này",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                UserTopBar(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    titleId = R.string.title_alarm
                )
            }
            when (alarmList) {
                is UIState.UISuccess<List<Alarm>> -> {
                    val alarms = (alarmList as UIState.UISuccess<List<Alarm>>).data
                    items(alarms.size, key = { alarms[it].id }) {
                        val alarm = alarms[it]
                        val dismissState = rememberSwipeToDismissBoxState(
                            positionalThreshold = { totalDistance -> totalDistance * 0.3f },
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.findAlarm(alarm.id)
                                    isShowDeleteDialog = true
                                    false
                                } else {
                                    true
                                }
                            }
                        )
                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            enableDismissFromEndToStart = true,
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Red, shape = RoundedCornerShape(16.dp))
                                        .padding(16.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }
                            },
                            content = {
                                AlarmItem(
                                    alarm = alarm,
                                    onToggle = {
                                        viewModel.saveAlarm(
                                            alarm.copy(enabled = !alarm.enabled),
                                            DialogMode.EDIT
                                        )
                                    },
                                    onClick = {
                                        viewModel.findAlarm(alarm.id)
                                        dialogMode = DialogMode.EDIT
                                        isShowDialog = true
                                    }
                                )
                            }
                        )
                    }
                }

                is UIState.UILoading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is UIState.UIError -> {}
            }

        }

        FloatingActionButton(
            onClick = {
                //toast cannot create if had 4 alarm
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    activity?.let {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    if (
                        (alarmList as? UIState.UISuccess<List<Alarm>>)?.data?.size == 4
                    ) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.alert_cannot_create_more_alarm),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return@FloatingActionButton
                    }
                    dialogMode = DialogMode.ADD
                    viewModel.setDefaultAlarm()
                    isShowDialog = true
                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Alarm")
        }
    }
    if (isShowDialog)
        AlarmDialog(
            onDismiss = { isShowDialog = false },
            onSave = {
                isShowDialog = false
                viewModel.saveAlarm(it, dialogMode)
            },
            viewModel = viewModel,
            mode = dialogMode
        )

    if (isShowDeleteDialog && chosenAlarm is UIState.UISuccess<Alarm>) {
        AlertDialog(
            onDismissRequest = { isShowDeleteDialog = false },
            title = { Text(stringResource(R.string.title_sure_to_delete)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAlarm((chosenAlarm as UIState.UISuccess<Alarm>).data)
                    isShowDeleteDialog = false
                }) {
                    Text(stringResource(R.string.btn_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { isShowDeleteDialog = false }) {
                    Text(stringResource(R.string.btn_cancel))
                }
            }
        )
    }
}
