package com.acteam.vocago.presentation.screen.noveldetail

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.model.NovelDetail
import com.acteam.vocago.domain.usecase.GetLastReadChapterUseCase
import com.acteam.vocago.domain.usecase.GetLocalChapterFlowUseCase
import com.acteam.vocago.domain.usecase.GetNovelDetailUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NovelDetailViewModel(
    val getNovelDetailUseCase: GetNovelDetailUseCase,
    val getReadChapterUseCase: GetLastReadChapterUseCase,
    val getLocalChapterFlowUseCase: GetLocalChapterFlowUseCase,
) : ViewModel() {
    private val _novelDetail = MutableStateFlow<UIState<NovelDetail>>(UIState.UILoading)
    private val _readChapter = MutableStateFlow("")

    private val _service = MutableStateFlow<DownloadNovelService?>(null)
    val service: StateFlow<DownloadNovelService?> = _service

    private val _downloadState = MutableStateFlow<DownloadServiceState>(DownloadServiceState.Idle)
    val downloadState: StateFlow<DownloadServiceState> = _downloadState

    private val _isBind = MutableStateFlow(false)
    val isBind: StateFlow<Boolean> = _isBind

    val readChapter = _readChapter
    val novelDetail = _novelDetail

    fun loadNovel(id: String) {
        _novelDetail.value = UIState.UILoading
        viewModelScope.launch {
            val result = getNovelDetailUseCase(id)
            _novelDetail.value =
                if (result == null) UIState.UIError(UIErrorType.UnknownError) else UIState.UISuccess(
                    result
                )
            val readChapter = getReadChapterUseCase(id)
            _readChapter.value = readChapter?.chapterId ?: ""
        }
    }

    fun bindService(context: Context) {
        val intent = Intent(context, DownloadNovelService::class.java)
        context.bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder) {
                val binder = service as DownloadNovelService.DownloadNovelBinder
                _service.value = binder.getService()
                viewModelScope.launch {
                    binder.getDownloadState().collect {
                        _downloadState.value = it
                    }
                }
                _isBind.value = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                _service.value = null
                _downloadState.value = DownloadServiceState.Idle
                _isBind.value = false
            }
        }, Context.BIND_AUTO_CREATE)
    }
}