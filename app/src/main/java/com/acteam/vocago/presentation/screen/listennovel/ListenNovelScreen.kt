package com.acteam.vocago.presentation.screen.listennovel

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.NovelDetail
import com.acteam.vocago.domain.model.NovelDetailChapter
import com.acteam.vocago.domain.model.TTSConfig
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.utils.DateDisplayHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListenNovelScreen(
    modifier: Modifier = Modifier,
    novelId: String,
    beginChapterId: String,
    navController: NavController,
    viewModel: ListenNovelViewModel,
) {
    val context = LocalContext.current
    val novelState by viewModel.novelDetail.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isBound by viewModel.isBound.collectAsStateWithLifecycle()
    val state by viewModel.playerState.collectAsStateWithLifecycle()
    var showModelBottomSetting by remember {
        mutableStateOf(false)
    }
    var showExitDialog by remember {
        mutableStateOf(false)
    }

    val launcherNoti = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                if (!isBound) {
                    viewModel.bindService(
                        context = context
                    )
                }
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.notification_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
            }
        }
    )

    LaunchedEffect(Unit) { }

    DisposableEffect(key1 = Unit) {
        if (!isBound) {
            val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
            if (hasPermission) {
                viewModel.bindService(
                    context = context
                )
            } else {
                launcherNoti.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        onDispose {
            viewModel.unbindService(context)
        }
    }

    LaunchedEffect(key1 = isBound, key2 = state) {
        if (isBound && state is ChapterPlayerState.Loading) {
            delay(3000)
            viewModel.startService(
                context = context,
                beginChapterId = beginChapterId
            )
            viewModel.getVoices()
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (novelState !is UIState.UISuccess) {
            viewModel.loadNovelDetail(novelId)
        }
    }


    if (
        novelState is UIState.UISuccess && state is ChapterPlayerState.Playing
    ) ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = stringResource(R.string.chapterList),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        (novelState as UIState.UISuccess<NovelDetail>).data.chapters.size
                    ) {
                        val chapter =
                            (novelState as UIState.UISuccess<NovelDetail>).data.chapters[it]
                        ChapterCard(
                            modifier = Modifier,
                            item = chapter,
                            onClick = {
                                viewModel.startService(
                                    context = context,
                                    beginChapterId = chapter._id
                                )
                            },
                            isSelect = chapter._id == (state as ChapterPlayerState.Playing).state._id
                        )
                    }
                }
            }
        }
    ) {
        BackHandler {
            showExitDialog = true
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = (novelState as UIState.UISuccess<NovelDetail>).data.fictionTitle)
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                showExitDialog = true
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }

        ) {
            val playerState = state as ChapterPlayerState.Playing
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(4.dp)
                    .padding(bottom = 36.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomBtn(
                        modifier = Modifier,
                        onClick = {
                            scope.launch { drawerState.open() }
                        },
                        icon = Icons.AutoMirrored.Filled.ListAlt,
                        text = R.string.text_list
                    )
                    CustomBtn(
                        modifier = Modifier,
                        onClick = {
                            showModelBottomSetting = true
                            viewModel.pauseService(context)
                        },
                        icon = Icons.Default.Settings,
                        text = R.string.text_setting
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                ChapterInfo(
                    number = playerState.state.chapterNumber,
                    text = playerState.state.chapterTitle,
                )

                Spacer(modifier = Modifier.weight(1f))

                NovelMediaController(
                    Modifier.padding(horizontal = 16.dp),
                    currentParagraphIndex = playerState.state.currentParagraphIndex,
                    totalParagraph = playerState.state.totalParagraphs,
                    currentParagraph = playerState.state.paragraphs[playerState.state.currentParagraphIndex],
                    onChoseParagraph = { index ->
                        viewModel.chooseParagraph(
                            context = context,
                            paragraphIndex = index
                        )
                    },
                    isPlay = playerState.isPlaying,
                    onPlay = {
                        viewModel.resumeService(context)
                    },
                    onPause = {
                        viewModel.pauseService(context)
                    },
                    onNext = {
                        viewModel.nextChapter(context)
                    },
                    onPrevious = {
                        viewModel.previousChapter(context)
                    }

                )

                Spacer(modifier = Modifier.weight(1f))
                val autoOff by viewModel.autoOff.collectAsStateWithLifecycle()
                AlarmPicker(
                    second = autoOff,
                    onSet = {
                        viewModel.setAutoOff(it, context)
                    },
                    onCancel = {
                        viewModel.setCancel(
                            true
                        )
                    }
                )
            }

            if (showExitDialog) {
                AlertDialog(
                    onDismissRequest = { showExitDialog = false },
                    title = {
                        Text(
                            text = stringResource(R.string.title_confirm_to_exit),
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.desc_confirm_to_exit),
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showExitDialog = false
                                viewModel.unbindService(context)
                                viewModel.stopService(context)
                                navController.popBackStack()
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
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                )
            }

            val config by viewModel.ttsConfig.collectAsStateWithLifecycle()
            if (showModelBottomSetting) {
                ModelBottomSetting(
                    onDismiss = {
                        showModelBottomSetting = false
                        viewModel.resumeService(context)
                    },
                    currentVoice = config.voice,
                    voiceList = viewModel.voiceList.collectAsStateWithLifecycle().value,
                    onVoiceChange = { voice ->
                        viewModel.setTTSConfig(
                            config = TTSConfig(
                                voice = voice,
                                speed = config.speed,
                                pitch = config.pitch
                            )
                        )
                    },
                    speed = config.speed,
                    pitch = config.pitch,
                    onSpeedChange = { speed ->
                        viewModel.setTTSConfig(
                            config = TTSConfig(
                                voice = config.voice,
                                speed = speed,
                                pitch = config.pitch
                            )
                        )
                    },
                    onPitchChange = { pitch ->
                        viewModel.setTTSConfig(
                            config = TTSConfig(
                                voice = config.voice,
                                speed = config.speed,
                                pitch = pitch
                            )
                        )
                    }
                )
            }
        }
    } else {
        LoadingSurface(
            modifier = Modifier.fillMaxSize()
        )
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ModelBottomSetting(
    onDismiss: () -> Unit = {},
    currentVoice: String = "",
    voiceList: List<String> = emptyList(),
    onVoiceChange: (String) -> Unit = {},
    onSpeedChange: (Float) -> Unit = {},
    onPitchChange: (Float) -> Unit = {},
    speed: Float = 1f,
    pitch: Float = 1f,
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Header với gradient
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        R.string.title_setting_voice,
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = stringResource(R.string.desc_setting_voice),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            SettingCard(
                title = stringResource(R.string.text_voice),
                icon = Icons.Default.RecordVoiceOver,
                iconColor = MaterialTheme.colorScheme.primary
            ) {
                var isDropdownExpanded by remember { mutableStateOf(false) }

                Box {
                    OutlinedButton(
                        onClick = { isDropdownExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = currentVoice.ifEmpty {
                                stringResource(R.string.text_select_voice)
                            },
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(if (isDropdownExpanded) 180f else 0f)
                        )
                    }

                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(16.dp)
                            )
                    ) {
                        voiceList.forEach { voice ->
                            DropdownMenuItem(
                                onClick = {
                                    onVoiceChange(voice)
                                    isDropdownExpanded = false
                                },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (voice == currentVoice) {
                                            Surface(
                                                modifier = Modifier.size(8.dp),
                                                shape = CircleShape,
                                                color = MaterialTheme.colorScheme.primary
                                            ) {}
                                        } else Spacer(modifier = Modifier.size(8.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = voice,
                                            fontWeight = if (voice == currentVoice) FontWeight.Medium else FontWeight.Normal
                                        )
                                    }
                                })
                        }
                    }
                }
            }

            // Speed Control Card
            SettingCard(
                title = stringResource(R.string.text_speed),
                icon = Icons.Default.Speed,
                iconColor = MaterialTheme.colorScheme.tertiary,
                value = "${String.format("%.1f", speed)}x"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Slider(
                        value = speed,
                        onValueChange = onSpeedChange,
                        valueRange = 0.5f..2.0f,
                        steps = 14,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.tertiary,
                            activeTrackColor = MaterialTheme.colorScheme.tertiary,
                            inactiveTrackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.24f),
                            activeTickColor = Color.Transparent,
                            inactiveTickColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        thumb = {
                            Surface(
                                modifier = Modifier.size(24.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.tertiary,
                                shadowElevation = 4.dp
                            ) {}
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.text_slow),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = stringResource(R.string.text_fast),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Pitch Control Card
            SettingCard(
                title = stringResource(R.string.text_pitch),
                icon = Icons.Default.Tune,
                iconColor = MaterialTheme.colorScheme.secondary,
                value = String.format("%.1f", pitch)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Slider(
                        value = pitch,
                        onValueChange = onPitchChange,
                        valueRange = 0.5f..2.0f,
                        steps = 14,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.secondary,
                            activeTrackColor = MaterialTheme.colorScheme.secondary,
                            inactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.24f),
                            activeTickColor = Color.Transparent,
                            inactiveTickColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        thumb = {
                            Surface(
                                modifier = Modifier.size(24.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.secondary,
                                shadowElevation = 4.dp
                            ) {}
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.text_low),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = stringResource(R.string.text_high),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SettingCard(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    value: String? = null,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = iconColor.copy(alpha = 0.15f)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                value?.let {
                    Surface(
                        color = iconColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.border(
                            1.dp,
                            iconColor.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = iconColor,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            content()
        }
    }
}

@Composable
fun ChapterCard(
    modifier: Modifier = Modifier,
    item: NovelDetailChapter = NovelDetailChapter(
        "",
        0,
        "",
        ""
    ),
    onClick: () -> Unit = {},
    isSelect: Boolean = false,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable {
                if (!isSelect)
                    onClick()
            }
            .border(
                width = 2.dp,
                color = if (isSelect) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Chapter number badge
            Surface(
                modifier = Modifier
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = item.chapterNumber.toString(),
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

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
                    } ${item.chapterNumber}: ${item.chapterTitle}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.padding(2.dp))

                if (item.createdAt.isBlank())
                    Text(
                        text = DateDisplayHelper.formatDateString(
                            dateString = item.createdAt
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        )
                    )
            }
        }
    }
}

@Composable
fun AlarmPicker(
    modifier: Modifier = Modifier,
    second: Int,
    onCancel: () -> Unit = {},
    onSet: (Int) -> Unit = {},
) {
    val showDialog = remember { mutableStateOf(false) }

    Button(
        modifier = modifier,
        onClick = {
            if (second == 0) {
                showDialog.value = true
            } else {
                onCancel()
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            if (second != 0) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.titleMedium.toSpanStyle()
                                .copy(
                                    fontWeight = FontWeight.Bold,
                                )
                        ) {
                            append(
                                "${(ceil(second / 60f).toInt()).toString().padStart(2, '0')}:${
                                    (second % 60).toString().padStart(2, '0')
                                }"
                            )
                        }
                        withStyle(
                            style = MaterialTheme.typography.titleSmall.toSpanStyle()
                                .copy(
                                    fontWeight = FontWeight.Normal,
                                )
                                .copy(
                                    White
                                )
                        ) {
                            append("  ${stringResource(id = R.string.text_cancel_alarm)}")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = White
                )
            } else {
                Icon(
                    Icons.Default.Alarm,
                    contentDescription = "Alarm",
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = stringResource(id = R.string.txt_auto_off),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (showDialog.value) {
            val minute = remember { mutableStateOf(0f) } // Float cho Slider

            Dialog(
                onDismissRequest = {
                    showDialog.value = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title
                    Text(
                        text = stringResource(id = R.string.txt_auto_off),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Slider + hiển thị giá trị
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${minute.value.toInt()} ${stringResource(id = R.string.text_minute)}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Slider(
                            value = minute.value,
                            onValueChange = { minute.value = it },
                            valueRange = 0f..120f,
                            steps = 11 // chia thành 12 nấc (mỗi 10 phút)
                        )
                    }

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showDialog.value = false }
                        ) {
                            Text(
                                text = stringResource(id = R.string.btn_cancel),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onSet(minute.value.toInt())
                                showDialog.value = false
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.btn_confirm),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun NovelMediaController(
    modifier: Modifier = Modifier,
    isPlay: Boolean = false,
    currentParagraphIndex: Int,
    totalParagraph: Int,
    currentParagraph: String,
    onChoseParagraph: (Int) -> Unit = {},
    onPlay: () -> Unit = {},
    onPause: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MyMediaController(
            isPlay = isPlay,
            onPlay = onPlay,
            onPause = onPause,
            onNext = onNext,
            onPrevious = onPrevious
        )

        CurrentParagraph(
            modifier = Modifier.padding(horizontal = 8.dp),
            p = currentParagraph
        )

        CustomProgress(
            modifier.padding(horizontal = 8.dp),
            value = currentParagraphIndex.toFloat(),
            rangeFrom = 0f,
            rangeTo = totalParagraph.toFloat() - 1,
            onValueChange = {
                onChoseParagraph(it.toInt())
            }
        )
    }
}

@Composable
fun CurrentParagraph(
    modifier: Modifier = Modifier,
    p: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(
                80.dp
            )
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            )
            .clip(
                RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = 8.dp, vertical = 4.dp
            )
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Text(
            text = p,
            style = MaterialTheme.typography.bodyLarge
        )
    }

}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomProgress(
    modifier: Modifier = Modifier,
    value: Float = 0.5f,
    rangeFrom: Float = 0f,
    rangeTo: Float = 1f,
    onValueChange: (Float) -> Unit = {},
) {
    Slider(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        valueRange = rangeFrom..rangeTo,
        modifier = modifier
            .height(16.dp),
        colors = SliderDefaults.colors(
            activeTrackColor = Color.Transparent,
            inactiveTrackColor = Color.Transparent
        ),
        thumb = {
            Box(
                Modifier
                    .size(16.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                    .border(
                        width = 2.dp,
                        MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        },
        track = { sliderPositions ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(0.8f),
                    )
            )
        }
    )
}

@Composable
fun MyMediaController(
    modifier: Modifier = Modifier,
    isPlay: Boolean = false,
    onPlay: () -> Unit = {},
    onPause: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                onPrevious()
            },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                Icons.Default.SkipPrevious,
                contentDescription = "Back",
                modifier = Modifier.size(60.dp)
            )
        }

        IconButton(
            onClick = {
                if (isPlay)
                    onPause()
                else
                    onPlay()
            },
            modifier = Modifier.size(90.dp)
        ) {
            Icon(
                if (isPlay) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                contentDescription = "Pause/Play",
                modifier = Modifier.size(70.dp)
            )
        }

        IconButton(
            onClick = {
                onNext()
            },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                Icons.Default.SkipNext,
                contentDescription = "Next",
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@Composable
fun ChapterInfo(number: Int, text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text(
            text = "${
                stringResource(R.string.chapters).replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                }
            } $number",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        // Banner QC

        Spacer(modifier = Modifier.size(16.dp))

        Box(
            modifier = Modifier
                .size(
                    height = 240.dp,
                    width = 280.dp
                )
                .background(MaterialTheme.colorScheme.primary)
        ) {


        }
    }

}


@Composable
fun CustomBtn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
    @StringRes text: Int = R.string.text_or,
) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .clickable(
                onClick = onClick
            )
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(
                RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)

        Text(
            text = stringResource(id = text),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )
    }
}