package com.acteam.vocago.presentation.screen.auth.login.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.login.LoginViewModel
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.utils.CredentialHelper
import com.acteam.vocago.utils.autofill
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP
import com.acteam.vocago.utils.safeClickable
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginForm(
    viewModel: LoginViewModel,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    val context = LocalContext.current
    val formState by viewModel.loginFormState.collectAsState()
    val uiState by viewModel.loginUIState.collectAsState()
    val scope = rememberCoroutineScope()


    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val textFieldFontSize = responsiveSP(14, 20, 20)

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
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
                            shape = RoundedCornerShape(topStart = 12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                OutlinedTextField(
                    value = formState.username,
                    onValueChange = { viewModel.setUsername(it) },
                    placeholder = { Text(stringResource(R.string.input_enter_username)) },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = textFieldFontSize
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(usernameFocusRequester)
                        .onFocusChanged(
                            onFocusChanged = {
                                if (it.isFocused && formState.username == "") {
                                    scope.launch {
                                        CredentialHelper.getCredential(context) { username, password ->
                                            viewModel.setUsername(username)
                                            viewModel.setPassword(password)
                                            passwordFocusRequester.requestFocus()
                                            onLoginClick()
                                        }
                                    }
                                }
                            }
                        )
                        .autofill(
                            autofillType = listOf(AutofillType.Username, AutofillType.EmailAddress),
                            onFill = {
                                viewModel.setUsername(it)
                            },
                        )
                        .fillMaxHeight(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        passwordFocusRequester.requestFocus()
                    }),
                    readOnly = uiState is UIState.UILoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    )
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
                            shape = RoundedCornerShape(bottomStart = 12.dp)
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
                    onValueChange = { viewModel.setPassword(it) },
                    placeholder = { Text(stringResource(R.string.input_enter_password)) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = textFieldFontSize
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(passwordFocusRequester)
                        .autofill(
                            autofillType = listOf(AutofillType.Password),
                            onFill = {
                                viewModel.setPassword(it)
                            }
                        )
                        .fillMaxHeight(),
                    visualTransformation = if (formState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    readOnly = uiState is UIState.UILoading,
                    trailingIcon = {
                        val image =
                            if (formState.passwordVisible) R.drawable.hidden else R.drawable.view
                        val description =
                            if (formState.passwordVisible) "Hidden password" else "Show password"

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
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (formState.isLoginButtonEnabled && uiState !is UIState.UILoading) {
                            onLoginClick()
                        }
                    })
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                stringResource(R.string.btn_forgot_password),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = responsiveSP(14, 20, 20)
                ),
                modifier = Modifier
                    .safeClickable(
                        "btn_forgot_password",
                        onClick = {
                            if (uiState !is UIState.UILoading) onForgotPasswordClick()
                        }
                    )
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier
                .height(responsiveDP(48, 56, 60))
                .fillMaxWidth()
                .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
            onClick = {
                if (formState.isLoginButtonEnabled && uiState !is UIState.UILoading) {
                    onLoginClick()
                } else if (!viewModel.loginFormState.value.isLoginButtonEnabled) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.text_please_type_username_and_password),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        ) {
            Text(
                stringResource(R.string.btn_login).uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}