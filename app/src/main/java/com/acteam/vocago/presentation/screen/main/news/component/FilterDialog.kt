package com.acteam.vocago.presentation.screen.main.news.component

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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.NewsCategory
import com.acteam.vocago.presentation.screen.main.news.NewsViewModel
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun FilterDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    viewModel: NewsViewModel,
) {
    val chosenCategory by viewModel.chosenCategories.collectAsState()
    val search by viewModel.keySearch.collectAsState()

    val tempChosenCategory = remember { mutableStateListOf<String>() }
    var keySearch by remember { mutableStateOf("") }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            tempChosenCategory.clear()
            tempChosenCategory.addAll(chosenCategory)
            keySearch = search
        }
    }

    val categories = remember {
        listOf(
            NewsCategory.NEWS,
            NewsCategory.LIFE,
            NewsCategory.TECH,
            NewsCategory.SPORT,
            NewsCategory.WORLD,
            NewsCategory.BUSINESS,
            NewsCategory.PERSPECTIVES,
            NewsCategory.TRAVEL,
        )
    }

    if (isVisible) {
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
                SearchBox(
                    keySearch = keySearch,
                    onValueChange = { keySearch = it },
                    onDone = {
                        viewModel.updateChosenCategories(tempChosenCategory)
                        viewModel.setKeySearch(keySearch)
                        onDismiss()
                    }
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.text_choose_categories),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                CategoriesFlowRow(
                    categories = categories,
                    chosenCategories = tempChosenCategory,
                    onCategoryToggle = { category ->
                        if (tempChosenCategory.contains(category)) tempChosenCategory.remove(
                            category
                        )
                        else tempChosenCategory.add(category)
                    }
                )

                Spacer(Modifier.height(32.dp))

                DialogButtons(
                    onCancel = onDismiss,
                    onSearch = {
                        viewModel.updateChosenCategories(tempChosenCategory)
                        viewModel.setKeySearch(keySearch)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchBox(
    keySearch: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    val height = responsiveDP(56, 56, 60)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(horizontal = 16.dp),
    ) {
        OutlinedTextField(
            value = keySearch,
            onValueChange = onValueChange,
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
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = responsiveSP(16, 20, 20)
            )
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoriesFlowRow(
    categories: List<NewsCategory>,
    chosenCategories: List<String>,
    onCategoryToggle: (String) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        categories.forEach { category ->
            val isChosen = chosenCategories.contains(category.value)
            OutlinedButton(
                onClick = { onCategoryToggle(category.value) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isChosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (isChosen) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                ),
                border = if (isChosen) null else BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline
                )
            ) {
                Text(
                    text = stringResource(category.stringResId),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun DialogButtons(
    onCancel: () -> Unit,
    onSearch: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
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
