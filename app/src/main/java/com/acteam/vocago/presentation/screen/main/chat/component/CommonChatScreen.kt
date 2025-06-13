package com.acteam.vocago.presentation.screen.main.chat.component

import MessageItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.main.chat.ChatViewModel

@Composable
fun CommonChatScreen(
    id: Int,
    title: String,
    avatarRes: Int,
    viewModel: ChatViewModel,
    rootNavController: NavController,
) {

    val userState = viewModel.userState.collectAsState()
    val messages = remember { viewModel.messageList }
    val isTyping by viewModel.isTyping.collectAsState()
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeBottomDp = with(LocalDensity.current) { imeBottomPx.toDp() }
    val focusManager = LocalFocusManager.current
    val promptDescription = viewModel.promptDescriptionMap[id] ?: "No description"
    val shouldShowIntro = remember { mutableStateOf(true) }
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            shouldShowIntro.value = false
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                }
        ) {
            ThinTopBar(
                title = title,
                onBackClick = { rootNavController.popBackStack() },
                avatarRes = avatarRes
            )
            if (shouldShowIntro.value) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = "Chatbot Avatar",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = promptDescription,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                state = listState,
                verticalArrangement = Arrangement.Top,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { message ->
                    MessageItem(
                        message,
                        userAvatarUrl = userState.value?.avatar,
                        chatbotAvatar = avatarRes,
                        userDefautlAvatar = R.drawable.capybara_avatar
                    )
                }
                if (isTyping) {
                    item {
                        TypingIndicator(
                            avatar = avatarRes,
                            chatbotName = title
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = imeBottomDp)
            ) {
                MessengerInput(
                    input = input,
                    onInputChange = { input = it },
                    onSendClick = {
                        if (input.isNotBlank()) {
                            viewModel.sendMessageById(id, input)
                            viewModel.playSound()
                            input = ""
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
