package com.acteam.vocago.presentation.screen.listennovel

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import com.acteam.vocago.R
import com.acteam.vocago.domain.usecase.GetChapterUseCase
import com.acteam.vocago.domain.usecase.GetTTSConfigUseCase
import com.acteam.vocago.utils.NotificationChannelHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.HttpURLConnection
import java.net.URL

class NovelPlayerService : Service(), KoinComponent {
    companion object {
        const val ACTION_PAUSE = "pause"
        const val ACTION_RESUME = "resume"
        const val ACTION_NEXT_CHAPTER = "next_chapter"
        const val ACTION_PREVIOUS_CHAPTER = "previous_chapter"
        const val ACTION_SPEAK_CHAPTER = "speak_chapter"
        const val ACTION_SPEAK_FROM_PARAGRAPH = "speak_from_paragraph"

        const val NOTIFICATION_ID = 1111

        const val chapterIdKey = "chapterId"

        const val indexKey = "index"

        const val urlKey = "url"
    }

    private val getChapterUseCase: GetChapterUseCase by inject()
    private val getConfigUseCase: GetTTSConfigUseCase by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var tts: TextToSpeech? = null

    private var bitmap: Bitmap? = null
    private val _playerState = MutableStateFlow<ChapterPlayerState>(ChapterPlayerState.Loading)
    private val binder = NovelPlayerBinder()

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = java.util.Locale.US
                serviceScope.launch {
                    getConfigUseCase().collect { config ->
                        tts?.apply {
                            setSpeechRate(config.speed)
                            setPitch(config.pitch)
                            voice = tts?.voices?.find { voice -> voice.name == config.voice }
                                ?: tts?.defaultVoice
                        }
                    }
                }
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        sendNotification()
                    }

                    override fun onDone(utteranceId: String?) {
                        val current = _playerState.value
                        if (current is ChapterPlayerState.Playing) {
                            val index = utteranceId?.toIntOrNull() ?: return
                            if (index < current.state.totalParagraphs - 1) {
                                speakFromParagraph(index + 1)
                            } else {
                                nextChapter()
                            }
                        }

                        sendNotification()
                    }

                    override fun onError(utteranceId: String?) {

                    }
                })
            }
        }
    }


    fun getVoices(): List<String> {
        if (tts == null) return emptyList()
        return tts?.voices
            ?.filter { it != null && it.locale.language == "en" }  // chỉ lấy tiếng Anh
            ?.map { it.name }
            ?: emptyList()
    }


    inner class NovelPlayerBinder : Binder() {
        fun getService(): NovelPlayerService = this@NovelPlayerService
        fun getStates() = _playerState
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handleAction(intent)
        sendNotification()
        Log.d("NovelPlayerService", "onStartCommand: ${intent.action}")
        return START_STICKY
    }

    private fun sendNotification() {
        val current = _playerState.value
        if (current !is ChapterPlayerState.Playing) return

        val playPauseAction = if (current.isPlaying) {
            NotificationCompat.Action(
                android.R.drawable.ic_media_pause,
                "Pause",
                getPendingIntent(
                    ACTION_PAUSE,
                )
            )
        } else {
            NotificationCompat.Action(
                android.R.drawable.ic_media_play,
                "Resume",
                getPendingIntent(
                    ACTION_RESUME,
                )
            )
        }

        val mediaSession = MediaSessionCompat(this, "PlayerSession")

        val metadata = MediaMetadataCompat.Builder()
            .putBitmap(
                MediaMetadataCompat.METADATA_KEY_ALBUM_ART,
                bitmap
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_TITLE,
                "Chapter ${current.state.chapterNumber}: ${current.state.chapterTitle}"
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_ARTIST,
                "Paragraph ${current.state.currentParagraphIndex + 1}/${current.state.totalParagraphs}"
            )
            .build()

        mediaSession.setMetadata(metadata)
        mediaSession.isActive = true

        val builder =
            NotificationCompat.Builder(this, NotificationChannelHelper.NOVEL_SPEAKING_CHANNEL_ID)
                .setSmallIcon(R.drawable.sun)
                .setContentTitle("Chapter ${current.state.chapterNumber}: ${current.state.chapterTitle}")
                .setContentText("Paragraph ${current.state.currentParagraphIndex + 1}/${current.state.totalParagraphs}")
                .setOngoing(current.isPlaying)
                .setOnlyAlertOnce(true)
                .addAction(
                    NotificationCompat.Action(
                        android.R.drawable.ic_media_previous,
                        "Previous",
                        getPendingIntent(
                            ACTION_PREVIOUS_CHAPTER,
                        )
                    )
                )
                .addAction(playPauseAction)
                .addAction(
                    NotificationCompat.Action(
                        android.R.drawable.ic_media_next,
                        "Next",
                        getPendingIntent(
                            ACTION_NEXT_CHAPTER,
                        )
                    )
                )
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.sessionToken)
                        .setShowActionsInCompactView(0, 1, 2)
                )

        startForeground(NOTIFICATION_ID, builder.build())
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, NovelPlayerReceiver::class.java).apply {
            this.action = action
        }
        return PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun handleAction(intent: Intent) {
        when (intent.action) {
            ACTION_PAUSE -> pause()
            ACTION_RESUME -> resume()
            ACTION_NEXT_CHAPTER -> nextChapter()
            ACTION_PREVIOUS_CHAPTER -> previousChapter()
            ACTION_SPEAK_CHAPTER -> {
                val chapterId = intent.getStringExtra(chapterIdKey)
                val url = intent.getStringExtra(urlKey)
                if (url != null) getBitmapFromURL(url)
                if (chapterId != null) speakChapter(chapterId)
            }

            ACTION_SPEAK_FROM_PARAGRAPH -> {
                val index = intent.getIntExtra(indexKey, 0)
                speakFromParagraph(index)
            }

            else -> {}
        }
    }

    private fun pause() {
        val current = _playerState.value
        if (current is ChapterPlayerState.Playing && current.isPlaying
        ) {
            _playerState.value = current.copy(isPlaying = false)
            tts?.stop()
        }
    }

    private fun resume() {
        val current = _playerState.value
        if (current is ChapterPlayerState.Playing && !current.isPlaying) {
            _playerState.value = current.copy(isPlaying = true)
            speakFromParagraph(current.state.currentParagraphIndex)
        }
    }

    private fun nextChapter() {
        val current = _playerState.value
        if (current is ChapterPlayerState.Playing) {
            if (current.state.nextChapterId != null)
                speakChapter(
                    chapterId = current.state.nextChapterId
                )
        }
    }

    private fun previousChapter() {
        val current = _playerState.value
        if (current is ChapterPlayerState.Playing) {
            if (current.state.previousChapterId != null)
                speakChapter(
                    chapterId = current.state.previousChapterId
                )
        }
    }

    private fun speakChapter(
        chapterId: String,
    ) {
        serviceScope.launch {
            val chapter = getChapterUseCase(chapterId)

            if (chapter != null) {
                _playerState.value = ChapterPlayerState.Playing(
                    isPlaying = true,
                    state = NovelPlayerState(
                        _id = chapter.chapter._id,
                        chapterNumber = chapter.chapter.chapterNumber,
                        chapterTitle = chapter.chapter.chapterTitle,
                        currentParagraphIndex = 0,
                        nextChapterId = chapter.nextChapter?._id,
                        previousChapterId = chapter.previousChapter?._id,
                        paragraphs = chapter.chapter.content.split("\n"),
                        totalParagraphs = chapter.chapter.content.split("\n").size
                    )
                )
                speakFromParagraph(index = 0)
            }
        }
    }

    private fun speakFromParagraph(index: Int) {
        if (tts != null && _playerState.value is ChapterPlayerState.Playing) {
            val state = _playerState.value as ChapterPlayerState.Playing
            _playerState.value = state.copy(
                isPlaying = true,
                state = state.state.copy(
                    currentParagraphIndex = index
                )
            )

            tts?.speak(
                state.state.paragraphs[index],
                TextToSpeech.QUEUE_FLUSH,
                null,
                index.toString()
            )
            sendNotification()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        tts?.stop()
        tts?.shutdown()
    }

    private fun getBitmapFromURL(src: String) {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            bitmap = BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}