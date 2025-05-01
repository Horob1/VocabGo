@file:Suppress("DEPRECATED_IDENTITY_EQUALS")

package com.acteam.vocago.presentation.screen.welcome.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R


@Composable
fun PageController(
    pageSize: Int,
    current: Int,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onSkip: () -> Unit,
    onClickLogin: () -> Unit
) {
    val buttonModifier = Modifier.width(
        140.dp
    )
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedVisibility(
            visible = current > 0,
        ) {
            Button(
                modifier = buttonModifier,
                onClick = {
                    if (current == 0)
                        return@Button
                    else if (current == pageSize - 1)
                        onSkip()
                    else
                        onPrev()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    if (current == pageSize - 1)
                        stringResource(R.string.btn_skip)
                    else
                        stringResource(R.string.btn_prev)
                )
            }
        }



        Spacer(modifier = Modifier.padding(horizontal = 16.dp))



        Button(
            modifier = buttonModifier,
            onClick = {
                if (current == pageSize - 1)
                    onClickLogin()
                else
                    onNext()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                if (current == pageSize - 1)
                    stringResource(R.string.btn_login)
                else
                    stringResource(R.string.btn_next)
            )
        }

    }
}