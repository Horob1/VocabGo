package com.acteam.vocago.presentation.screen.user.profile

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.R
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

@Composable
fun ChangePasswordScreen(
    viewModel: ProfileViewModel,
    navController: NavController,
) {
    val uiState by viewModel.changePasswordUIState.collectAsState()
    val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeBottomDp = with(LocalDensity.current) { imeBottomPx.toDp() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val deviceType = getDeviceType()
    val buttonHeight = responsiveDP(48, 56, 60)
    val titleFontSize = responsiveSP(30, 36, 42)
    val textFontSize = responsiveSP(20, 24, 24)
    val descFontSize = responsiveSP(14, 20, 20)
    val verticalSpacing = responsiveDP(12, 20, 24)
    val topPadding = responsiveDP(16, 24, 28)
    val horizontalPadding = responsiveDP(24, 40, 48)
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
                        .padding(bottom = imeBottomDp)
                        .fillMaxSize()
                        .then(
                            if (enableVerticalScroll) Modifier.verticalScroll(scrollState)
                            else Modifier
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing)
                ) {
                    TopBar(
                        text = stringResource(R.string.text_change_password),
                        fontSize = titleFontSize,
                        onBackClick = { navController.popBackStack() }
                    )
                    AuthImageCard(R.drawable.resetpassword, 0.7f)
                    Spacer(modifier = Modifier.weight(1f))
                    CommonContent(
                        viewModel = viewModel,
                        focusManager = focusManager,
                        navController = navController,
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
                            onBackClick = { navController.popBackStack() }
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
                            text = stringResource(R.string.text_change_password),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = titleFontSize
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = horizontalPadding / 3)
                        )
                        CommonContent(
                            viewModel = viewModel,
                            focusManager = focusManager,
                            navController = navController,
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
                    UIErrorType.ForbiddenError -> R.string.text_password_not_correct
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
    viewModel: ProfileViewModel,
    focusManager: FocusManager,
    navController: NavController,
    textFontSize: TextUnit,
    descFontSize: TextUnit,
    verticalSpacing: Dp,
    buttonHeight: Dp,
    context: Context,
    deviceType: DeviceType
) {
    val formState by viewModel.changePasswordFormState.collectAsState()

    val onSaveChangeClick = remember(viewModel, navController, focusManager) {
        {
            if (formState.isChangePasswordButtonEnabled) {
                viewModel.changePassword {
                    navController.popBackStack()
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

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ChangePasswordForm(
            viewModel = viewModel,
            onSaveChangeClick = onSaveChangeClick
        )
        Spacer(modifier = Modifier.height(verticalSpacing * 3))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight),
            onClick = onSaveChangeClick
        ) {
            Text(
                text = stringResource(R.string.btn_save_change).uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(verticalSpacing / 3))
    }
}

private val topRoundedCornerShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
private val bottomRoundedCornerShape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
private val noRoundedCornerShape = RoundedCornerShape(0.dp)

@Composable
fun ChangePasswordForm(
    viewModel: ProfileViewModel,
    onSaveChangeClick: () -> Unit
) {
    val oldPasswordFocusRequester = remember { FocusRequester() }
    val newPasswordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        oldPasswordFocusRequester.requestFocus()
    }

    val uiState by viewModel.changePasswordUIState.collectAsState()
    val formState by viewModel.changePasswordFormState.collectAsState()

    val primaryBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = Color.Transparent,
        disabledBorderColor = Color.Transparent
    )

    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = bottomRoundedCornerShape)
    ) {
        PasswordRow(
            labelRes = R.string.input_enter_old_password,
            value = formState.oldPassword,
            onValueChange = { viewModel.setOldPassword(it) },
            isVisible = formState.isOldPasswordVisible,
            onToggleVisibility = { viewModel.toggleOldPasswordVisibility() },
            focusRequester = oldPasswordFocusRequester,
            nextFocusRequester = newPasswordFocusRequester,
            textFieldColors = textFieldColors,
            surfaceColor = surfaceVariantColor,
            borderColor = primaryBorderColor,
            backgroundColor = primaryColor,
            iconColor = onPrimaryColor,
            shape = topRoundedCornerShape
        )

        PasswordRow(
            labelRes = R.string.input_enter_new_password,
            value = formState.password,
            onValueChange = { viewModel.setPassword(it) },
            isVisible = formState.isPasswordVisible,
            onToggleVisibility = { viewModel.togglePasswordVisibility() },
            focusRequester = newPasswordFocusRequester,
            nextFocusRequester = confirmPasswordFocusRequester,
            textFieldColors = textFieldColors,
            surfaceColor = surfaceVariantColor,
            borderColor = primaryBorderColor,
            backgroundColor = primaryColor,
            iconColor = onPrimaryColor,
            shape = noRoundedCornerShape
        )

        PasswordRow(
            labelRes = R.string.input_confirm_new_password,
            value = formState.confirmPassword,
            onValueChange = { viewModel.setConfirmPassword(it) },
            isVisible = formState.isConfirmPasswordVisible,
            onToggleVisibility = { viewModel.toggleConfirmPasswordVisibility() },
            focusRequester = confirmPasswordFocusRequester,
            nextFocusRequester = null,
            textFieldColors = textFieldColors,
            surfaceColor = surfaceVariantColor,
            borderColor = primaryBorderColor,
            backgroundColor = primaryColor,
            iconColor = onPrimaryColor,
            readOnly = uiState is UIState.UILoading,
            onDone = {
                if (formState.isChangePasswordButtonEnabled && uiState !is UIState.UILoading) {
                    onSaveChangeClick()
                }
            },
            shape = bottomRoundedCornerShape
        )
    }
}

@Composable
fun PasswordRow(
    @StringRes labelRes: Int,
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    focusRequester: FocusRequester,
    nextFocusRequester: FocusRequester? = null,
    textFieldColors: TextFieldColors,
    surfaceColor: Color,
    borderColor: Color,
    backgroundColor: Color,
    iconColor: Color,
    readOnly: Boolean = false,
    onDone: (() -> Unit)? = null,
    shape: RoundedCornerShape
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = surfaceColor, shape = shape)
            .border(width = 1.dp, color = borderColor, shape = shape)
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .fillMaxHeight()
                .background(color = backgroundColor, shape = shape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = iconColor
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(stringResource(id = labelRes)) },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .fillMaxHeight(),
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = textFieldColors,
            trailingIcon = {
                PasswordTrailingIcon(
                    isPasswordVisible = isVisible,
                    onToggleVisibility = onToggleVisibility
                )
            },
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = if (nextFocusRequester != null) ImeAction.Next else ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { nextFocusRequester?.requestFocus() },
                onDone = { onDone?.invoke() }
            )
        )
    }
}

@Composable
private fun PasswordTrailingIcon(
    isPasswordVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    val imageResId = if (isPasswordVisible) R.drawable.hidden else R.drawable.view
    val painter = painterResource(id = imageResId)
    val description = if (isPasswordVisible) "Hide password" else "Show password"

    IconButton(onClick = onToggleVisibility) {
        Icon(
            painter = painter,
            contentDescription = description,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
        )
    }
}