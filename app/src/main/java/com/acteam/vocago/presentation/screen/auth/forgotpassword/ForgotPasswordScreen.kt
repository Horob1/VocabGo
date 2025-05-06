package com.acteam.vocago.presentation.screen.auth.forgotpassword

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.BackButton
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP


@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit, onResetPasswordClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    val emailFocusRequester = remember { FocusRequester() }
    val buttonHeight = responsiveDP(48, 56, 60)
    val focusManager = LocalFocusManager.current
    val deviceType = getDeviceType()

    val titleFontSize = responsiveSP(mobile = 30, tabletPortrait = 36, tabletLandscape = 42)
    val textFontSize = responsiveSP(mobile = 20, tabletPortrait = 24, tabletLandscape = 24)
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val verticalSpacing = responsiveDP(mobile = 12, tabletPortrait = 20, tabletLandscape = 24)
    val topPadding = responsiveDP(mobile = 16, tabletPortrait = 24, tabletLandscape = 28)
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
                    modifier = Modifier
                        .padding(horizontal = horizontalPadding)
                        .verticalScroll(scrollState)
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
                            text = stringResource(R.string.text_forgot_password),
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
                        AuthImageCard(R.drawable.forgotpassword, width = 0.8f)
                    }

                    Spacer(modifier = Modifier.height(verticalSpacing * 3))
                    Text(
                        text = stringResource(R.string.text_input_your_email),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = textFontSize,
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(verticalSpacing * 2))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp, MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.5f
                                ), RoundedCornerShape(12.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .fillMaxHeight()
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(12.dp)
                                ),
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
                            onValueChange = {
                                email = it
                            },
                            placeholder = { Text(stringResource(R.string.input_enter_email)) },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = responsiveSP(
                                    mobile = 14,
                                    tabletPortrait = 20,
                                    tabletLandscape = 20
                                )
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = {
                                emailFocusRequester.requestFocus()
                            }),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(verticalSpacing * 3))

                    Button(
                        modifier = Modifier
                            .height(buttonHeight)
                            .fillMaxWidth()
                            .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
                        onClick = {
                            onResetPasswordClick()
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.btn_send_email).uppercase(),
                            style = MaterialTheme.typography.titleMedium
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
                        Spacer(modifier = Modifier.height(verticalSpacing))
                        AuthImageCard(R.drawable.forgotpassword, 0.8f)
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
                            text = stringResource(R.string.text_forgot_password),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = titleFontSize
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(horizontal = horizontalPadding / 3)
                        )
                        Spacer(modifier = Modifier.height(verticalSpacing * 2))
                        Text(
                            text = stringResource(R.string.text_input_your_email),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = textFontSize,
                                textAlign = TextAlign.Center
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp, MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.5f
                                    ), RoundedCornerShape(12.dp)
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(48.dp)
                                    .fillMaxHeight()
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
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
                                onValueChange = {
                                    email = it
                                },
                                placeholder = { Text(stringResource(R.string.input_enter_email)) },
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = responsiveSP(
                                        mobile = 14,
                                        tabletPortrait = 20,
                                        tabletLandscape = 20
                                    )
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                                keyboardActions = KeyboardActions(onNext = {
                                    emailFocusRequester.requestFocus()
                                }),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent,
                                    disabledBorderColor = Color.Transparent
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(verticalSpacing * 3))
                        Button(
                            modifier = Modifier
                                .height(buttonHeight)
                                .fillMaxWidth()
                                .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
                            onClick = {
                                onResetPasswordClick()
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.btn_send_email).uppercase(),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }

    }
}
