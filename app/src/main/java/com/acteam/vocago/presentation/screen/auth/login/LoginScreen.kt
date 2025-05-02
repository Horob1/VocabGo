package com.acteam.vocago.presentation.screen.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.BackButton
import com.acteam.vocago.presentation.screen.auth.common.PlatFormSignUpButton
import com.acteam.vocago.presentation.screen.auth.login.component.LoginForm

@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                BackButton(
                    onClick = onBackClick,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AuthImageCard(R.drawable.login, width = 0.8f)
            }

            LoginForm()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                )
                Text(
                    text = "or",
                    modifier = Modifier.padding(horizontal = 8.dp),

                    )
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                PlatFormSignUpButton(R.drawable.google, {})
                PlatFormSignUpButton(R.drawable.facebook, {})
                PlatFormSignUpButton(R.drawable.github, {})
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                Text(
                    text = "Sign Up",
                    modifier = Modifier.clickable(
                        enabled = true,
                        onClick = {

                        }
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}