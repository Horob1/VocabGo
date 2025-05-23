package com.acteam.vocago.presentation.screen.auth.register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.utils.responsiveSP

@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier
) {
    val textFieldFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
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
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = textFieldFontSize
            ),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPasswordVisible: Boolean,
    onVisibilityChange: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester(),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val textFieldFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
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
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = textFieldFontSize
            ),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .fillMaxHeight(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            ),
            trailingIcon = {
                val image = if (isPasswordVisible) R.drawable.hidden else R.drawable.view
                val description = if (isPasswordVisible) "Hidden password" else "Show password"

                IconButton(onClick = onVisibilityChange) {
                    Icon(
                        painter = painterResource(id = image),
                        contentDescription = description,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = keyboardActions
        )
    }
}
