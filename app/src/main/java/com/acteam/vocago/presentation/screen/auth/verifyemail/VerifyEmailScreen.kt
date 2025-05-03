package com.acteam.vocago.presentation.screen.auth.verifyemail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.BackButton
import com.acteam.vocago.presentation.screen.auth.common.CountdownTimer
import com.acteam.vocago.presentation.screen.auth.common.OTPInputField
import com.acteam.vocago.utils.safeClickable


@Composable
fun VerifyEmailScreen(
    onBackClick: () -> Unit,
    onResendOtp: () -> Unit,
) {
    var otp by remember { mutableStateOf("") }
    var isTimerReset by remember { mutableStateOf(false) }
    var timerKey by remember { mutableStateOf(0) }
    val resetTimer = {
        isTimerReset = !isTimerReset
        timerKey +=1
        onResendOtp()
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                BackButton(
                    onClick = onBackClick,
                )
            }
            Text(
                text = stringResource(R.string.text_verify_email),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AuthImageCard(R.drawable.verify_email, width = 0.8f)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CountdownTimer(
                    initialTime =  60 ,
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
                text = stringResource(R.string.text_resend_email),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .safeClickable(
                        "resend_verify_email",
                    ){
                        resetTimer()
                    }
                    .padding(8.dp)
                    .align(Alignment.End)
            )
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
                onClick = {
                },
            ) {
                Text(
                    stringResource(R.string.input_enter_otp).uppercase(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
@Preview
@Composable
fun VerifyEmailScreenPreview() {
    VerifyEmailScreen(
        onBackClick = {},
        onResendOtp = {},
    )}