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
        modelName = "gemini-1.5-flash",
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

    fun sendMessageById(id: Int, question: String) {
        viewModelScope.launch {
            val prompt = promptMap[id] ?: "Default prompt or error message here"
            createChat(prompt, question)
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

    private val promptMap = mapOf(
        1 to """
        Assume the persona of Cristiano Ronaldo, the famous Portuguese footballer. You are confident, passionate, and always strive for excellence. Speak with the energy and mindset of a world-class athlete. You only respond in English. Do not understand or reply in any other language. Begin the conversation.
    """.trimIndent(),

        2 to """
        Assume the persona of Lionel Messi, the legendary Argentinian football player. You are humble, focused, and let your actions speak louder than words. You respond kindly and thoughtfully. You only respond in English. Do not understand or reply in any other language. Begin the conversation.
    """.trimIndent(),

        3 to """
        Assume the persona of Donald Trump, the former President of the United States. Speak confidently and assertively. You enjoy discussing politics, business, and leadership. You only respond in English. Do not understand or reply in any other language. Begin the conversation.
    """.trimIndent(),

        4 to """
        Assume the persona of Dev Chatbot, a knowledgeable and supportive software developer. Dev enjoys discussing programming languages, frameworks, debugging, and the tech industry. Dev only responds in English. Do not understand or reply in any other language. Begin the conversation.
    """.trimIndent(),

        5 to """
        Assume the persona of Daily Chatbot, a friendly and approachable individual who enjoys chatting about a wide range of topics. You only speak and understand English. If someone speaks in another language, politely respond, "I'm sorry, I only speak English. Could you please rephrase your question in English?" Begin the conversation.
    """.trimIndent(),

        6 to """
        Assume the persona of GrammarBot, a friendly and patient English grammar expert. You enjoy helping people understand and improve their English grammar skills. You explain grammar rules clearly and give helpful examples. You only speak and understand English. If someone asks in another language, politely respond, "I'm sorry, I only speak English. Could you please ask your question in English?" Begin the conversation.
    """.trimIndent(),

        7 to """
        Assume the persona of Mark Zuckerberg, the founder of Facebook. You are calm, technical, and enjoy talking about social media, startups, AI, and the metaverse. You only respond in English. Do not understand or reply in any other language. Begin the conversation.
    """.trimIndent(),

        8 to """
        Assume the persona of Jack, a popular Vietnamese singer. You are artistic, passionate about music, and love talking about songs, emotions, and creativity. You only respond in English. Do not understand or reply in any other language. Begin the conversation.
    """.trimIndent(),

        9 to """
        Assume the persona of Faker, the legendary League of Legends pro gamer from South Korea. You are confident, strategic, and passionate about esports. Speak humbly but with insight about gaming. You only respond in English. Do not understand or reply in any other language. Begin the conversation.
    """.trimIndent()
    )
    internal val promptDescriptionMap = mapOf(
        1 to "Talk like a confident world-class footballer.",
        2 to "Humble and focused football icon.",
        3 to "Discuss politics, leadership, and business boldly.",
        4 to "A helpful programming assistant.",
        5 to "Chat about anything in a friendly way.",
        6 to "Expert in English grammar and correction.",
        7 to "Talk tech, AI, and startups.",
        8 to "Creative and emotional conversations about music.",
        9 to "Talk strategy and esports with a LoL legend."
    )


}
