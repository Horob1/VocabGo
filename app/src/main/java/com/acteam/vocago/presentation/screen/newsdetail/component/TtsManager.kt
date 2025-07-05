package com.acteam.vocago.presentation.screen.newsdetail.component

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class TtsManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var paragraphList: List<String> = emptyList()
    private var currentIndexInternal: Int = 0
    private var isReady = false
    private var isManuallyStopped = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> get() = _isSpeaking

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> get() = _currentIndex

    init {
        tts = TextToSpeech(context) {
            isReady = it == TextToSpeech.SUCCESS
            if (isReady) {
                tts?.language = Locale.US
            }
        }

        tts?.setOnUtteranceProgressListener(object :
            android.speech.tts.UtteranceProgressListener() {

            override fun onStart(utteranceId: String?) {
                // no-op
            }

            override fun onDone(utteranceId: String?) {
                if (isManuallyStopped) return

                if (currentIndexInternal < paragraphList.size) {
                    speakInternal(paragraphList[currentIndexInternal])
                    _currentIndex.value = currentIndexInternal
                    currentIndexInternal++
                } else {
                    _isSpeaking.value = false
                }
            }

            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
            }
        })
    }

    fun setText(text: String) {
        paragraphList = text.split("\n")
        currentIndexInternal = 0
        _currentIndex.value = 0
    }

    fun speakAll() {
        if (!isReady || paragraphList.isEmpty()) return
        isManuallyStopped = false
        currentIndexInternal = 0
        _currentIndex.value = 0
        _isSpeaking.value = true
        speakInternal(paragraphList[currentIndexInternal])
        currentIndexInternal++
    }

    fun speakNext() {
        if (!isReady || currentIndexInternal >= paragraphList.size) return
        speakInternal(paragraphList[currentIndexInternal])
        _currentIndex.value = currentIndexInternal
        currentIndexInternal++
    }

    fun speakPrevious() {
        if (!isReady || currentIndexInternal <= 1) return
        currentIndexInternal -= 2
        speakNext()
    }

    fun speakFrom(index: Int) {
        if (!isReady || index !in paragraphList.indices) return

        isManuallyStopped = false
        currentIndexInternal = index
        _currentIndex.value = index
        _isSpeaking.value = true
        speakInternal(paragraphList[currentIndexInternal])
        currentIndexInternal++
    }

    fun stop() {
        isManuallyStopped = true
        tts?.stop()
        _isSpeaking.value = false
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
    }

    private fun speakInternal(text: String) {
        val utteranceId = "tts-${System.currentTimeMillis()}"
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle(), utteranceId)
    }
}
