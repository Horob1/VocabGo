package com.acteam.vocago.presentation.screen.auth.resetpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.BackButton
import com.acteam.vocago.presentation.screen.auth.common.CountdownTimer
import com.acteam.vocago.presentation.screen.auth.common.OTPInputField
import com.acteam.vocago.presentation.screen.auth.resetpassword.component.ResetPasswordForm
import com.acteam.vocago.utils.safeClickable

@Composable
fun ResetPasswordScreen(
    onBackClick: () -> Unit,
    onResendOtp: () -> Unit,
    onChangeClick: () -> Unit
) {
    var email by remember { mutableStateOf("vancong@gmail.com") }
    var otp by remember { mutableStateOf("") }
    var isTimerReset by remember { mutableStateOf(false) }
    var timerKey by remember { mutableStateOf(0) }
    val resetTimer = {
        isTimerReset = !isTimerReset
        timerKey += 1
        onResendOtp()
    }
    val scrollState = rememberScrollState()
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                .fillMaxSize()
                .verticalScroll(
                    scrollState
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    onClick = onBackClick,
                )

                Text(
                    text = stringResource(R.string.text_reset_password),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )

                Spacer(modifier = Modifier.width(40.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AuthImageCard(R.drawable.resetpassword, width = 0.6f)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CountdownTimer(
                    initialTime = 60,
                    key = timerKey,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OTPInputField(
                    otp = otp,
                    onOtpChange = { newOtp ->
                        otp = newOtp
                    },
                    onOtpCompleted = { otp ->

                    }
                )
            }
            Text(
                text = "${stringResource(R.string.text_send_otp)} $email",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
            ResetPasswordForm()
            Text(
                text = stringResource(R.string.text_resend_email),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier

                    .safeClickable(
                        key = "btn_resend_email_forgot_password",
                        onClick = {
                            resetTimer()
                        }
                    )
                    .padding(8.dp)
                    .align(Alignment.End)
            )
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
                onClick = {
                    onChangeClick()
                },
            ) {
                Text(
                    text = stringResource(R.string.btn_save_change).uppercase(),
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }
    }
}
