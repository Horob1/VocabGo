package com.acteam.vocago.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationChannelHelper {
    const val DOWNLOAD_CHANNEL_ID = "download_channel"
    const val NOVEL_SPEAKING_CHANNEL_ID = "novel_speaking_channel"


    fun createAllChannels(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        val downloadChannel = NotificationChannel(
            DOWNLOAD_CHANNEL_ID,
            "Download",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setSound(null, null)
        }

        val novelSpeakingChannel = NotificationChannel(
            NOVEL_SPEAKING_CHANNEL_ID,
            "Novel Speaking",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setSound(null, null)
        }
        manager.createNotificationChannel(downloadChannel)
        manager.createNotificationChannel(novelSpeakingChannel)
    }
}