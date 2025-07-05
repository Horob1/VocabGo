package com.acteam.vocago.presentation.screen.auth.verifyemail

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.CountdownDisplay
import com.acteam.vocago.presentation.screen.auth.common.OTPInputField
import com.acteam.vocago.presentation.screen.auth.common.TopBar
import com.acteam.vocago.presentation.screen.auth.common.TopBarNoTitle
import com.acteam.vocago.presentation.screen.common.ErrorBannerWithTimer
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP
import com.acteam.vocago.utils.responsiveValue
import com.acteam.vocago.utils.safeClickable


@Composable
fun VerifyEmailScreen(
    email: String,
    viewModel: VerifyEmailViewModel,
    authNavController: NavController
) {
    val uiState by viewModel.verifyEmailUIState.collectAsState()
    val otpState by viewModel.otpState.collectAsState()
    val countDownState by viewModel.otpCountdown.collectAsState()
    val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeBottomDp = with(LocalDensity.current) { imeBottomPx.toDp() }

    val buttonHeight = responsiveDP(48, 56, 60)
    val focusManager = LocalFocusManager.current
    val deviceType = getDeviceType()
    val titleFontSize = responsiveSP(mobile = 30, tabletPortrait = 36, tabletLandscape = 42)
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val descFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)
    val verticalSpacing = responsiveDP(mobile = 12, tabletPortrait = 20, tabletLandscape = 24)
    val topPadding = responsiveDP(mobile = 16, tabletPortrait = 24, tabletLandscape = 28)
    val textFontSize = responsiveSP(mobile = 20, tabletPortrait = 24, tabletLandscape = 24)
    LaunchedEffect(Unit) {
        viewModel.startOtpCountdown(300)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            })
    {
        if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding)
                    .padding(bottom = imeBottomDp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(verticalSpacing)
            ) {
                TopBar(
                    text = stringResource(R.string.text_verify_email),
                    fontSize = titleFontSize,
                    onBackClick = { authNavController.popBackStack() }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AuthImageCard(R.drawable.verify_email, width = 0.8f)
                }
                Spacer(
                    modifier = if (deviceType == DeviceType.Mobile)
                        Modifier.height(verticalSpacing * 3)
                    else
                        Modifier.height(verticalSpacing / 3)
                )
                VerifyOtpSection(
                    email = email,
                    otp = otpState.otp,
                    onOtpChange = { viewModel.setOtpValue(it) },
                    onResendClick = { viewModel.resendVerifyEmail(email) },
                    onVerifyClick = {
                        viewModel.verifyEmail(email) {
                            authNavController.navigate(NavScreen.LoginNavScreen) {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }
                    },
                    buttonHeight = buttonHeight,
                    horizontalPadding = horizontalPadding,
                    textFontSize = textFontSize,
                    descFontSize = descFontSize,
                    verticalSpacing = verticalSpacing,
                    countDownText = countDownState
                )
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
                    TopBarNoTitle {
                        authNavController.popBackStack()
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
                    VerifyOtpSection(
                        email = email,
                        otp = otpState.otp,
                        onOtpChange = { viewModel.setOtpValue(it) },
                        onResendClick = { viewModel.resendVerifyEmail(email) },
                        onVerifyClick = {
                            viewModel.verifyEmail(email) {
                                authNavController.navigate(NavScreen.LoginNavScreen) {
                                    popUpTo(0)
                                    launchSingleTop = true
                                }
                            }
                        },
                        buttonHeight = buttonHeight,
                        horizontalPadding = horizontalPadding,
                        textFontSize = textFontSize,
                        descFontSize = descFontSize,
                        verticalSpacing = verticalSpacing,
                        countDownText = countDownState
                    )
                }
            }

        }
        if (uiState is UIState.UIError) {
            val errorType = (uiState as UIState.UIError).errorType
            val message = when (errorType) {
                UIErrorType.NotFoundError -> R.string.text_error_user_not_found
                UIErrorType.BadRequestError -> R.string.text_otp_expired_and_invalid
                UIErrorType.ForbiddenError -> R.string.text_user_was_banned
                UIErrorType.TooManyRequestsError -> R.string.text_too_many_requests
                else -> R.string.text_unknown_error
            }
            ErrorBannerWithTimer(
                title = stringResource(R.string.text_error),
                message = stringResource(message),
                iconResId = R.drawable.error_banner,
                onTimeout = { viewModel.clearUIState() },
                onDismiss = { viewModel.clearUIState() },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp)
            )
        }
    }
    if (uiState is UIState.UILoading) {
        LoadingSurface(
            picSize = responsiveValue(180, 360, 360)
        )
    }
}

@Composable
fun VerifyOtpSection(
    email: String,
    otp: String,
    onOtpChange: (String) -> Unit,
    onResendClick: () -> Unit,
    onVerifyClick: () -> Unit,
    buttonHeight: Dp,
    horizontalPadding: Dp,
    textFontSize: TextUnit,
    descFontSize: TextUnit,
    verticalSpacing: Dp,
    countDownText: String
) {
    val deviceType = getDeviceType()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(responsiveDP(8, 12, 16))
    ) {
        if (deviceType == DeviceType.Mobile) {
            Spacer(modifier = Modifier.height(verticalSpacing))
        } else if (deviceType == DeviceType.TabletPortrait) {
            Spacer(modifier = Modifier.height(verticalSpacing))
        }
        CountdownDisplay(timerText = countDownText)
        if (deviceType == DeviceType.Mobile) {
            Spacer(modifier = Modifier.height(verticalSpacing))
        } else if (deviceType == DeviceType.TabletPortrait) {
            Spacer(modifier = Modifier.height(verticalSpacing))
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

        if (deviceType == DeviceType.Mobile) {
            Spacer(modifier = Modifier.height(verticalSpacing))
        } else if (deviceType == DeviceType.TabletPortrait) {
            Spacer(modifier = Modifier.height(verticalSpacing))
        }
        OTPInputField(
            otp = otp,
            onOtpChange = onOtpChange
        )
        if (deviceType == DeviceType.Mobile) {
            Spacer(modifier = Modifier.height(verticalSpacing))
        } else if (deviceType == DeviceType.TabletPortrait) {
            Spacer(modifier = Modifier.height(verticalSpacing / 3))
        }
        Text(
            text = stringResource(R.string.text_resend_email),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                fontSize = descFontSize
            ),
            modifier = Modifier
                .safeClickable("resend_verify_email") {
                    onResendClick()
                }
                .padding(horizontal = horizontalPadding / 3)
                .align(Alignment.End)
        )
        if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
            Spacer(modifier = Modifier.height(verticalSpacing * 3))
        }
        Button(
            modifier = Modifier
                .height(buttonHeight)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
            onClick = onVerifyClick
        ) {
            Text(
                stringResource(R.string.input_enter_otp).uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
