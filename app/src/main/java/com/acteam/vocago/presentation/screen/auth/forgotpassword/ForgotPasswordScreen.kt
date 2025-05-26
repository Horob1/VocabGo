package com.acteam.vocago.presentation.screen.auth.forgotpassword

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    authNavController: NavController,
) {
    val formState by viewModel.forgotPasswordFormState.collectAsState()
    val uiState by viewModel.forgotPasswordUIState.collectAsState()
    val emailFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val deviceType = getDeviceType()
    val horizontalPadding = responsiveDP(24, 40, 48)
    val verticalSpacing = responsiveDP(12, 20, 24)
    val titleFontSize = responsiveSP(30, 36, 42)
    val textFontSize = responsiveSP(20, 24, 24)
    val buttonHeight = responsiveDP(48, 56, 60)
    val topPadding = responsiveDP(16, 24, 28)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            }
    ) {
        if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(verticalSpacing)
            ) {
                TopBar(
                    text = stringResource(R.string.text_forgot_password),
                    titleFontSize,
                    onBackClick = {
                        authNavController.navigate(NavScreen.LoginNavScreen) {
                            popUpTo(NavScreen.ForgotPasswordNavScreen) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )

                AuthImageCard(R.drawable.forgotpassword, width = 0.8f)

                Spacer(modifier = Modifier.weight(1f))

                InputLabel(textFontSize)

                EmailInputField(
                    email = formState.email,
                    onEmailChange = viewModel::setEmail,
                    focusRequester = emailFocusRequester,
                    focusManager = focusManager,
                    isButtonEnabled = formState.isForgotPasswordButtonEnabled,
                    isLoading = uiState is UIState.UILoading,
                    onSendClick = {
                        viewModel.forgotPassword {
                            authNavController.navigate(
                                NavScreen.ResetPasswordNavScreen(email = formState.email)
                            ) { launchSingleTop = true }
                        }
                    }
                )

                SendButton(
                    isEnabled = formState.isForgotPasswordButtonEnabled,
                    isLoading = uiState is UIState.UILoading,
                    height = buttonHeight,
                    onClick = {
                        if (formState.isForgotPasswordButtonEnabled) {
                            viewModel.forgotPassword {
                                focusManager.clearFocus()
                                authNavController.navigate(
                                    NavScreen.ResetPasswordNavScreen(email = formState.email)
                                ) { launchSingleTop = true }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.text_please_type_email),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
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
                    TopBarNoTitle(
                        onBackClick = {
                            authNavController.navigate(NavScreen.LoginNavScreen) {
                                popUpTo(NavScreen.ForgotPasswordNavScreen) { inclusive = true }
                                launchSingleTop = true
                            }
                        })
                    Spacer(modifier = Modifier.height(verticalSpacing))
                    AuthImageCard(R.drawable.forgotpassword, width = 0.8f)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(top = topPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing)
                ) {
                    Spacer(modifier = Modifier.height(verticalSpacing * 3))
                    Text(
                        text = stringResource(R.string.text_forgot_password),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = titleFontSize
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(verticalSpacing * 2))
                    InputLabel(textFontSize)

                    EmailInputField(
                        email = formState.email,
                        onEmailChange = viewModel::setEmail,
                        focusRequester = emailFocusRequester,
                        focusManager = focusManager,
                        isButtonEnabled = formState.isForgotPasswordButtonEnabled,
                        isLoading = uiState is UIState.UILoading,
                        onSendClick = {
                            viewModel.forgotPassword {
                                authNavController.navigate(
                                    NavScreen.ResetPasswordNavScreen(email = formState.email)
                                ) { launchSingleTop = true }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(verticalSpacing * 2))
                    SendButton(
                        isEnabled = formState.isForgotPasswordButtonEnabled,
                        isLoading = uiState is UIState.UILoading,
                        height = buttonHeight,
                        onClick = {
                            if (formState.isForgotPasswordButtonEnabled) {
                                viewModel.forgotPassword {
                                    focusManager.clearFocus()
                                    authNavController.navigate(
                                        NavScreen.ResetPasswordNavScreen(email = formState.email)
                                    ) { launchSingleTop = true }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.text_please_type_email),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }
        }
        if (uiState is UIState.UIError) {
            val errorType = (uiState as UIState.UIError).errorType
            val message = when (errorType) {
                UIErrorType.NotFoundError -> R.string.text_email_has_not_been_register
                UIErrorType.BadRequestError -> R.string.text_email_is_not_verify
                UIErrorType.UnauthorizedError -> R.string.text_email_incorrect
                UIErrorType.TooManyRequestsError -> R.string.text_too_many_requests
                UIErrorType.UnexpectedEntityError -> R.string.text_unknown_error
                UIErrorType.ForbiddenError -> R.string.text_user_was_banned
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
fun InputLabel(fontSize: TextUnit) {
    Text(
        text = stringResource(R.string.text_input_your_email),
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            textAlign = TextAlign.Center
        ),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun EmailInputField(
    email: String,
    onEmailChange: (String) -> Unit,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    isButtonEnabled: Boolean,
    isLoading: Boolean,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text(stringResource(R.string.input_enter_email)) },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(onDone = {
                if (isButtonEnabled && !isLoading) {
                    onSendClick()
                    focusManager.clearFocus()
                }
            }),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SendButton(
    isEnabled: Boolean,
    isLoading: Boolean,
    height: Dp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        enabled = isEnabled && !isLoading
    ) {
        Text(
            text = stringResource(R.string.btn_send_email).uppercase(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}