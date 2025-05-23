package com.acteam.vocago.presentation.screen.main.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSearch: () -> Unit,
) {
    val responsiveDPValue = responsiveDP(56, 56, 60)

    val genres = listOf(
        "Anime", "Học tập", "Cuộc sống", "Truyện tranh",
        "Thiếu nhi", "Giải trí", "Kinh dị", "Tình cảm"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(
            initialScale = 0.3f
        ),
        exit = fadeOut() + scaleOut(
            targetScale = 0.3f
        )
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(
                            16.dp
                        )
                    )
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            responsiveDPValue
                        )
                        .clip(
                            RoundedCornerShape(
                                responsiveDPValue
                            )
                        )
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.3f
                            ),
                            RoundedCornerShape(
                                responsiveDPValue
                            )
                        )
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        placeholder = { Text("Search") },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = responsiveSP(14, 20, 20)
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "key_search",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        keyboardActions = KeyboardActions(onDone = {

                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent
                        )
                    )
                }



                Spacer(modifier = Modifier.height(20.dp))

                // Các button thể loại
                Text(
                    text = "Chọn thể loại",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    genres.forEach { genre ->
                        Button(
                            onClick = {}
                        ) {
                            Text(
                                text = genre,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nút Hủy và Tìm
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Huỷ")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onSearch,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Tìm")
                    }
                }
            }
        }
    }
}

