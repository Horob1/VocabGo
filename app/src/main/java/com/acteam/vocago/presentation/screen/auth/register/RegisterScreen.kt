package com.acteam.vocago.presentation.screen.auth.register

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.BackButton
import com.acteam.vocago.presentation.screen.auth.register.component.RegisterForm
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
fun RegisterScreen(
    viewModel: RegisterViewModel,
    authNavController: NavController
) {
    val uiState by viewModel.registerUIState.collectAsState()
    val formState by viewModel.registerFormState.collectAsState()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val titleFontSize = responsiveSP(mobile = 30, tabletPortrait = 36, tabletLandscape = 42)
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val verticalSpacing = responsiveDP(mobile = 12, tabletPortrait = 20, tabletLandscape = 24)
    val buttonHeight = responsiveDP(mobile = 48, tabletPortrait = 56, tabletLandscape = 60)
    val descFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)
    val deviceType = getDeviceType()
    val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeBottomDp = with(LocalDensity.current) { imeBottomPx.toDp() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .padding(bottom = imeBottomDp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(verticalSpacing)
        ) {
            RegisterHeader(authNavController, titleFontSize)

            if (deviceType == DeviceType.TabletPortrait) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AuthImageCard(R.drawable.register, 0.5f)
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            RegisterForm(viewModel, authNavController)

            Spacer(modifier = Modifier.height(verticalSpacing / 3)) // Use remembered verticalSpacing
            val currentEmailForNav = rememberUpdatedState(formState.email)

            Button(
                onClick = {
                    viewModel.register {
                        authNavController.navigate(
                            NavScreen.VerifyEmailNavScreen(email = currentEmailForNav.value)
                        ) {
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier
                    .height(buttonHeight)
                    .fillMaxWidth()
                    .shadow(8.dp, shape = RoundedCornerShape(24.dp))
            ) {
                Text(
                    text = stringResource(R.string.btn_sign_up).uppercase(),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            RegisterFooter(authNavController, descFontSize)

            Spacer(modifier = Modifier.height(verticalSpacing / 3))
        }
        when (val currentUiState = uiState) {
            is UIState.UIError -> {
                val messageResId = when (currentUiState.errorType) {
                    UIErrorType.ConflictError -> R.string.text_username_or_email_already_exists
                    UIErrorType.ServerError -> R.string.text_server_error
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
            }

            is UIState.UILoading -> {
                val loadingPicSize = responsiveValue(180, 360, 360)
                LoadingSurface(picSize = loadingPicSize)
            }

            else -> {
            }
        }
    }
}

@Composable
fun RegisterHeader(navController: NavController, fontSize: TextUnit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(
            onClick = {
                navController.navigate(NavScreen.LoginNavScreen) {
                    popUpTo(NavScreen.RegisterNavScreen) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.btn_sign_up),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = fontSize),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(40.dp))
    }
}

@Composable
fun RegisterFooter(navController: NavController, descFontSize: TextUnit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.text_already_have_account),
            fontSize = descFontSize
        )
        Text(
            text = stringResource(R.string.btn_login),
            modifier = Modifier
                .safeClickable("btn_login") {
                    navController.navigate(NavScreen.LoginNavScreen) {
                        popUpTo(NavScreen.RegisterNavScreen) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                .padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                fontSize = descFontSize
            )
        )
    }
}

