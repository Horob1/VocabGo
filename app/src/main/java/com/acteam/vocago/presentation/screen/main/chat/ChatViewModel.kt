package com.acteam.vocago.presentation.screen.main.chat

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.BuildConfig
import com.acteam.vocago.R
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.presentation.screen.main.chat.data.MessageModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(
    context: Context,
    getLocalUserProfileUseCase: GetLocalUserProfileUseCase,
) : ViewModel() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    private val _messageList = mutableStateListOf<MessageModel>()
    val messageList: List<MessageModel> get() = _messageList

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping

    val userState = getLocalUserProfileUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    private val _selectedPromptIndex = MutableStateFlow(0)
    val selectedPromptIndex: StateFlow<Int> = _selectedPromptIndex

    fun selectPromptIndex(index: Int) {
        _selectedPromptIndex.value = index
    }

    private suspend fun createChat(prompt: String, question: String) {
        val chat = generativeModel.startChat(
            history = listOf(
                content("model") { text(prompt) }
            ) + _messageList.map {
                content(if (it.isModel) "model" else "user") {
                    text(it.message)
                }
            }
        )

        _messageList.add(MessageModel(message = question, isModel = false))
        _isTyping.value = true

        try {
            val response = chat.sendMessage(question)
            _messageList.add(MessageModel(message = response.text.toString(), isModel = true))
        } catch (e: Exception) {
            _messageList.add(MessageModel("Error: ${e.message}", isModel = true))
        } finally {
            _isTyping.value = false
        }
    }

    fun sendMessageWithPrompt(prompt: String?, question: String) {
        viewModelScope.launch {
            val safePrompt = prompt ?: "Default prompt"
            createChat(safePrompt, question)
        }
    }

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val soundId = soundPool.load(context, R.raw.message, 1)

    fun playSound() {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }
}