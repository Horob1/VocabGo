package com.acteam.vocago.presentation.screen.main.novel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.common.rememberNetworkStatus
import com.acteam.vocago.presentation.screen.main.novel.component.NovelCarousel
import com.acteam.vocago.presentation.screen.main.novel.component.NovelList
import com.acteam.vocago.presentation.screen.main.novel.component.TabletCarousel
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NovelScreen(
    viewModel: NovelViewModel,
    rootNavController: NavController,
) {
    LocalContext.current
    val deviceType = getDeviceType()
    rememberNetworkStatus().value
    val novelFirstPageList by viewModel.novelFirstPage.collectAsState()
    val readNovelFirstPageList by viewModel.readNovelFirstPage.collectAsState()

    LaunchedEffect(Unit) {
        if (novelFirstPageList !is UIState.UISuccess) {
            viewModel.loadNovelFirstPage()
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadReadNovelFirstPage()
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
//
//    LaunchedEffect(isConnected) {
//        if (!isConnected && displayMode) {
//            viewModel.setConnectMode(false)
//            Toast.makeText(
//                context,
//                context.getString(R.string.alert_you_are_offline),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }

    if (
        novelFirstPageList is UIState.UILoading && readNovelFirstPageList is UIState.UILoading
    ) LoadingSurface(Modifier.fillMaxSize())

    if (novelFirstPageList is UIState.UISuccess)
        LazyColumn {
            stickyHeader {
                Row(
                    Modifier
                        .background(
                            MaterialTheme.colorScheme.background
                        )
                        .padding(
                            horizontal = responsiveDP(
                                mobile = 16,
                                tabletPortrait = 24,
                                tabletLandscape = 32
                            ),
                            vertical = responsiveDP(
                                mobile = 8,
                                tabletPortrait = 12,
                                tabletLandscape = 16
                            )
                        )
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(R.string.title_novel_center),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = responsiveSP(
                                    mobile = 20,
                                    tabletPortrait = 22,
                                    tabletLandscape = 24
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(
                            modifier = Modifier.size(
                                responsiveDP(
                                    mobile = 4,
                                    tabletPortrait = 8,
                                    tabletLandscape = 12
                                )
                            )
                        )
                        Text(
                            stringResource(R.string.title_learn_voca_by_novel),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    0.7f
                                )
                            )
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                rootNavController.navigate(NavScreen.SearchNovelNavScreen(""))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "key_search",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
//                    Spacer(
//                        modifier = Modifier.size(
//                            responsiveDP(
//                                mobile = 4,
//                                tabletPortrait = 8,
//                                tabletLandscape = 12
//                            )
//                        )
//                    )
//                    IconButton(
//                        onClick = {
//                            if (displayMode && isConnected) {
//                                viewModel.setConnectMode(false)
//                            } else if (!displayMode && isConnected) {
//                                viewModel.setConnectMode(true)
//                            } else {
//                                Toast.makeText(
//                                    context,
//                                    context.getString(R.string.alert_you_are_offline),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    ) {
//                        Icon(
//                            imageVector = if (!displayMode) Icons.Default.Wifi else Icons.Default.WifiOff,
//                            contentDescription = if (!displayMode) "Online" else "Offline",
//                            tint = MaterialTheme.colorScheme.onSurface,
//                            modifier = Modifier.padding(start = 4.dp)
//                        )
//                    }
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            if (deviceType == DeviceType.TabletLandscape) {
                item {
                    TabletCarousel(
                        novel = (novelFirstPageList as UIState.UISuccess).data,
                        onItemClick = {
                            rootNavController.navigate(NavScreen.NovelDetailNavScreen(it))
                        }
                    )
                }
                item {
                    HorizontalDivider()
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        NovelList(
                            modifier = Modifier.fillMaxWidth(),
                            title = R.string.title_new_novel,
                            novelList = (novelFirstPageList as UIState.UISuccess).data,
                            onNovelClick = {
                                rootNavController.navigate(NavScreen.NovelDetailNavScreen(it))
                            },
                            onGoToFullNovelList = {
                                rootNavController.navigate(NavScreen.SearchNovelNavScreen(""))
                            }
                        )

                        if (readNovelFirstPageList is UIState.UISuccess)
                        // Read novel
                            NovelList(
                                modifier = Modifier.fillMaxWidth(),
                                bgColorAlpha = 0.3f,
                                title = R.string.title_read_novels,
                                novelList = (readNovelFirstPageList as UIState.UISuccess).data,
                                onNovelClick = {
                                    rootNavController.navigate(NavScreen.NovelDetailNavScreen(it))
                                },
                                onGoToFullNovelList = {
                                    rootNavController.navigate(NavScreen.NovelHistoryNavScreen)
                                }
                            )
                    }
                }
            } else {
                item {
                    NovelCarousel(
                        novel = (novelFirstPageList as UIState.UISuccess).data,
                        onItemClick = {
                            rootNavController.navigate(NavScreen.NovelDetailNavScreen(it))
                        }
                    )
                }
                item {

                    NovelList(
                        modifier = Modifier.fillMaxWidth(),
                        title = R.string.title_new_novel,
                        novelList = (novelFirstPageList as UIState.UISuccess).data,
                        onNovelClick = {
                            rootNavController.navigate(NavScreen.NovelDetailNavScreen(it))
                        },
                        onGoToFullNovelList = {
                            rootNavController.navigate(NavScreen.SearchNovelNavScreen(""))
                        }
                    )

                }

                if (readNovelFirstPageList is UIState.UISuccess)
                // Read novel
                    item {
                        NovelList(
                            modifier = Modifier.fillMaxWidth(),
                            bgColorAlpha = 0.3f,
                            title = R.string.title_read_novels,
                            novelList = (readNovelFirstPageList as UIState.UISuccess).data,
                            onNovelClick = {
                                rootNavController.navigate(NavScreen.NovelDetailNavScreen(it))
                            },
                            onGoToFullNovelList = {
                                rootNavController.navigate(NavScreen.NovelHistoryNavScreen)
                            }
                        )
                    }
            }

        }
}