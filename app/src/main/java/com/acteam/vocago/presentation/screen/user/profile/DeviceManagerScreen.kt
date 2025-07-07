package com.acteam.vocago.presentation.screen.user.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.data.model.DeviceDTO
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.main.toeictest.component.ErrorDisplay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DeviceManagementScreen(
    viewModel: ProfileViewModel,
    navController: NavController
) {
    val deviceListState by viewModel.deviceListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getDevicesList()
    }

    when (deviceListState) {
        is UIState.UILoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UIState.UISuccess -> {
            val devices = (deviceListState as UIState.UISuccess<List<DeviceDTO>>).data
            DeviceListContent(
                devices = devices,
                navController = navController,
                onDeleteDevice = { device ->
                    viewModel.logoutUserDevice(
                        device._id
                    ) {
                        viewModel.getDevicesList()
                    }
                }
            )
        }

        is UIState.UIError -> {
            ErrorDisplay()
        }
    }
}

@Composable
fun DeviceListContent(
    devices: List<DeviceDTO>,
    navController: NavController,
    onDeleteDevice: (DeviceDTO) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deviceToDelete by remember { mutableStateOf<DeviceDTO?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.text_device_management),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(devices) { device ->
                DeviceItem(
                    device = device,
                    onDeleteClick = {
                        deviceToDelete = device
                        showDeleteDialog = true
                    }
                )
            }
        }

        if (showDeleteDialog && deviceToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(stringResource(R.string.text_delete_device)) },
                text = { Text(stringResource(R.string.text_delete_device_confirm)) },
                confirmButton = {
                    TextButton(onClick = {
                        onDeleteDevice(deviceToDelete!!)
                        showDeleteDialog = false
                    }) {
                        Text(stringResource(R.string.text_delete), color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(stringResource(R.string.btn_cancel))
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DeviceItem(
    device: DeviceDTO,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ID: ${device._id.truncateId()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(R.string.text_last_login)}: ${formatDate(device.updatedAt)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onDeleteClick,
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Xóa thiết bị")
            }
        }
    }
}

fun String.truncateId(): String {
    return if (this.length > 10) this.take(10) + "..." else this
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}
