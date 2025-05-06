package com.acteam.vocago.presentation.screen.auth.register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun RegisterForm() {
    var text by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val passwordFocusRequester = remember { FocusRequester() }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val deviceType = getDeviceType()
    val textFieldFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(
                    responsiveDP(
                        8, 16, 16
                    )
                )
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
                                shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        placeholder = { Text(stringResource(R.string.input_first_name)) },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = textFieldFontSize
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {

                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent
                        )
                    )

                    VerticalDivider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp),
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.5f
                        )
                    )

                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        placeholder = { Text(stringResource(R.string.input_last_name)) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = textFieldFontSize
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {

                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent
                        )
                    )
                }

                CommonTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = stringResource(R.string.input_enter_username),
                    icon = Icons.Default.Person
                )

                CommonTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = stringResource(R.string.input_enter_email),
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                CommonTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = stringResource(R.string.input_enter_phone),
                    icon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                )

                CommonTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = stringResource(R.string.input_enter_address),
                    icon = Icons.Default.LocationOn,
                    keyboardType = KeyboardType.Text
                )
                DateInputField()

                GenderDropdown()
                PasswordTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = stringResource(R.string.input_enter_password),
                    isPasswordVisible = passwordVisible,
                    onVisibilityChange = { passwordVisible = !passwordVisible },
                    focusRequester = passwordFocusRequester
                )
                PasswordTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = stringResource(R.string.input_confirm_password),
                    isPasswordVisible = confirmPasswordVisible,
                    onVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible },
                    focusRequester = passwordFocusRequester
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        responsiveDP(
                            8, 16, 16
                        )
                    )
                ) {
                    CommonTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = stringResource(R.string.input_first_name),
                        icon = Icons.Default.Face
                    )

                    CommonTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = stringResource(R.string.input_last_name),
                        icon = Icons.Default.Face
                    )

                    CommonTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = stringResource(R.string.input_enter_username),
                        icon = Icons.Default.Person
                    )

                    CommonTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = stringResource(R.string.input_enter_email),
                        icon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email
                    )

                    CommonTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = stringResource(R.string.input_enter_phone),
                        icon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone
                    )

                }
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(24.dp),
                    color = MaterialTheme.colorScheme.primary.copy(
                        alpha = 1f
                    )
                )
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        responsiveDP(
                            8, 16, 16
                        )
                    )
                ) {
                    CommonTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = stringResource(R.string.input_enter_address),
                        icon = Icons.Default.LocationOn,
                        keyboardType = KeyboardType.Text
                    )
                    DateInputField()

                    GenderDropdown()

                    PasswordTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = stringResource(R.string.input_enter_password),
                        isPasswordVisible = passwordVisible,
                        onVisibilityChange = { passwordVisible = !passwordVisible },
                        focusRequester = passwordFocusRequester
                    )
                    PasswordTextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = stringResource(R.string.input_confirm_password),
                        isPasswordVisible = confirmPasswordVisible,
                        onVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible },
                        focusRequester = passwordFocusRequester
                    )
                }
            }
        }
    }
}