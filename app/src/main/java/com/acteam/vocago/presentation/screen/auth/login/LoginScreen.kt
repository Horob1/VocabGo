package com.acteam.vocago.presentation.screen.auth.login

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.acteam.vocago.BuildConfig
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.TopBar
import com.acteam.vocago.presentation.screen.auth.common.TopBarNoTitle
import com.acteam.vocago.presentation.screen.auth.login.component.LoginForm
import com.acteam.vocago.presentation.screen.common.ErrorBannerWithTimer
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.utils.CredentialHelper
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.GoogleLoginHelper
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP
import com.acteam.vocago.utils.responsiveValue
import com.acteam.vocago.utils.safeClickable
import com.acteam.vocago.utils.validators.AuthValidators

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    rootNavController: NavController,
    authNavController: NavController,
) {
    val context = LocalContext.current
    val uiState by viewModel.loginUIState.collectAsState()
    val loginFormState by viewModel.loginFormState.collectAsState()
    var show2FADialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val deviceType = getDeviceType()
    val titleFontSize = responsiveSP(mobile = 30, tabletPortrait = 36, tabletLandscape = 42)
    val descFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val topPadding = responsiveDP(mobile = 16, tabletPortrait = 24, tabletLandscape = 28)
    val verticalSpacing = responsiveDP(mobile = 12, tabletPortrait = 20, tabletLandscape = 24)
    val dialogTitleFontSize = responsiveSP(mobile = 20, tabletPortrait = 26, tabletLandscape = 28)
    val dialogTextFontSize = responsiveSP(mobile = 14, tabletPortrait = 18, tabletLandscape = 20)
    val dialogButtonFontSize = responsiveSP(mobile = 14, tabletPortrait = 18, tabletLandscape = 20)

    val onLoginClicked = remember(viewModel, context, focusManager, rootNavController) {
        {
            viewModel.login {
                CredentialHelper.saveCredential(
                    context,
                    viewModel.loginFormState.value.username,
                    viewModel.loginFormState.value.password
                )
                focusManager.clearFocus()
                rootNavController.navigate(NavScreen.MainNavScreen) {
                    popUpTo(NavScreen.AuthNavScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            focusManager.clearFocus()
        }
    }
    val onForgotPasswordClicked = remember(authNavController) {
        {
            authNavController.navigate(NavScreen.ForgotPasswordNavScreen) {
                launchSingleTop = true
            }
        }
    }
    val onGoogleLoginClicked = remember(context, viewModel, rootNavController) {
        {
            val activity = context as? ComponentActivity
            if (activity == null) {
                Toast.makeText(context, "Cannot initiate Google Login.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                GoogleLoginHelper.loginWithGoogle(
                    activity = activity,
                    context = context,
                    scope = activity.lifecycleScope,
                    webClientId = BuildConfig.GOOGLE_CLIENT_ID,
                    onSuccess = { idToken ->
                        viewModel.loginWithGoogle(idToken) {
                            rootNavController.navigate(NavScreen.MainNavScreen) {
                                popUpTo(NavScreen.AuthNavScreen) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }


    Box(
        modifier = Modifier
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
                TopBar(
                    text = stringResource(R.string.btn_login),
                    fontSize = titleFontSize,
                    onBackClick = {
                        rootNavController.navigate(NavScreen.MainNavScreen) {
                            popUpTo(NavScreen.AuthNavScreen) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (deviceType == DeviceType.Mobile) 0.7f else 0.8f)
                            .aspectRatio(ratio = 1f)
                            .clip(CircleShape)
                            .shadow(8.dp, shape = CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.login),
                            contentDescription = "Auth Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(verticalSpacing / 3))
                LoginForm(
                    viewModel = viewModel,
                    onLoginClick = onLoginClicked,
                    onForgotPasswordClick = onForgotPasswordClicked
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                    )
                    Text(
                        text = stringResource(R.string.text_or).uppercase(),
                        modifier = Modifier.padding(horizontal = horizontalPadding / 3),
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GoogleLoginButton(onClick = onGoogleLoginClicked)
                }

                Spacer(modifier = Modifier.height(verticalSpacing / 3))

                SignUpText(
                    descFontSize = descFontSize,
                    horizontalPadding = horizontalPadding,
                    uiState = uiState,
                    onSignUpClick = {
                        authNavController.navigate(NavScreen.RegisterNavScreen) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        } else { // TabletLandscape
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
                        rootNavController.navigate(NavScreen.MainNavScreen) {
                            popUpTo(NavScreen.AuthNavScreen) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                    Spacer(modifier = Modifier.height(verticalSpacing))
                    AuthImageCard(R.drawable.login, 0.8f)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(top = topPadding),
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
                        modifier = Modifier.padding(horizontal = horizontalPadding / 3)
                    )
                    Spacer(modifier = Modifier.height(verticalSpacing))
                    LoginForm(
                        viewModel = viewModel,
                        onLoginClick = onLoginClicked,
                        onForgotPasswordClick = onForgotPasswordClicked
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
                        GoogleLoginButton(onClick = onGoogleLoginClicked)
                    }
                    SignUpText(
                        descFontSize = descFontSize,
                        horizontalPadding = horizontalPadding,
                        uiState = uiState,
                        onSignUpClick = {
                            authNavController.navigate(NavScreen.RegisterNavScreen) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }

        if (uiState is UIState.UIError) {
            val username = loginFormState.username
            val errorType = (uiState as UIState.UIError).errorType
            var clearErrorSignal = true

            if (errorType == UIErrorType.BadRequestError && username.isNotBlank()) {
                if (AuthValidators.isValidEmail(username)) {
                    authNavController.navigate(NavScreen.VerifyEmailNavScreen(username)) {
                        launchSingleTop = true
                    }
                } else {
                    ErrorBannerWithTimer(
                        title = stringResource(R.string.text_error),
                        message = stringResource(R.string.text_try_again_with_email),
                        iconResId = R.drawable.error_banner,
                        onTimeout = { viewModel.clearUIState() },
                        onDismiss = { viewModel.clearUIState() },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 16.dp)
                    )
                    clearErrorSignal = false
                }
            } else if (errorType == UIErrorType.PreconditionFailedError) {
                show2FADialog = true
            } else if (errorType == UIErrorType.LockedError) {
                ErrorBannerWithTimer(
                    title = stringResource(R.string.text_error),
                    message = stringResource(R.string.text_locked_account_5_minutes),
                    iconResId = R.drawable.error_banner,
                    onTimeout = { viewModel.clearUIState() },
                    onDismiss = { viewModel.clearUIState() },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 16.dp)
                )
                clearErrorSignal = false
            }
            else {
                val messageResId = when (errorType) {
                    UIErrorType.NotFoundError,
                    UIErrorType.UnauthorizedError,
                    UIErrorType.UnexpectedEntityError,
                        -> R.string.text_username_or_password_incorrect

                    UIErrorType.ForbiddenError -> R.string.text_user_was_banned
                    else -> R.string.text_unknown_error
                }
                ErrorBannerWithTimer(
                    title = stringResource(R.string.text_error),
                    message = stringResource(messageResId),
                    iconResId = R.drawable.error_banner,
                    onTimeout = { viewModel.clearUIState() },
                    onDismiss = { viewModel.clearUIState() },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 16.dp)
                )
                clearErrorSignal = false
            }

            if (clearErrorSignal) {
                viewModel.clearUIState()
            }
        }
    }

    if (uiState is UIState.UILoading) {
        LoadingSurface(picSize = responsiveValue(180, 360, 360))
    }

    if (show2FADialog) {
        val titleText = stringResource(R.string.text_2fa_authentication)
        val descriptionText = stringResource(R.string.text_2fa_description)
        val continueText = stringResource(R.string.btn_continue)
        val cancelText = stringResource(R.string.btn_cancel)
        val toastText = stringResource(R.string.text_try_again_with_email)

        AlertDialog(
            modifier = Modifier.width(
                remember(deviceType) {
                    if (deviceType == DeviceType.TabletPortrait || deviceType == DeviceType.TabletLandscape) 500.dp else 300.dp
                }
            ),
            onDismissRequest = { show2FADialog = false },
            title = {
                Text(
                    text = titleText,
                    fontSize = dialogTitleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = descriptionText,
                    fontSize = dialogTextFontSize,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        show2FADialog = false
                        if (AuthValidators.isValidEmail(loginFormState.username)) {
                            authNavController.navigate(
                                NavScreen.VerifyTwoFANavScreen(loginFormState.username)
                            ) { launchSingleTop = true }
                        } else {
                            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(
                        text = continueText,
                        fontSize = dialogButtonFontSize,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { show2FADialog = false }) {
                    Text(
                        text = cancelText,
                        fontSize = dialogButtonFontSize,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Composable
fun GoogleLoginButton(
    onClick: () -> Unit,
) {
    val buttonHeight = responsiveDP(48, 56, 60)
    val googleLogoFontSize = responsiveSP(18, 22, 24)

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight),
        shape = RoundedCornerShape(36.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.text_sign_in_with_Google),
                fontSize = googleLogoFontSize, // Use remembered value
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
fun SignUpText(
    descFontSize: TextUnit,
    horizontalPadding: Dp,
    uiState: UIState<Unit>,
    onSignUpClick: () -> Unit,
) {
    val actualPadding = remember(horizontalPadding) { horizontalPadding / 3 }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.text_dont_have_account),
            fontSize = descFontSize,
            modifier = Modifier.padding(horizontal = actualPadding)
        )
        Text(
            text = stringResource(R.string.btn_sign_up),
            modifier = Modifier
                .safeClickable("btn_sign_up") {
                    if (uiState !is UIState.UILoading) {
                        onSignUpClick()
                    }
                }
                .padding(horizontal = actualPadding),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                fontSize = descFontSize
            )
        )
    }
}