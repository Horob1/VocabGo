package com.acteam.vocago.presentation.screen.novelhistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.main.novel.component.NovelCard
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelHistoryScreen(
    viewModel: NovelHistoryViewModel,
    navController: NavController,
) {
    val novelPaging = viewModel.novelPagingFlow.collectAsStateWithLifecycle()
    val shortHeightDp = responsiveDP(
        mobile = 8,
        tabletPortrait = 12,
        tabletLandscape = 16
    )
    val superTitleFontSize = responsiveSP(
        mobile = 20,
        tabletPortrait = 24,
        tabletLandscape = 28
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                Modifier
                    .padding(
                        vertical = shortHeightDp
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }


                Text(
                    stringResource(R.string.title_read_novels),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = superTitleFontSize,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    ),
                )

                Spacer(
                    modifier = Modifier
                        .padding(
                            8.dp
                        )
                        .width(20.dp)
                )
            }


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {

                items(
                    count = novelPaging.value.size,
                    key = { index ->
                        novelPaging.value[index]._id
                    },
                    contentType = { "Novel card" }
                ) { index ->
                    val item = novelPaging.value[index]

                    NovelCard(
                        novel = item,
                        onClick = {
                            navController.navigate(
                                NavScreen.NovelDetailNavScreen(item._id)
                            )
                        }
                    )
                }
            }
        }
    }
}