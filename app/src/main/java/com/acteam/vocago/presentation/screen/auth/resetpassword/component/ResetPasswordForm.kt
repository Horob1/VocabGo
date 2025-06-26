package com.acteam.vocago.presentation.screen.auth.resetpassword.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.resetpassword.ResetPasswordViewModel
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlin.system.measureTimeMillis

private val topRoundedCornerShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
private val bottomRoundedCornerShape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)

@Composable
fun ResetPasswordForm(
    viewModel: ResetPasswordViewModel,
    onSaveChangeClick: () -> Unit
) {
    measureTimeMillis {

        val passwordFocusRequester = remember { FocusRequester() }
        val confirmPasswordFocusRequester = remember { FocusRequester() }

        val uiState by viewModel.resetPasswordUIState.collectAsState()
        val formState by viewModel.resetPasswordFormState.collectAsState()

        val primaryBorderColor =
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)

        val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
        val primaryColor = MaterialTheme.colorScheme.primary
        val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

        val textFieldColors =
            OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            )

        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = bottomRoundedCornerShape
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        color = surfaceVariantColor,
                        shape = topRoundedCornerShape
                    )
                    .border(
                        width = 1.dp,
                        color = primaryBorderColor,
                        shape = topRoundedCornerShape
                    )
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                        .background(
                            color = primaryColor,
                            shape = topRoundedCornerShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = onPrimaryColor
                    )
                }

                OutlinedTextField(
                    value = formState.password,
                    onValueChange = { viewModel.setPassword(it) }, // This is fine, delegates to ViewModel
                    placeholder = {
                        Text(stringResource(R.string.input_enter_password))
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(passwordFocusRequester)
                        .fillMaxHeight(),
                    visualTransformation = if (formState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = textFieldColors,
                    trailingIcon = {
                        PasswordTrailingIcon(
                            isPasswordVisible = formState.isPasswordVisible,
                            onToggleVisibility = { viewModel.togglePasswordVisibility() }
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        confirmPasswordFocusRequester.requestFocus()
                    }),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        color = surfaceVariantColor,
                        shape = bottomRoundedCornerShape
                    )
                    .border(
                        width = 1.dp,
                        color = primaryBorderColor,
                        shape = bottomRoundedCornerShape
                    )
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                        .background(
                            color = primaryColor,
                            shape = bottomRoundedCornerShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = onPrimaryColor
                    )
                }

                OutlinedTextField(
                    value = formState.confirmPassword,
                    onValueChange = { viewModel.setConfirmPassword(it) },
                    placeholder = {
                        Text(stringResource(R.string.input_confirm_password))
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(confirmPasswordFocusRequester)
                        .fillMaxHeight(),
                    visualTransformation = if (formState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = textFieldColors,
                    trailingIcon = {
                        PasswordTrailingIcon(
                            isPasswordVisible = formState.isConfirmPasswordVisible,
                            onToggleVisibility = { viewModel.toggleConfirmPasswordVisibility() }
                        )
                    },
                    readOnly = uiState is UIState.UILoading,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (formState.isResetPasswordButtonEnabled && uiState !is UIState.UILoading) {
                            onSaveChangeClick()
                        }
                    }),
                )
            }
        }
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
                .height(24.dp) // Consider using Dp values from a theme/constants file
                .width(24.dp)
        )
    }
}