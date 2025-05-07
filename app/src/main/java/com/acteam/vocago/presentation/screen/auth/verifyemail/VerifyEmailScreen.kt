package com.acteam.vocago.presentation.screen.auth.verifyemail

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.BackButton
import com.acteam.vocago.presentation.screen.auth.common.CountdownTimer
import com.acteam.vocago.presentation.screen.auth.common.OTPInputField
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP
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
        timerKey += 1
        onResendOtp()
    }
    var email by remember { mutableStateOf("vancong@gmail.com") }

    val buttonHeight = responsiveDP(48, 56, 60)
    val focusManager = LocalFocusManager.current
    val deviceType = getDeviceType()
    val titleFontSize = responsiveSP(mobile = 30, tabletPortrait = 36, tabletLandscape = 42)
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val descFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)
    val verticalSpacing = responsiveDP(mobile = 12, tabletPortrait = 20, tabletLandscape = 24)
    val topPadding = responsiveDP(mobile = 16, tabletPortrait = 24, tabletLandscape = 28)
    val textFontSize = responsiveSP(mobile = 20, tabletPortrait = 24, tabletLandscape = 24)

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }) {
            if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = horizontalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BackButton(
                            onClick = onBackClick,
                        )
                        Text(
                            text = stringResource(R.string.text_verify_email),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = titleFontSize,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .weight(1f),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AuthImageCard(R.drawable.verify_email, width = 0.8f)
                    }
                    Spacer(
                        modifier = if (deviceType == DeviceType.Mobile)
                            Modifier.weight(1f)
                        else
                            Modifier.height(verticalSpacing / 3)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CountdownTimer(
                            initialTime = 60,
                            key = timerKey,
                        )
                    }

                    Spacer(modifier = Modifier.height(verticalSpacing))
                    Text(
                        text = "${stringResource(R.string.text_send_otp)} $email",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = textFontSize,
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
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
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            fontSize = descFontSize
                        ),
                        modifier = Modifier
                            .safeClickable(
                                "resend_verify_email",
                            ) {
                                resetTimer()
                            }
                            .padding(horizontalPadding / 3)
                            .align(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(verticalSpacing))

                    Button(
                        modifier = Modifier
                            .height(buttonHeight)
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
                    Spacer(modifier = Modifier.height(verticalSpacing / 3))
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = horizontalPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(top = topPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = topPadding, start = horizontalPadding),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                BackButton(onClick = onBackClick)
                            }
                        }
                        Spacer(modifier = Modifier.height(verticalSpacing))
                        AuthImageCard(R.drawable.verify_email, 0.8f)
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(
                                top = topPadding,
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(space = verticalSpacing)
                    ) {
                        Spacer(modifier = Modifier.height(verticalSpacing * 3))
                        Text(
                            text = stringResource(R.string.text_verify_email),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = titleFontSize
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(horizontal = horizontalPadding / 3)

                        )
                        Spacer(modifier = Modifier.height(verticalSpacing))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CountdownTimer(
                                initialTime = 60,
                                key = timerKey,
                            )
                        }
                        Text(
                            text = "${stringResource(R.string.text_send_otp)} $email",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = textFontSize,
                                textAlign = TextAlign.Center
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
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
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                                fontSize = descFontSize
                            ),
                            modifier = Modifier
                                .safeClickable(
                                    "resend_verify_email",
                                ) {
                                    resetTimer()
                                }
                                .padding(horizontalPadding / 3)
                                .align(Alignment.End)
                        )

                        Button(
                            modifier = Modifier
                                .height(buttonHeight)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
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
        }
    }
}