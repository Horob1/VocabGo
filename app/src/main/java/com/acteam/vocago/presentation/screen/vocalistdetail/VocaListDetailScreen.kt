package com.acteam.vocago.presentation.screen.vocalistdetail

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.acteam.vocago.R
import com.acteam.vocago.data.local.entity.VocaEntity
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.vocalistdetail.component.AddWordDialog
import com.acteam.vocago.utils.safeClickable
import java.util.Locale

@Composable
fun VocaListDetailScreen(
    viewModel: VocaListDetailViewModel,
    navController: NavController,
    vocaListId: Int,
) {
    val vocaListDetailData by viewModel.vocaListDetailData.collectAsState()
    val listId by viewModel.listId.collectAsState()

    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    //    var showEditDialog by remember { mutableStateOf(false) }
    var showAddWordDialog by remember { mutableStateOf(false) }

    // Khởi tạo TextToSpeech
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    // Đảm bảo dọn bộ nhớ khi không dùng nữa
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    LaunchedEffect(Unit) {
        if (listId == null) {
            viewModel.setId(vocaListId)
        }
    }

    Scaffold(
        topBar = {
            if (vocaListDetailData != null) {
                VocabListDetailTopBar(
                    navController = navController,
                    name = vocaListDetailData!!.vocaList.title,
//                    onEditClick = {
//                        showEditDialog = true
//                    },
                    onAddWordClick = {
                        showAddWordDialog = true
                    }
                )
            }
        }
    ) { innerPadding ->
        if (vocaListDetailData == null) {
            LoadingSurface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }

        if (vocaListDetailData != null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (!vocaListDetailData!!.vocas.isEmpty()) item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(8.dp, 4.dp)

                                    .weight(1f, true)
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .clip(
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .safeClickable(
                                        key = "flashcard_navigator"
                                    ) {
                                        navController.navigate(
                                            NavScreen.FlashCardNavScreen(
                                                vocaListId
                                            )
                                        )
                                    }
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Outlined.ContentCopy,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(R.string.flashcard),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(8.dp, 4.dp)

                                    .weight(1f, true)
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .clip(
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .safeClickable(
                                        key = "learn_navigator"
                                    ) {
                                        if (
                                            vocaListDetailData!!.vocas.size < 5
                                        ) {
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.not_enough_words),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@safeClickable
                                        }

                                        if (
                                            vocaListDetailData!!.vocas.size > 50
                                        ) {
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.too_many_words),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@safeClickable
                                        }

                                        navController.navigate(
                                            NavScreen.LearnVocaNavScreen(
                                                vocaListId
                                            )
                                        )

                                    }
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Outlined.AllInclusive,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(R.string.learn),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    items(
                        vocaListDetailData!!.vocas.size,
                        key = { index ->
                            vocaListDetailData!!.vocas[index].id
                        }
                    ) { index ->
                        var showConfirmDeleteVoca by remember { mutableStateOf(false) }

                        SwipeToDismissBox(
                            state = rememberSwipeToDismissBoxState(
                                confirmValueChange = {
                                    if (it == SwipeToDismissBoxValue.EndToStart) {
                                        // Xóa từ vựng
                                        showConfirmDeleteVoca = true
                                        true
                                    }
                                    false
                                }
                            ),
                            backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize()
                                        .background(
                                            Color.Red.copy(0.7f),
                                            shape = MaterialTheme.shapes.medium
                                        ),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Row {
                                        Icon(
                                            Icons.Filled.DeleteSweep,
                                            contentDescription = "Delete",
                                            tint = Color.White
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .width(16.dp)
                                                .height(16.dp)
                                        )
                                    }
                                }
                            }
                        ) {
                            VocabularyCard(
                                word =
                                    vocaListDetailData!!.vocas[index],
                                speak = {
                                    tts?.speak(
                                        vocaListDetailData!!.vocas[index].word,
                                        TextToSpeech.QUEUE_FLUSH,
                                        null,
                                        null
                                    )
                                }
                            )
                        }

                        if (showConfirmDeleteVoca) {
                            // Hiển thị dialog xác nhận xóa
                            Dialog(
                                onDismissRequest = { showConfirmDeleteVoca = false }
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp)
                                    ) {
                                        Text(
                                            text = "${stringResource(R.string.title_sure_to_delete)} ${vocaListDetailData!!.vocas[index].word}",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Button(
                                                onClick = { showConfirmDeleteVoca = false },
                                                modifier = Modifier.weight(1f),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                                ),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Text(
                                                    text = stringResource(R.string.btn_cancel),
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                            }

                                            Button(
                                                onClick = {
                                                    viewModel.deleteVoca(vocaListDetailData!!.vocas[index])
                                                    showConfirmDeleteVoca = false
                                                },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Text(
                                                    text = stringResource(R.string.btn_delete),
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

//        if (showEditDialog) {
//
//        }

        if (showAddWordDialog) {
            AddWordDialog(
                onDismiss = { showAddWordDialog = false },
                onSaved = {
                    viewModel.saveWordToVocalist(it)
                },
                imageList = viewModel.images.collectAsState().value,
                loadImages = {
                    viewModel.loadImages(it)
                },
                listId = vocaListId
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabListDetailTopBar(
    navController: NavController,
    onAddWordClick: () -> Unit,
//    onEditClick: () -> Unit,
    name: String,
) {
    TopAppBar(
        title = {
            Text(
                name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null
                )
            }
        },
        actions = {
            var showMenu by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.btn_add_word)) },
                        onClick = {
                            onAddWordClick()
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Add, contentDescription = "Add word")
                        }
                    )
//                    DropdownMenuItem(
//                        text = { Text(stringResource(R.string.btn_edit)) },
//                        onClick = {
//                            onEditClick()
//                            showMenu = false
//                        },
//                        leadingIcon = {
//                            Icon(Icons.Default.Edit, contentDescription = "edit")
//                        }
//                    )
                }
            }
        }
    )
}


@Composable
fun VocabularyCard(
    word: VocaEntity,
    speak: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header với từ vựng và nút phát âm
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = word.word,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Nút phát âm với hiệu ứng
                    IconButton(
                        onClick = { speak() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Pronunciation",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Phiên âm
                val phonetic = word.pronunciation
                    ?.removePrefix("{")
                    ?.removeSuffix("}")
                    ?.trim()
                    ?.split(",")

                if (!phonetic.isNullOrEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(phonetic.size) { index ->
                            val pronunciation = phonetic[index]
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = pronunciation.trim(),
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Loại từ
                if (!word.type.isNullOrBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = word.type,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Ảnh minh họa (nếu có)
            if (!word.urlImage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(word.urlImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Word illustration",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.capybara_avatar),
                    error = painterResource(R.drawable.capybara_avatar),
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            // Nghĩa của từ
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = word.meaning,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )
        }
    }
}