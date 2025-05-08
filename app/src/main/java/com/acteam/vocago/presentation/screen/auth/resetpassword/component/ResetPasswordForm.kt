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

@Composable
fun ResetPasswordForm(
    viewModel: ResetPasswordViewModel,
    onSaveChangeClick: () -> Unit
) {
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    val uiState by viewModel.resetPasswordUIState.collectAsState()
    val formState by viewModel.resetPasswordFormState.collectAsState()
    Column {
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .shadow(
                    6.dp,
                    RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
                    .border(
                        1.dp, MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.5f
                        ), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                OutlinedTextField(
                    value = formState.password,
                    onValueChange = {
                       viewModel.setPassword(it)
                    },
                    placeholder = {
                        Text(stringResource(R.string.input_enter_password))
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(passwordFocusRequester)
                        .fillMaxHeight(),
                    visualTransformation = if (formState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    trailingIcon = {
                        val image =
                            if (formState.isPasswordVisible) R.drawable.hidden else R.drawable.view
                        val description =
                            if (formState.isPasswordVisible) "Hidden password" else "Show password"

                        IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                            Icon(
                                painter = painterResource(id = image),
                                contentDescription = description,
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(24.dp)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
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
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                    )
                    .border(
                        1.dp, MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.5f
                        ), RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                OutlinedTextField(
                    value = formState.confirmPassword,
                    onValueChange = {
                        viewModel.setConfirmPassword(it)
                    },
                    placeholder = {
                        Text(stringResource(R.string.input_confirm_password))
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(confirmPasswordFocusRequester)
                        .fillMaxHeight(),
                    visualTransformation = if (formState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    trailingIcon = {
                        val image =
                            if (formState.isConfirmPasswordVisible) R.drawable.hidden else R.drawable.view
                        val description =
                            if (formState.isConfirmPasswordVisible) "Hidden password" else "Show password"

                        IconButton(onClick = { viewModel.toggleConfirmPasswordVisibility() }) {
                            Icon(
                                painter = painterResource(id = image),
                                contentDescription = description,
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(24.dp)
                            )
                        }
                    },
                    readOnly = uiState is UIState.UILoading,
                    keyboardOptions = KeyboardOptions.Default.copy(
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
