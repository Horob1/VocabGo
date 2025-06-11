package com.acteam.vocago.presentation.screen.auth.resetpassword

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import com.acteam.vocago.presentation.screen.auth.common.data.OtpState
import com.acteam.vocago.presentation.screen.auth.resetpassword.component.ResetPasswordForm
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
fun ResetPasswordScreen(
    email: String,
    viewModel: ResetPasswordViewModel,
    authNavController: NavController,
) {
    val uiState by viewModel.resetPasswordUIState.collectAsState()
    val otpState by viewModel.otpState.collectAsState()
    val countDownState by viewModel.otpCountdown.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current

    val deviceType =
        getDeviceType()


    val buttonHeight = responsiveDP(48, 56, 60)
    val titleFontSize = responsiveSP(30, 36, 42)
    val textFontSize = responsiveSP(20, 24, 24)
    val descFontSize = responsiveSP(14, 20, 20)
    val verticalSpacing = responsiveDP(12, 20, 24)
    val topPadding = responsiveDP(16, 24, 28)
    val horizontalPadding = responsiveDP(24, 40, 48)
    LaunchedEffect(Unit) {
        viewModel.startOtpCountdown(300)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            }
    ) {
        val enableVerticalScroll = configuration.screenHeightDp < 800

        when (deviceType) {
            DeviceType.Mobile, DeviceType.TabletPortrait -> {
                Column(
                    modifier = Modifier
                        .padding(horizontal = horizontalPadding)
                        .fillMaxSize()
                        .then(
                            if (enableVerticalScroll) Modifier.verticalScroll(scrollState)
                            else Modifier
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing)
                ) {
                    TopBar(
                        text = stringResource(R.string.text_reset_password),
                        fontSize = titleFontSize,
                        onBackClick = { authNavController.popBackStack() }
                    )
                    AuthImageCard(R.drawable.resetpassword, 0.7f)
                    CommonContent(
                        email = email,
                        viewModel = viewModel,
                        focusManager = focusManager,
                        authNavController = authNavController,
                        otpState = otpState,
                        countDownState = countDownState,
                        textFontSize = textFontSize,
                        descFontSize = descFontSize,
                        verticalSpacing = verticalSpacing,
                        buttonHeight = buttonHeight,
                        context = context,
                        deviceType = deviceType
                    )
                }
            }

            DeviceType.TabletLandscape -> {
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
                        TopBarNoTitle(
                            onBackClick = { authNavController.popBackStack() }
                        )
                        Spacer(modifier = Modifier.height(verticalSpacing))
                        AuthImageCard(R.drawable.resetpassword, 0.8f)
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
                            text = stringResource(R.string.text_reset_password),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = titleFontSize
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = horizontalPadding / 3)
                        )
                        CommonContent(
                            email = email,
                            viewModel = viewModel,
                            focusManager = focusManager,
                            authNavController = authNavController,
                            otpState = otpState,
                            countDownState = countDownState,
                            textFontSize = textFontSize,
                            descFontSize = descFontSize,
                            verticalSpacing = verticalSpacing,
                            buttonHeight = buttonHeight,
                            context = context,
                            deviceType = deviceType
                        )
                    }
                }
            }
        }
        if (uiState is UIState.UIError) {
            val error = uiState as UIState.UIError
            val messageRes = remember(error.errorType) {
                when (error.errorType) {
                    UIErrorType.NotFoundError -> R.string.text_email_has_not_been_register
                    UIErrorType.BadRequestError -> R.string.text_otp_expired_and_invalid
                    UIErrorType.UnexpectedEntityError -> R.string.text_unknown_error
                    UIErrorType.ForbiddenError -> R.string.text_user_was_banned
                    UIErrorType.TooManyRequestsError -> R.string.text_too_many_requests
                    else -> R.string.text_unknown_error
                }
            }
            ErrorBannerWithTimer(
                title = stringResource(R.string.text_error),
                message = stringResource(messageRes),
                iconResId = R.drawable.error_banner,
                onTimeout = remember(viewModel) { { viewModel.clearUIState() } },
                onDismiss = remember(viewModel) { { viewModel.clearUIState() } },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp)
            )
        }

        if (uiState is UIState.UILoading) {
            val picSize = responsiveValue(180, 360, 360)
            LoadingSurface(picSize = picSize)
        }
    }
}

@Composable
private fun CommonContent(
    email: String,
    viewModel: ResetPasswordViewModel,
    focusManager: FocusManager,
    authNavController: NavController,
    otpState: OtpState,
    countDownState: String,
    textFontSize: TextUnit,
    descFontSize: TextUnit,
    verticalSpacing: Dp,
    buttonHeight: Dp,
    context: Context,
    deviceType: DeviceType
) {
    if (deviceType == DeviceType.Mobile) {
        Spacer(modifier = Modifier.height(verticalSpacing))
    }
    CountdownDisplay(timerText = countDownState)
    if (deviceType == DeviceType.Mobile) {
        Spacer(modifier = Modifier.height(verticalSpacing))
    }
    OTPInputField(
        otp = otpState.otp,
        onOtpChange = viewModel::setOtpValue
    )
    Spacer(modifier = Modifier.height(verticalSpacing / 3))

    Text(
        text = "${stringResource(R.string.text_send_otp)} $email",
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = textFontSize, // Using passed-in value
            textAlign = TextAlign.Center
        ),
        color = MaterialTheme.colorScheme.primary
    )

    val onSaveChangeClick = remember(viewModel, email, authNavController, focusManager) {
        {
            viewModel.resetPassword(email) {
                authNavController.navigate(NavScreen.LoginNavScreen) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            }
            focusManager.clearFocus()
        }
    }
    ResetPasswordForm(
        viewModel = viewModel,
        onSaveChangeClick = onSaveChangeClick
    )

    Text(
        text = stringResource(R.string.text_resend_email),
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = descFontSize
        ),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .safeClickable(
                "btn_resend_email_forgot_password",
                onClick = remember(viewModel, email) {
                    {
                        viewModel.requestOtpAndStartCountdown(email)
                    }
                })
            .padding(8.dp),
        textAlign = TextAlign.End
    )

    if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
        Spacer(modifier = Modifier.height(verticalSpacing * 2))
    }

    val onResetButtonClick = remember(viewModel, email, authNavController, focusManager, context) {
        {
            if (viewModel.resetPasswordFormState.value.isResetPasswordButtonEnabled) {
                viewModel.resetPassword(email) {
                    authNavController.navigate(NavScreen.LoginNavScreen) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
                focusManager.clearFocus()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.text_please_all_required),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Button(
        modifier = Modifier
            .height(buttonHeight)
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
        onClick = onResetButtonClick
    ) {
        Text(
            text = stringResource(R.string.btn_save_change).uppercase(),
            style = MaterialTheme.typography.titleMedium
        )
    }
    Spacer(modifier = Modifier.height(verticalSpacing / 3))
}