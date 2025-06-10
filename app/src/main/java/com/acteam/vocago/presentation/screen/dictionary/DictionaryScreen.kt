package com.acteam.vocago.presentation.screen.dictionary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

fun DictionaryScreen() {
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

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
                        onQueryChange = { searchText = it },
                        onSearch = { active = false },
                        expanded = active,
                        onExpandedChange = {
                            active = it
                        },
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
                onExpandedChange = {
                    active = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = SearchBarDefaults.dockedShape,
                colors = SearchBarDefaults.colors(),
                tonalElevation = SearchBarDefaults.TonalElevation,
                shadowElevation = SearchBarDefaults.ShadowElevation,
                content = {
                    // This content is displayed when the search bar is active/expanded
                    Text(
                        "Search suggestions or recent searches will appear here.",
                        modifier = Modifier.padding(16.dp)
                    )
                    // You would typically have a LazyColumn or similar here to display actual results
                },
            )

            // Your main dictionary content goes here
            Text(
                text = "Welcome to your dictionary!",
                modifier = Modifier.padding(16.dp)
            )
            // Example of a list of words or definitions
            // LazyColumn {
            //     items(yourDictionaryWords) { word ->
            //         Text(word.name)
            //     }
            // }
        }
    }
}