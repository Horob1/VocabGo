package com.acteam.vocago.presentation.screen.dictionary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.data.model.WordOfTheDaySimpleDto
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.worddetail.WordDetailViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DictionaryScreen(
    viewModel: WordDetailViewModel,
    rootNavController: NavController,
) {
    val context = LocalContext.current
    var active by remember { mutableStateOf(false) }

    val searchText by viewModel.searchText.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val selectedWord by viewModel.selectedWord.collectAsState()
    val recentWords by viewModel.recentWords.collectAsState()
    val wordOfTheDayState by viewModel.wordOfTheDayState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadRecentSearches(context)
        viewModel.loadWordOfTheDay()
    }

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
                        onSearch = {
                            viewModel.onSuggestionClick(context, searchText)
                            active = false
                        },
                        expanded = active,
                        onExpandedChange = { active = it },
                        enabled = true,
                        placeholder = { Text(stringResource(R.string.text_search)) },
                        leadingIcon = {
                            IconButton(onClick = { rootNavController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.onSuggestionClick(context, searchText)
                                    active = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            }
                        },
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
                            stringResource(R.string.text_no_suggestions),
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
                                            viewModel.onSuggestionClick(context, word)
                                            active = false
                                        }
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.text_word_of_the_day),
                    style = MaterialTheme.typography.titleMedium
                )
                when (val state = wordOfTheDayState) {
                    is UIState.UISuccess<*> -> {
                        val wordData = state.data as WordOfTheDaySimpleDto
                        WordOfTheDayCard(
                            wordData = wordData,
                            onViewAllClick = {
                                rootNavController.navigate(NavScreen.WordDetailNavScreen(wordData.word))
                            },
                            onNavigateToChooseVocaList = { word ->
                                //rootNavController.navigate(NavScreen.ChooseVocaListNavScreen(word))
                            },
                        )
                    }

                    is UIState.UILoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UIState.UIError -> {
                        Text(
                            stringResource(R.string.text_failed_loading_wotd),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.text_recently),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    recentWords.forEach { word ->
                        Surface(
                            tonalElevation = 2.dp,
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 8.dp)
                                .clickable {
                                    viewModel.onSuggestionClick(context, word)
                                    active = false
                                }
                        ) {
                            Text(
                                text = word,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

        }
    }
    selectedWord?.let { word ->
        rootNavController.navigate(NavScreen.WordDetailNavScreen(word))
        viewModel.clearSelectedWord()
    }
}
