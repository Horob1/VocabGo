package com.acteam.vocago.presentation.screen.readnovel

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.NovelDetail
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.readnovel.component.ColorPicker
import com.acteam.vocago.presentation.screen.readnovel.component.ContentSection
import com.acteam.vocago.presentation.screen.readnovel.component.EnableImmersiveMode
import com.acteam.vocago.presentation.screen.readnovel.component.FontPicker
import com.acteam.vocago.presentation.screen.readnovel.component.FontSizeAdjuster
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadNovelScreen(
    chapterId: String,
    novelId: String,
    viewModel: ReadNovelViewModel,
    rootNavController: NavController,
) {
    val chapters by viewModel.chapters.collectAsState()
    val novelDetail by viewModel.novelDetail.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }
    var showSettingDialog by remember { mutableStateOf(false) }
    val fontSize by viewModel.fontSize.collectAsState()
    val theme by viewModel.theme.collectAsState()
    val fontFamily by viewModel.fontFamily.collectAsState()
    val context = LocalContext.current

    val laucher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            rootNavController.navigate(
                NavScreen.ListenNovelNavScreen(
                    novelId = novelId,
                    chapterId = chapterId
                )
            )
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.alert_permission_diened),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    EnableImmersiveMode(true)

    // Chặn nút back
    BackHandler {
        showExitDialog = true
    }

    LaunchedEffect(Unit) {
        if (novelDetail !is UIState.UISuccess) {
            viewModel.loadNovel(novelId)
        }
    }

    LaunchedEffect(novelDetail) {
        if (novelDetail !is UIState.UISuccess) {
            viewModel.loadChapters(chapterId) {
                // toast error
                Log.e("ReadNovelScreen", "Error loading chapters")
            }
        }
    }

    Scaffold(
        containerColor = theme.backgroundColor,
        contentColor = theme.textColor,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (novelDetail is UIState.UISuccess) {
                val novelData = remember {
                    (novelDetail as UIState.UISuccess<NovelDetail>).data
                }
                // Horizontally Pager
                val pagerState = rememberPagerState(
                    initialPage = novelData.chapters.indexOfFirst {
                        it._id == chapterId
                    },
                    pageCount = {
                        novelData.chapters.size
                    }
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { index ->
                    val _id = novelData.chapters[index]._id
                    val chapter = chapters.find { it.chapter._id == _id }
                    if (chapter != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                        ) {
                            // Enhanced header with better styling
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = theme.backgroundColor.copy(alpha = 0.8f)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = theme.backgroundColor.copy(alpha = 0.8f)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Enhanced back button
                                    IconButton(
                                        onClick = {
                                            rootNavController.popBackStack()
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape),
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = theme.textColor.copy(alpha = 0.1f)
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                            contentDescription = "Back",
                                            tint = theme.textColor.copy(alpha = 0.7f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    // Enhanced chapter title
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "${
                                                stringResource(R.string.chapters).replaceFirstChar {
                                                    if (it.isLowerCase()) it.titlecase(
                                                        Locale.getDefault()
                                                    ) else it.toString()
                                                }
                                            } ${novelData.chapters[index].chapterNumber}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = theme.textColor.copy(alpha = 0.8f),
                                            fontFamily = fontFamily.fontFamily,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = novelData.chapters[index].chapterTitle,
                                            fontSize = 12.sp,
                                            color = theme.textColor.copy(alpha = 0.6f),
                                            fontFamily = fontFamily.fontFamily,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    // Enhanced settings button
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                showSettingDialog = true
                                            },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape),
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = theme.textColor.copy(alpha = 0.1f)
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Settings,
                                                contentDescription = "Setting",
                                                tint = theme.textColor.copy(alpha = 0.7f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                val hasPermission =
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                        ContextCompat.checkSelfPermission(
                                                            context,
                                                            Manifest.permission.POST_NOTIFICATIONS
                                                        ) == PackageManager.PERMISSION_GRANTED
                                                    } else {
                                                        true
                                                    }

                                                if (hasPermission) {
                                                    rootNavController.navigate(
                                                        NavScreen.ListenNovelNavScreen(
                                                            novelId = novelId,
                                                            chapterId = chapterId
                                                        )
                                                    )
                                                } else {
                                                    laucher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                                }
                                            },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape),
                                            colors = IconButtonDefaults.iconButtonColors(
                                                containerColor = theme.textColor.copy(alpha = 0.1f)
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                                contentDescription = "Listen",
                                                tint = theme.textColor.copy(alpha = 0.7f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            ContentSection(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                chapter = chapter,
                                fontSize = fontSize,
                                theme = theme,
                                author = novelData.author,
                                fontFamily = fontFamily.fontFamily,
                                wordUiState = viewModel.wordUiState.collectAsState().value,
                                setSelectedWord = {
                                    viewModel.getWordDetail(it)
                                },
                                navController = rootNavController,
                            )
                        }
                    } else {
                        LoadingSurface(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                LaunchedEffect(pagerState.currentPage) {
                    viewModel.loadChapters(novelData.chapters[pagerState.currentPage]._id) {
                        // toast error
                    }
                    viewModel.updateReadChapter(
                        novelData.chapters[pagerState.currentPage]._id,
                        novelData._id
                    )
                }

            } else {
                LoadingSurface(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Enhanced exit dialog
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = {
                    Text(
                        text = stringResource(R.string.title_confirm_to_exit),
                        fontWeight = FontWeight.SemiBold,
                        color = theme.textColor
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.desc_confirm_to_exit),
                        color = theme.textColor.copy(
                            alpha = 0.6f
                        )
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            rootNavController.popBackStack()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.btn_exit),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text(
                            text = stringResource(R.string.btn_cancel),
                            fontWeight = FontWeight.Medium,
                            color = theme.textColor
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = theme.backgroundColor,
            )
        }

        // Enhanced settings modal
        if (showSettingDialog) {
            ModalBottomSheet(
                onDismissRequest = { showSettingDialog = false },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                ),
                containerColor = theme.backgroundColor,
                contentColor = theme.textColor,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    // Enhanced settings header
                    Text(
                        text = stringResource(R.string.title_setting),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center,
                        color = theme.textColor
                    )

                    // Settings sections with enhanced styling
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        FontSizeAdjuster(
                            maxValue = 28f,
                            minValue = 12f,
                            value = fontSize,
                            onValueChange = {
                                viewModel.setFontSize(it) {}
                            },
                            modifier = Modifier.padding(16.dp),
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        ColorPicker(
                            modifier = Modifier.padding(16.dp),
                            selectedColor = theme,
                            onColorSelected = {
                                viewModel.setTheme(it.name)
                            },
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        FontPicker(
                            modifier = Modifier.padding(16.dp),
                            selectedFont = fontFamily,
                            onFontSelected = {
                                viewModel.setFontFamily(it.name)
                            }
                        )
                    }
                }
            }
        }
    }
}