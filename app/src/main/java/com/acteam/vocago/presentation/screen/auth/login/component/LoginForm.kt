package com.acteam.vocago.presentation.screen.auth.login.component

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R


@Composable
fun LoginForm() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val passwordFocusRequester = remember { FocusRequester() }
    Column {
        Column(
            modifier = Modifier
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .shadow(
                    6.dp,
                    RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                )
        ) {

            // Ô tài khoản với icon ở đầu
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
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    placeholder = { Text("Enter Username") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        passwordFocusRequester.requestFocus()
                    }),
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
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    placeholder = {
                        Text("Enter Password")
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(passwordFocusRequester)
                        .fillMaxHeight(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    trailingIcon = {
                        val image =
                            if (passwordVisible) R.drawable.hidden else R.drawable.view
                        val description =
                            if (passwordVisible) "Hidden password" else "Show password"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
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

                    }),
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                "Forgot Password?",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                ),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .height(40.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            onClick = {

            },
        ) {
            Text(
                "Sign In",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}