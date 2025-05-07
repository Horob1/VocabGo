package com.acteam.vocago.presentation.screen.auth.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun OTPInputField(
    otp: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onOtpCompleted: (String) -> Unit = {},
) {
    val focusRequesters = List(6) { FocusRequester() }
    val localFocusManager = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        (0 until 6).forEach { index ->
            val char = otp.getOrNull(index)?.toString() ?: ""
            OTPDigitField(
                value = char,
                onValueChange = { value ->
                    if (value.length <= 1 && value.all { it.isDigit() }) {
                        val newOtp = otp.toMutableList()
                        if (otp.length > index) {
                            newOtp[index] = value.firstOrNull() ?: ' '
                        } else {
                            newOtp.add(value.firstOrNull() ?: ' ')
                        }
                        onOtpChange(newOtp.joinToString("").trim())

                        if (value.isNotEmpty() && index < 5) {
                            focusRequesters[index + 1].requestFocus()
                        } else if (index == 5) {
                            localFocusManager.clearFocus()
                            onOtpCompleted(otp)
                        }
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequesters[index])
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }
}

@Composable
fun OTPDigitField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .shadow(8.dp, shape = RoundedCornerShape(0.dp))
            .clip(RoundedCornerShape(0.dp)),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        visualTransformation = VisualTransformation.None,
        shape = RoundedCornerShape(0.dp),
        colors = OutlinedTextFieldDefaults.colors(
            //focusedBorderColor = colorScheme.primary, // Viền khi được chọn có màu chính của theme
            //unfocusedBorderColor = colorScheme.onSurfaceVariant, // Viền khi không được chọn
            focusedContainerColor = colorScheme.surfaceVariant, // Nền khi được chọn
            unfocusedContainerColor = colorScheme.surfaceVariant, // Nền khi không được chọn
            focusedTextColor = colorScheme.onSurface, // Văn bản khi được chọn
            unfocusedTextColor = colorScheme.onSurfaceVariant, // Văn bản khi không được chọn
            cursorColor = colorScheme.primary
        ),
        placeholder = {
            Text(" ", color = colorScheme.outlineVariant) // Màu placeholder
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun CountdownTimer(
    initialTime: Int,
    key: Int,
) {
    var timeLeft by remember { mutableIntStateOf(initialTime) }
    var isTimerRunning by remember { mutableStateOf(true) }

    LaunchedEffect(key) {
        timeLeft = initialTime
        isTimerRunning = true
        while (timeLeft > 0) {
            delay(1000L)
            if (isTimerRunning) {
                timeLeft -= 1
            }
        }
    }
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)
    CountdownDisplay(formattedTime)
}

@Composable
fun CountdownDisplay(timerText: String) {
    Text(
        text = timerText,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )
    )
}

