package com.acteam.vocago.presentation.screen.listennovel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.model.NovelDetail
import com.acteam.vocago.domain.model.TTSConfig
import com.acteam.vocago.domain.usecase.GetNovelDetailUseCase
import com.acteam.vocago.domain.usecase.GetTTSConfigUseCase
import com.acteam.vocago.domain.usecase.SaveTTSConfigUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListenNovelViewModel(
    private val getNovelDetailUseCase: GetNovelDetailUseCase,
    getTTSVoicesUseCase: GetTTSConfigUseCase,
    private val setTTSConfigUseCase: SaveTTSConfigUseCase,
) : ViewModel() {
    val ttsConfig = getTTSVoicesUseCase().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TTSConfig(
            voice = "",
            speed = 1f,
            pitch = 1f,
        )
    )
    private val _novelDetail = MutableStateFlow<UIState<NovelDetail>>(UIState.UILoading)
    val novelDetail: StateFlow<UIState<NovelDetail>> = _novelDetail

    private val _autoOff = MutableStateFlow<Int>(0)
    val autoOff = _autoOff

    private val _cancel = MutableStateFlow<Boolean>(false)

    fun setCancel(cancel: Boolean) {
        _cancel.value = cancel
    }

    fun setAutoOff(minute: Int, context: Context) {
        if (minute == 0) return
        viewModelScope.launch {
            _autoOff.value = minute * 60
            _cancel.value = false
            while (_autoOff.value != 0) {
                if (_cancel.value) break
                _autoOff.value--
                delay(1000)
            }
            if (!_cancel.value) {
                pauseService(context)
                _autoOff.value = 0
            } else _cancel.value = false
        }

    }


    private val _isBound = MutableStateFlow(false)
    val isBound = _isBound

    private val _service = MutableStateFlow<NovelPlayerService?>(null)
    val service = _service

    private val _playerState = MutableStateFlow<ChapterPlayerState>(ChapterPlayerState.Loading)
    val playerState = _playerState

    private val _voiceList = MutableStateFlow<List<String>>(emptyList())
    val voiceList = _voiceList

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, ibinder: IBinder?) {
            val binder = ibinder as? NovelPlayerService.NovelPlayerBinder
            service.value = binder?.getService()

            // collect state từ service
            viewModelScope.launch {
                binder?.getStates()?.collect {
                    _playerState.value = it
                }
            }

            _isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service.value = null
            _playerState.value = ChapterPlayerState.Loading
            _isBound.value = false
        }
    }


    fun bindService(
        context: Context,
    ) {
        val intent = Intent(context, NovelPlayerService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService(context: Context) {
        if (_isBound.value) {
            context.unbindService(serviceConnection)
            _isBound.value = false
        }
    }


    fun loadNovelDetail(novelId: String) {
        _novelDetail.value = UIState.UILoading
        viewModelScope.launch {
            val result = getNovelDetailUseCase(novelId)
            if (result != null) {
                _novelDetail.value = UIState.UISuccess(result)
            } else {
                _novelDetail.value = UIState.UIError(UIErrorType.UnknownError)
            }
        }
    }

    fun startService(
        context: Context,
        beginChapterId: String,
    ) {
        val intent = Intent(context, NovelPlayerService::class.java).apply {
            action = NovelPlayerService.ACTION_SPEAK_CHAPTER
            putExtra(NovelPlayerService.chapterIdKey, beginChapterId)
        }
        context.startService(intent)
    }

    fun pauseService(
        context: Context,
    ) {
        val intent = Intent(context, NovelPlayerService::class.java).apply {
            action = NovelPlayerService.ACTION_PAUSE
        }
        context.startService(intent)
    }

    fun resumeService(
        context: Context,
    ) {
        val intent = Intent(context, NovelPlayerService::class.java).apply {
            action = NovelPlayerService.ACTION_RESUME
        }
        context.startService(intent)
    }

    fun chooseParagraph(
        context: Context,
        paragraphIndex: Int,
    ) {
        val intent = Intent(context, NovelPlayerService::class.java).apply {
            action = NovelPlayerService.ACTION_SPEAK_FROM_PARAGRAPH
            putExtra(NovelPlayerService.indexKey, paragraphIndex)
        }
        context.startService(intent)
    }

    fun nextChapter(
        context: Context,
    ) {
        val intent = Intent(context, NovelPlayerService::class.java).apply {
            action = NovelPlayerService.ACTION_NEXT_CHAPTER
        }
        context.startService(intent)
    }

    fun previousChapter(
        context: Context,
    ) {
        val intent = Intent(context, NovelPlayerService::class.java).apply {
            action = NovelPlayerService.ACTION_PREVIOUS_CHAPTER
        }
        context.startService(intent)
    }

    fun stopService(
        context: Context,
    ) {
        val intent = Intent(context, NovelPlayerService::class.java)
        context.stopService(intent)
    }

    fun setTTSConfig(
        config: TTSConfig,
    ) {
        viewModelScope.launch {
            setTTSConfigUseCase(config)
        }
    }

    fun getVoices() {
        if (service.value != null) {
            _voiceList.value = service.value!!.getVoices()
        }
    }
}