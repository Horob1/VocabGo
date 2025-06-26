package com.acteam.vocago.presentation.screen.dictionary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(viewModel: DictionaryViewModel) {
    var active by remember { mutableStateOf(false) }

    val searchText by viewModel.searchText.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            DockedSearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchText,
                        onQueryChange = { viewModel.onSearchTextChange(it) },
                        onSearch = { active = false },
                        expanded = active,
                        onExpandedChange = { active = it },
                        enabled = true,
                        placeholder = { Text(stringResource(R.string.text_search)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        trailingIcon = null,
                        colors = TextFieldDefaults.colors(),
                        interactionSource = null,
                    )
                },
                expanded = active,
                onExpandedChange = { active = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = SearchBarDefaults.dockedShape,
                colors = SearchBarDefaults.colors(),
                tonalElevation = SearchBarDefaults.TonalElevation,
                shadowElevation = SearchBarDefaults.ShadowElevation,
                content = {
                    if (suggestions.isEmpty()) {
                        Text(
                            "No suggestions.",
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn {
                            items(suggestions) { word ->
                                Text(
                                    text = word,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.onSearchTextChange(word)
                                            active = false
                                        }
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                },
            )

            Text(
                text = "Welcome to your dictionary!",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
