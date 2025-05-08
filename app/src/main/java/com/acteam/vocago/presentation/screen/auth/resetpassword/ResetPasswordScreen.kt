package com.acteam.vocago.presentation.screen.auth.resetpassword

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.BackButton
import com.acteam.vocago.presentation.screen.auth.common.CountdownDisplay
import com.acteam.vocago.presentation.screen.auth.common.OTPInputField
import com.acteam.vocago.presentation.screen.auth.resetpassword.component.ResetPasswordForm
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
    viewModel: ResetPasswordViewModel,
    onBackClick: () -> Unit,
    onResendOtp: () -> Unit,
    onSaveChangeClick: () -> Unit
) {

    val uiState by viewModel.resetPasswordUIState.collectAsState()
    val formState by viewModel.resetPasswordFormState.collectAsState()
    val otpState by viewModel.otpState.collectAsState()
    val coutdownState by viewModel.otpCountdown.collectAsState()

    val buttonHeight = responsiveDP(48, 56, 60)
    val focusManager = LocalFocusManager.current
    val deviceType = getDeviceType()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val titleFontSize = responsiveSP(mobile = 30, tabletPortrait = 36, tabletLandscape = 42)
    val textFontSize = responsiveSP(mobile = 20, tabletPortrait = 24, tabletLandscape = 24)
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val descFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)
    val verticalSpacing = responsiveDP(mobile = 12, tabletPortrait = 20, tabletLandscape = 24)
    val topPadding = responsiveDP(mobile = 16, tabletPortrait = 24, tabletLandscape = 28)
    LaunchedEffect(uiState) {
        viewModel.startOtpCountdown(300)
        val error = uiState
        if (error is UIState.UIError) {
            val messageRes = when (error.errorType) {
                UIErrorType.NotFoundError -> R.string.text_email_has_not_been_register
                UIErrorType.BadRequestError -> R.string.text_otp_expired_and_invalid
                UIErrorType.UnexpectedEntityError -> R.string.text_unknown_error
                UIErrorType.ForbiddenError -> R.string.text_user_was_banned
                else -> R.string.text_unknown_error
            }
            Toast.makeText(context, context.getString(messageRes), Toast.LENGTH_SHORT).show()
            Toast.makeText(
                context,
                context.getString(R.string.text_unknown_error),
                Toast.LENGTH_SHORT
            )
                .show()
            viewModel.clearUIState()
        }
    }
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
                    modifier = if (LocalConfiguration.current.screenHeightDp < 800)
                        Modifier
                            .padding(horizontal = horizontalPadding)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    else
                        Modifier
                            .padding(horizontal = horizontalPadding)
                            .fillMaxSize(),
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
                            text = stringResource(R.string.text_reset_password),
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
                        AuthImageCard(R.drawable.resetpassword, width = 0.7f)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CountdownDisplay(timerText = coutdownState )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OTPInputField(
                            otp = otpState.otp,
                            onOtpChange = { it ->
                                viewModel.setOtpValue(it)
                            },
                            onOtpCompleted = { otp ->

                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(verticalSpacing / 3))
                    Text(
                        text = "${stringResource(R.string.text_send_otp)} ${formState.email}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = textFontSize,
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
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
                            .safeClickable(
                                key = "btn_resend_email_forgot_password",
                                onClick = {
                                    viewModel.resetOtpCountdown()
                                }
                            )
                            .padding(8.dp)
                            .align(Alignment.End)
                    )
                    Spacer(
                        modifier = if (deviceType == DeviceType.Mobile)
                            Modifier.weight(1f)
                        else
                            Modifier.height(verticalSpacing / 3)
                    )
                    Button(
                        modifier = Modifier
                            .height(buttonHeight)
                            .fillMaxWidth()
                            .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
                        onClick = {
                            if ( formState.isResetPasswordButtonEnabled && uiState !is UIState.UILoading){
                                viewModel.resetPassword {
                                    onSaveChangeClick
                                }
                            } else if (!viewModel.resetPasswordFormState.value.isResetPasswordButtonEnabled) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.text_please_all_required),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.btn_save_change).uppercase(),
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
                        Spacer(modifier = Modifier.height(verticalSpacing * 3))
                        AuthImageCard(R.drawable.resetpassword, 0.8f)
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
                            text = stringResource(R.string.text_reset_password),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = titleFontSize
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(horizontal = horizontalPadding / 3)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CountdownDisplay(timerText = coutdownState )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OTPInputField(
                                otp = otpState.otp,
                                onOtpChange = { it ->
                                    viewModel.setOtpValue(it)
                                },
                                onOtpCompleted = { otp ->

                                }
                            )
                        }
                        Text(
                            text = "${stringResource(R.string.text_send_otp)} ${formState.email}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = textFontSize,
                                textAlign = TextAlign.Center
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
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

                                .safeClickable(
                                    key = "btn_resend_email_forgot_password",
                                    onClick = {
                                        viewModel.resetOtpCountdown()
                                    }
                                )
                                .padding(8.dp)
                                .align(Alignment.End)
                        )

                        Button(
                            modifier = Modifier
                                .height(buttonHeight)
                                .fillMaxWidth()
                                .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
                            onClick = {
                                if ( formState.isResetPasswordButtonEnabled && uiState !is UIState.UILoading){
                                     viewModel.resetPassword(onSaveChangeClick)
                                } else if (!viewModel.resetPasswordFormState.value.isResetPasswordButtonEnabled) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.text_please_all_required),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
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
        }
        if (uiState is UIState.UILoading) {
            LoadingSurface(
                picSize = responsiveValue(180, 360, 360)
            )
        }
    }
}
