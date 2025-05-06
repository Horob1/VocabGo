package com.acteam.vocago.presentation.screen.auth.login

import android.util.Log
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.common.AuthCardType
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.BackButton
import com.acteam.vocago.presentation.screen.auth.common.PlatFormSignUpButton
import com.acteam.vocago.presentation.screen.auth.login.component.LoginForm
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
fun LoginScreen(
    viewModel: LoginViewModel,
    onBackClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    navigateToVerifyEmail: (String) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.loginUIState.collectAsState()
    val focusManager = LocalFocusManager.current
    val credentialManager = remember { CredentialManager.create(context) }


    val deviceType = getDeviceType()
    val titleFontSize = responsiveSP(mobile = 30, tabletPortrait = 36, tabletLandscape = 42)
    val descFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val topPadding = responsiveDP(mobile = 16, tabletPortrait = 24, tabletLandscape = 28)
    val verticalSpacing = responsiveDP(mobile = 12, tabletPortrait = 20, tabletLandscape = 24)

    LaunchedEffect(uiState) {
        Log.d("LoginScreen", "Current uiState: $uiState")
        val error = uiState
        if (error is UIState.UIError) {
            Log.e("LoginScreen", "Error: ${error.errorType}")
            val username = viewModel.loginFormState.value.username
            if (error.errorType == UIErrorType.BadRequestError && username.isNotBlank()) {
                navigateToVerifyEmail(username)
                return@LaunchedEffect
            }

            val messageRes = when (error.errorType) {
                UIErrorType.NotFoundError,
                UIErrorType.UnauthorizedError -> R.string.text_usernamer_or_password_incorrect

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
                }
        ) {
            if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = horizontalPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BackButton(
                            onClick = onBackClick,
                        )

                        Text(
                            text = stringResource(R.string.btn_login),
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
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AuthImageCard(
                            R.drawable.login,
                            width = if (deviceType == DeviceType.Mobile) 0.7f else 0.8f,
                            type = if (deviceType == DeviceType.Mobile) AuthCardType.Circle else AuthCardType.Square
                        )

                    }
                    Spacer(modifier = Modifier.height(verticalSpacing / 3))
                    LoginForm(
                        viewModel = viewModel,
                        onLoginClick = {
                            viewModel.login {
                                try {
                                    credentialManager.createCredential(
                                        request = CreatePasswordRequest(
                                            id = viewModel.loginFormState.value.username,
                                            password = viewModel.loginFormState.value.password
                                        ),
                                        context = context
                                    )
                                } catch (_: Exception) {
                                }
                                onBackClick()
                            }
                        },
                        onForgotPasswordClick = onForgotPasswordClick
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp),
                        )
                        Text(
                            text = stringResource(R.string.text_or).uppercase(),
                            modifier = Modifier.padding(horizontal = horizontalPadding / 3),

                            )
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp),
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PlatFormSignUpButton(R.drawable.google) {}
                        PlatFormSignUpButton(R.drawable.facebook) {}
                        PlatFormSignUpButton(R.drawable.github) {}
                    }

                    Spacer(modifier = Modifier.height(verticalSpacing / 3))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.text_dont_have_account),
                            modifier = Modifier.padding(horizontal = horizontalPadding / 3),
                            fontSize = descFontSize
                        )
                        Text(
                            text = stringResource(R.string.btn_sign_up),
                            modifier = Modifier
                                .safeClickable(
                                    "btn_sign_up",
                                    onClick = {
                                        onSignUpClick()
                                    }
                                )
                                .padding(
                                    horizontalPadding / 3
                                ),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                                fontSize = descFontSize
                            )
                        )
                    }
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = topPadding, start = horizontalPadding),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            BackButton(onClick = onBackClick)
                        }
                        Spacer(modifier = Modifier.height(verticalSpacing))
                        AuthImageCard(R.drawable.login, 0.8f)
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
                            text = stringResource(R.string.btn_login),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = titleFontSize
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(horizontal = horizontalPadding / 3)

                        )
                        Spacer(modifier = Modifier.height(verticalSpacing))
                        LoginForm(
                            viewModel = viewModel,
                            onLoginClick = {
                                viewModel.login {
                                    try {
                                        credentialManager.createCredential(
                                            request = CreatePasswordRequest(
                                                id = viewModel.loginFormState.value.username,
                                                password = viewModel.loginFormState.value.password
                                            ),
                                            context = context
                                        )
                                    } catch (_: Exception) {
                                    }
                                    onBackClick()
                                }
                            },
                            onForgotPasswordClick = onForgotPasswordClick
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(
                                Modifier
                                    .weight(1f)
                                    .height(1.dp)
                            )
                            Text(
                                text = stringResource(R.string.text_or).uppercase(),
                                fontSize = descFontSize,
                                modifier = Modifier.padding(horizontal = horizontalPadding / 3)
                            )
                            HorizontalDivider(
                                Modifier
                                    .weight(1f)
                                    .height(1.dp)
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            PlatFormSignUpButton(R.drawable.google) {}
                            PlatFormSignUpButton(R.drawable.facebook) {}
                            PlatFormSignUpButton(R.drawable.github) {}
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.text_dont_have_account),
                                fontSize = descFontSize,
                            )
                            Text(
                                text = stringResource(R.string.btn_sign_up),
                                modifier = Modifier
                                    .safeClickable("btn_sign_up") { onSignUpClick() }
                                    .padding(horizontal = horizontalPadding / 3),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = descFontSize,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
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