package com.acteam.vocago.presentation.screen.user.profile.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.user.profile.ProfileViewModel

@Composable
fun TwoFactorAuthenticatorCard(
    viewModel: ProfileViewModel,
) {
    var showQRDialog by remember { mutableStateOf(false) }
    val qrCodeState by viewModel.qrCodeState.collectAsState()
    val qrCodeData by viewModel.qrCodeData.collectAsState()
    val qrCodeBitmap by viewModel.qrCodeBitmap.collectAsState()
    val otpState by viewModel.otpState.collectAsState()
    val setup2FAState by viewModel.setup2FAState.collectAsState()
    val disable2FAState by viewModel.disable2FAState.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val errorMessage = stringResource(R.string.setup_2fa_failed)
    val disableErrorMessage = stringResource(R.string.disable_2fa_failed)
    LaunchedEffect(setup2FAState) {
        when (setup2FAState) {
            is UIState.UISuccess -> {
                showQRDialog = false
                viewModel.clearQRCodeData()
            }

            is UIState.UIError -> {
                Toast.makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            else -> {}
        }
    }
    LaunchedEffect(disable2FAState) {
        when (disable2FAState) {
            is UIState.UISuccess -> {
                viewModel.clearQRCodeData()
            }

            is UIState.UIError -> {
                Toast.makeText(context, disableErrorMessage, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp, MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.5f
                ), RoundedCornerShape(12.dp)
            ),
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
                imageVector = Icons.Default.PhoneIphone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            text = stringResource(R.string.two_factor_authentication),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )

        BorderedModernButton(
            text = if (userProfile?.require2FA == true) stringResource(R.string.btn_disable) else stringResource(
                R.string.btn_enable
            ),
            icon = if (userProfile?.require2FA == true) Icons.Default.LockOpen else Icons.Default.Lock,
            onClick = {
                if (userProfile?.require2FA != true) {
                    viewModel.getTwoFAQrCode()
                    showQRDialog = true
                } else {
                    viewModel.disableTwoFA()
                }
            },
            modifier = Modifier.padding(end = 16.dp),
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            borderColor = if (userProfile?.require2FA == true)
                MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
            else
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    }

    if (showQRDialog) {
        TwoFactorSetupDialog(
            isVisible = showQRDialog,
            otp = otpState.otp,
            qrCodeState = qrCodeState,
            qrCodeData = qrCodeData,
            qrCodeBitmap = qrCodeBitmap,
            onDismiss = {
                showQRDialog = false
                viewModel.clearQRCodeData()
            },
            onVerify = { code ->
                viewModel.setOtpValue(code)
                viewModel.setupTwoFA()
            },
            onGenerateQRCode = { data ->
                viewModel.decodeBase64ToBitmap(data)
            },
            onOtpChange = {
                viewModel.setOtpValue(it)
            }
        )
    }

}

@Composable
fun BorderedModernButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    borderColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .background(
                containerColor,
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                borderColor,
                RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )
        }
    }
}