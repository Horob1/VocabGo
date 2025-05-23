package com.acteam.vocago.presentation.screen.auth.register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.acteam.vocago.presentation.screen.auth.register.RegisterViewModel
import com.acteam.vocago.utils.responsiveSP
import com.acteam.vocago.utils.responsiveValue

@Composable
fun GenderDropdown(
    viewModel: RegisterViewModel
) {
    val genderOptions = listOf("Nam", "Nữ", "Khác")
    val genderMap = mapOf(
        "Nam" to "MALE",
        "Nữ" to "FEMALE",
        "Khác" to "OTHER"
    )
    val reverseGenderMap = genderMap.entries.associate { (k, v) -> v to k } // MALE -> Nam, ...

    val formState by viewModel.registerFormState.collectAsState()
    val selectedGender = formState.gender // MALE, FEMALE, OTHER
    var expanded by remember { mutableStateOf(false) }
    val textFieldFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .clickable { expanded = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = reverseGenderMap[selectedGender] ?: "Chọn giới tính",
                    color = if (selectedGender.isNotEmpty()) MaterialTheme.colorScheme.onSurface else Color.Gray,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = textFieldFontSize
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.fillMaxWidth(
                responsiveValue(90, 90, 40) / 100f
            )
        ) {
            genderOptions.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(gender) },
                    onClick = {
                        val genderCode = genderMap[gender] ?: "OTHER"
                        viewModel.setGender(genderCode)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
