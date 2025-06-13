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
import androidx.compose.runtime.key // Import for key
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

// --- Optimizations: Define constants outside the Composable ---
// These values do not change based on Composable inputs or state,
// so defining them once avoids re-creation on every recomposition.
private val GENDER_OPTIONS = listOf("Nam", "Nữ", "Khác")
private val GENDER_MAP = mapOf(
    "Nam" to "MALE",
    "Nữ" to "FEMALE",
    "Khác" to "OTHER"
)

// Inverting the map once and reusing it is more efficient than doing it on every recomposition.
private val REVERSE_GENDER_MAP = GENDER_MAP.entries.associate { (k, v) -> v to k }

private val GENDER_DROPDOWN_SHAPE = RoundedCornerShape(12.dp)
private val GENDER_ICON_BACKGROUND_SHAPE = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)


@Composable
fun GenderDropdown(
    viewModel: RegisterViewModel
) {
    val formState by viewModel.registerFormState.collectAsState()
    val selectedGender = formState.gender
    var expanded by remember { mutableStateOf(false) }

    val textFieldFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(GENDER_DROPDOWN_SHAPE)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                GENDER_DROPDOWN_SHAPE
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
                        GENDER_ICON_BACKGROUND_SHAPE
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
                    text = REVERSE_GENDER_MAP[selectedGender] ?: "Chọn giới tính",
                    color = if (selectedGender.isNotEmpty()) MaterialTheme.colorScheme.onSurface else Color.Gray,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = textFieldFontSize
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Mở danh sách chọn giới tính"
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(responsiveValue(90, 90, 40) / 100f)
        ) {
            GENDER_OPTIONS.forEach { gender ->
                key(gender) {
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            val genderCode = GENDER_MAP[gender] ?: "OTHER"
                            viewModel.setGender(genderCode)
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}