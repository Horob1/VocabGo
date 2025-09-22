package com.acteam.vocago.presentation.screen.auth.verify2fa

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

@Composable
fun VerifyTwoFAScreen(
    email: String,
    viewModel: VerifyTwoFAViewModel,
    authNavController: NavController,
    rootNavController: NavController,
) {
    val uiState by viewModel.verifyTwoFAUIState.collectAsState()
    val otpState by viewModel.otpState.collectAsState()

    val focusManager = LocalFocusManager.current
    val deviceType = getDeviceType()

    val titleFontSize = responsiveSP(30, 36, 42)
    val horizontalPadding = responsiveDP(24, 40, 48)
    val verticalSpacing = responsiveDP(12, 20, 24)
    val topPadding = responsiveDP(16, 24, 28)
    val textFontSize = responsiveSP(20, 24, 24)
    val buttonHeight = responsiveDP(48, 56, 60)

    val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeBottomDp = with(LocalDensity.current) { imeBottomPx.toDp() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
    ) {
        if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = imeBottomDp)
                    .padding(horizontal = horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(verticalSpacing)
            ) {
                TopBar(
                    text = stringResource(R.string.text_2fa_authentication),
                    fontSize = titleFontSize,
                    onBackClick = { authNavController.popBackStack() }
                )
                AuthImageCard(R.drawable.twofa, width = 0.8f)

                Spacer(
                    modifier = if (deviceType == DeviceType.Mobile) Modifier.weight(1f) else Modifier.height(
                        verticalSpacing / 3
                    )
                )

                Verify2FABody(
                    otp = otpState.otp,
                    onOtpChange = viewModel::setOtpValue,
                    onVerifyClick = {
                        viewModel.verifyTwoFA(email) {
                            rootNavController.navigate(NavScreen.MainNavScreen) {
                                popUpTo(NavScreen.AuthNavScreen) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                        focusManager.clearFocus()
                    },
                    textFontSize = textFontSize,
                    buttonHeight = buttonHeight,
                    verticalSpacing = verticalSpacing,
                    horizontalPadding = horizontalPadding
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopBarNoTitle {
                        authNavController.popBackStack()
                    }
                    Spacer(modifier = Modifier.height(verticalSpacing))
                    AuthImageCard(R.drawable.twofa, width = 0.8f)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(top = topPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing)
                ) {
                    Spacer(modifier = Modifier.height(verticalSpacing * 2))
                    Text(
                        text = stringResource(R.string.text_2fa_authentication),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = titleFontSize
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(horizontal = horizontalPadding / 3)

                    )
                    Spacer(modifier = Modifier.height(verticalSpacing * 3))
                    Verify2FABody(
                        otp = otpState.otp,
                        onOtpChange = viewModel::setOtpValue,
                        onVerifyClick = {
                            viewModel.verifyTwoFA(email) {
                                rootNavController.navigate(NavScreen.MainNavScreen) {
                                    popUpTo(NavScreen.AuthNavScreen) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                            focusManager.clearFocus()
                        },
                        textFontSize = textFontSize,
                        buttonHeight = buttonHeight,
                        verticalSpacing = verticalSpacing,
                        horizontalPadding = horizontalPadding
                    )
                }
            }
        }

        // Error banner
        if (uiState is UIState.UIError) {
            val errorType = (uiState as UIState.UIError).errorType
            val messageRes = when (errorType) {
                UIErrorType.NotFoundError -> R.string.text_error_user_not_found
                UIErrorType.BadRequestError -> R.string.text_otp_expired_and_invalid
                UIErrorType.ServerError -> R.string.text_server_error
                else -> R.string.text_unknown_error
            }

            ErrorBannerWithTimer(
                title = stringResource(R.string.text_error),
                message = stringResource(messageRes),
                iconResId = R.drawable.error_banner,
                onTimeout = { viewModel.clearUIState() },
                onDismiss = { viewModel.clearUIState() },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp)
            )
        }

        if (uiState is UIState.UILoading) {
            LoadingSurface(picSize = responsiveValue(180, 360, 360))
        }
    }
}

@Composable
private fun Verify2FABody(
    otp: String,
    onOtpChange: (String) -> Unit,
    onVerifyClick: () -> Unit,
    textFontSize: TextUnit,
    buttonHeight: Dp,
    verticalSpacing: Dp,
    horizontalPadding: Dp
) {
    val deviceType = getDeviceType()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
            Spacer(modifier = Modifier.height(verticalSpacing * 2))

        }
        Text(
            text = stringResource(R.string.text_verify_2fa_description),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = textFontSize,
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
        )

        if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
            Spacer(modifier = Modifier.height(verticalSpacing * 2))

        }
        OTPInputField(otp = otp, onOtpChange = onOtpChange)

        if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
            Spacer(modifier = Modifier.height(verticalSpacing * 2))
        } else {
            Spacer(modifier = Modifier.height(verticalSpacing))
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
