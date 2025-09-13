package com.acteam.vocago.presentation.screen.noveldetail

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.acteam.vocago.R
import com.acteam.vocago.data.local.entity.ChapterEntity
import com.acteam.vocago.domain.usecase.GetChapterUseCase
import com.acteam.vocago.domain.usecase.SaveChapterUseCase
import com.acteam.vocago.utils.NotificationChannelHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.cancellation.CancellationException

class DownloadNovelService : Service(), KoinComponent {
    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val TAG = "DownloadNovelService"
        const val ACTION_DOWNLOAD = "ACTION_DOWNLOAD"
        const val ACTION_CANCEL = "ACTION_CANCEL"
    }

    private val getChapterUseCase by inject<GetChapterUseCase>()
    private val saveChapterUseCase by inject<SaveChapterUseCase>()
    private val binder = DownloadNovelBinder()

    private val downloadState = MutableStateFlow<DownloadServiceState>(DownloadServiceState.Idle)
    private var novelInfo: NovelInfo? = null
    private var downloadJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    inner class DownloadNovelBinder : Binder() {
        fun getService(): DownloadNovelService = this@DownloadNovelService
        fun getDownloadState() = downloadState
        fun getNovelInfo() = novelInfo
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handleIntent(intent)
        return START_STICKY
    }

    private fun buildNotification(
        progress: Int,
        indeterminate: Boolean = false,
    ): android.app.Notification {
        val info = novelInfo ?: return NotificationCompat.Builder(
            this, NotificationChannelHelper.DOWNLOAD_CHANNEL_ID
        ).build()

        return NotificationCompat.Builder(this, NotificationChannelHelper.DOWNLOAD_CHANNEL_ID)
            .setSmallIcon(R.drawable.outline_arrow_circle_down_24)
            .setContentTitle(info.name)
            .setContentText(getString(R.string.text_download_chapter))
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setProgress(100, progress, indeterminate)
            .build()
    }

    private fun sendDoneNotification(success: Int, total: Int) {
        val info = novelInfo ?: return
        val notification = NotificationCompat.Builder(
            this, NotificationChannelHelper.DOWNLOAD_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.outline_arrow_circle_down_24)
            .setContentTitle("${info.name} ${getString(R.string.text_download_done)}")
            .setContentText("$success/$total")
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
        manager.notify(NOTIFICATION_ID + 1, notification)
        manager.cancel(NOTIFICATION_ID)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            ACTION_DOWNLOAD -> {
                val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getSerializableExtra("novelInfo", NovelInfo::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getSerializableExtra("novelInfo") as? NovelInfo
                }
                data?.let {
                    novelInfo = it
                    startDownload(it)
                }
            }

            ACTION_CANCEL -> cancelDownload()
        }
    }

    private fun startDownload(novelInfo: NovelInfo) {
        downloadJob?.cancel()
        downloadState.value = DownloadServiceState.Downloading(0f)

        startForeground(NOTIFICATION_ID, buildNotification(0, indeterminate = true))

        downloadJob = serviceScope.launch {
            val chapterList = novelInfo.chapterList
            var success = 0

            chapterList.forEachIndexed { index, chapter ->
                ensureActive()
                try {
                    val data = getChapterUseCase(chapter._id)
                    if (data != null) {
                        success++
                        saveChapterUseCase(
                            ChapterEntity(
                                _id = data.chapter._id,
                                fictionId = data.chapter.fictionId,
                                chapterNumber = data.chapter.chapterNumber,
                                chapterTitle = data.chapter.chapterTitle,
                                content = data.chapter.content,
                                createdAt = data.chapter.createdAt,
                                nextChapterId = data.nextChapter?._id,
                                previousChapterId = data.previousChapter?._id
                            )
                        )
                    }

                    val progress = ((index + 1) * 100f / chapterList.size).toInt()
                    downloadState.value = DownloadServiceState.Downloading(progress.toFloat())

                    val manager =
                        getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
                    manager.notify(NOTIFICATION_ID, buildNotification(progress))
                } catch (e: CancellationException) {
                    return@launch
                } catch (e: Exception) {
                    Log.e(TAG, "Download error: ${e.message}", e)
                }
            }

            downloadState.value = DownloadServiceState.Idle
            withContext(Dispatchers.Main) {
                sendDoneNotification(success, chapterList.size)
                stopSelf()
            }
        }
    }

    private fun cancelDownload() {
        downloadJob?.cancel()
        downloadJob = null
        downloadState.value = DownloadServiceState.Idle
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
