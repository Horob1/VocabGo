package com.acteam.vocago.presentation.screen.auth.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp


@Composable
fun RowOtpInputField(
    otp: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequesters = List(6) { FocusRequester() }
    val localFocusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
    ) {
        // Hàng đầu: 3 ô đầu (index 0, 1, 2)
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                12.dp,
                alignment = Alignment.CenterHorizontally
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            (0 until 3).forEach { index ->
                val char = otp.getOrNull(index)?.toString() ?: ""
                OTPDigitField(
                    value = char,
                    onValueChange = { value ->
                        handleOtpInput(
                            value = value,
                            index = index,
                            otp = otp,
                            focusRequesters = focusRequesters,
                            focusManager = localFocusManager,
                            onOtpChange = onOtpChange
                        )
                    },
                    onKeyEvent = { keyEvent ->
                        handleBackspace(keyEvent, char, index, focusRequesters)
                    },
                    modifier = Modifier
                        .focusRequester(focusRequesters[index])
                        .size(48.dp) // 👈 ô vuông cố định 48dp
                )
            }
        }

        // Hàng thứ hai: 3 ô cuối (index 3, 4, 5)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            (3 until 6).forEach { index ->
                val char = otp.getOrNull(index)?.toString() ?: ""
                OTPDigitField(
                    value = char,
                    onValueChange = { value ->
                        handleOtpInput(
                            value = value,
                            index = index,
                            otp = otp,
                            focusRequesters = focusRequesters,
                            focusManager = localFocusManager,
                            onOtpChange = onOtpChange
                        )
                    },
                    onKeyEvent = { keyEvent ->
                        handleBackspace(keyEvent, char, index, focusRequesters)
                    },
                    modifier = Modifier
                        .focusRequester(focusRequesters[index])
                        .size(48.dp) // 👈 giống trên
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }
}

private fun handleOtpInput(
    value: String,
    index: Int,
    otp: String,
    focusRequesters: List<FocusRequester>,
    focusManager: FocusManager,
    onOtpChange: (String) -> Unit
) {
    if (value.all { it.isDigit() } && value.length <= 1) {
        // Tạo mảng char từ OTP hiện tại, đảm bảo có đủ 6 vị trí
        val otpArray = CharArray(6) { i ->
            otp.getOrNull(i) ?: ' '
        }

        // Cập nhật ký tự tại vị trí index
        if (value.isNotEmpty()) {
            otpArray[index] = value[0]
            // Tự động chuyển focus sang ô tiếp theo
            when (index) {
                0, 1 -> focusRequesters[index + 1].requestFocus() // Từ ô 0->1, 1->2
                2 -> focusRequesters[3].requestFocus() // Từ ô 2 xuống ô 3 (hàng mới)
                3, 4 -> focusRequesters[index + 1].requestFocus() // Từ ô 3->4, 4->5
                5 -> focusManager.clearFocus() // Ô cuối cùng
            }
        } else {
            // Xóa ký tự - thay thế bằng khoảng trắng
            otpArray[index] = ' '
        }

        // Tạo OTP string mới, loại bỏ khoảng trắng cuối
        val newOtp = otpArray.concatToString().trimEnd()
        onOtpChange(newOtp)
    }
}

// Helper function để xử lý phím Backspace
private fun handleBackspace(
    keyEvent: KeyEvent,
    char: String,
    index: Int,
    focusRequesters: List<FocusRequester>
): Boolean {
    return if (keyEvent.key == Key.Backspace && char.isEmpty() && index > 0) {
        val previousIndex = when (index) {
            1, 2 -> index - 1 // Từ ô 1->0, 2->1
            3 -> 2 // Từ ô 3 lên ô 2 (hàng trên)
            4, 5 -> index - 1 // Từ ô 4->3, 5->4
            else -> 0
        }
        focusRequesters[previousIndex].requestFocus()
        true
    } else {
        false
    }
}
