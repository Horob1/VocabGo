package com.acteam.vocago.presentation.screen.main.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.NewsCategory
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterDialog(
    isVisible: Boolean,
    chosenCategory: List<String>,
    onUpdateChosenCategory: (List<String>) -> Unit,
    onDismiss: () -> Unit,
    onSearch: () -> Unit,
) {
    val responsiveDPValue = responsiveDP(56, 56, 60)
    val categories = listOf(
        NewsCategory.NEWS,
        NewsCategory.LIFE,
        NewsCategory.TECH,
        NewsCategory.SPORT,
        NewsCategory.WORLD,
        NewsCategory.BUSINESS,
        NewsCategory.PERPECTIVES,
        NewsCategory.TRAVEL,
    )

    AnimatedVisibility(
        visible = isVisible,
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(24.dp)
            ) {
                // Search Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(responsiveDPValue)
                        .clip(RoundedCornerShape(responsiveDPValue))
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                        .padding(horizontal = 16.dp),
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text(stringResource(R.string.text_search)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxSize(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = responsiveSP(
                                16,
                                20,
                                20
                            )
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Title
                Text(
                    text = stringResource(R.string.text_choose_categories),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Categories FlowRow
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    categories.forEach { category ->
                        val isChosen = chosenCategory.contains(category.value)
                        OutlinedButton(
                            onClick = {
                                val list = chosenCategory.toMutableList()
                                if (isChosen) list.remove(category.value)
                                else list.add(category.value)
                                onUpdateChosenCategory(list)
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isChosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                contentColor = if (isChosen) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            ),
                            border = if (isChosen) BorderStroke(
                                0.dp,
                                Color.Transparent
                            ) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Text(
                                text = stringResource(category.stringResId),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            stringResource(R.string.btn_cancel),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Button(
                        onClick = onSearch,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            stringResource(R.string.text_search),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
