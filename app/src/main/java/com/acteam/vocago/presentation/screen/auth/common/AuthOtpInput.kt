package com.acteam.vocago.presentation.screen.auth.common

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType

@Composable
fun OTPInputField(
    otp: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
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
                    // Chỉ cho phép số hoặc chuỗi rỗng
                    if (value.all { it.isDigit() } && value.length <= 1) {
                        // Tạo mảng char từ OTP hiện tại, đảm bảo có đủ 6 vị trí
                        val otpArray = CharArray(6) { i ->
                            otp.getOrNull(i) ?: ' '
                        }

                        // Cập nhật ký tự tại vị trí index
                        if (value.isNotEmpty()) {
                            otpArray[index] = value[0]
                            // Tự động chuyển focus sang ô tiếp theo
                            if (index < 5) {
                                focusRequesters[index + 1].requestFocus()
                            } else {
                                localFocusManager.clearFocus()
                            }
                        } else {
                            // Xóa ký tự - thay thế bằng khoảng trắng
                            otpArray[index] = ' '
                        }

                        // Tạo OTP string mới, loại bỏ khoảng trắng cuối
                        val newOtp = otpArray.concatToString().trimEnd()
                        onOtpChange(newOtp)
                    }
                },
                onKeyEvent = { keyEvent ->
                    // Xử lý phím Backspace để chuyển focus về ô trước
                    if (keyEvent.key == Key.Backspace && char.isEmpty() && index > 0) {
                        focusRequesters[index - 1].requestFocus()
                        true
                    } else {
                        false
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequesters[index])
                    .weight(1f)
                    .aspectRatio(0.85f)
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
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val deviceType = getDeviceType()

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .shadow(8.dp, shape = RoundedCornerShape(0.dp))
            .clip(RoundedCornerShape(0.dp))
            .onKeyEvent(onKeyEvent),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontSize = if (deviceType == DeviceType.Mobile) 14.sp else 30.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        visualTransformation = VisualTransformation.None,
        shape = RoundedCornerShape(0.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorScheme.primary,
            unfocusedBorderColor = colorScheme.onSurfaceVariant,
            focusedContainerColor = colorScheme.surfaceVariant,
            unfocusedContainerColor = colorScheme.surfaceVariant,
            focusedTextColor = colorScheme.onSurface,
            unfocusedTextColor = colorScheme.onSurfaceVariant,
            cursorColor = colorScheme.primary
        ),
        placeholder = {
            Text(" ", color = colorScheme.outlineVariant)
        }
    )
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