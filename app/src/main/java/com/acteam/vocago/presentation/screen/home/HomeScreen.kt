package com.acteam.vocago.presentation.screen.home


import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Home(
    onLoginClick: () -> Unit,

    ) {
    Button(
        onClick = {
            onLoginClick()
        }
    ) {
        Text("Login")
    }
}