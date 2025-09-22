package com.acteam.vocago.presentation.screen.user.profile.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.acteam.vocago.R
import com.acteam.vocago.data.model.GetTwoFAQrCodeResponse
import com.acteam.vocago.presentation.screen.auth.common.RowOtpInputField
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.main.toeictest.component.ErrorDisplay

@Composable
fun TwoFactorSetupDialog(
    isVisible: Boolean,
    otp: String,
    qrCodeState: UIState<Unit>,
    qrCodeData: GetTwoFAQrCodeResponse?,
    qrCodeBitmap: Bitmap?,
    onDismiss: () -> Unit,
    onVerify: (String) -> Unit,
    onGenerateQRCode: (String) -> Unit,
    onOtpChange: (String) -> Unit
) {
    if (!isVisible) return

    LaunchedEffect(qrCodeData) {
        qrCodeData?.let { data ->
            val qrCodeContent = data.qrcode
            if (qrCodeContent.isNotEmpty()) {
                onGenerateQRCode(qrCodeContent)
            }
        }
    }

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(R.string.desc_two_factor_setup),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // QR Code
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(
                                Color.White,
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (qrCodeState) {
                            is UIState.UILoading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            is UIState.UISuccess -> {
                                qrCodeBitmap?.let { bitmap ->
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "QR Code",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } ?: LoadingSurface()
                            }

                            is UIState.UIError -> {
                                ErrorDisplay()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.desc_two_factor_setup_2),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RowOtpInputField(
                        otp = otp,
                        onOtpChange = onOtpChange,
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    ModernButton(
                        text = stringResource(R.string.btn_enable),
                        icon = Icons.Default.Lock,
                        onClick = {
                            if (otp.length == 6) {
                                onVerify(otp)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }

    }
}
