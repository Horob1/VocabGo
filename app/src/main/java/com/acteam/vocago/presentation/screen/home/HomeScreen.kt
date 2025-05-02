package com.acteam.vocago.presentation.screen.home


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Home(
    onLoginClick: () -> Unit,

    ) {
    Button(
        modifier = Modifier.padding(50.dp),
        onClick = {
            onLoginClick()
        }
    ) {
        Text("Login")
    }
}