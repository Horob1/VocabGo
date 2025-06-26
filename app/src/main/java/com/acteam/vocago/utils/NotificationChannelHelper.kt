package com.acteam.vocago.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannelHelper {
    const val ALARM_CHANNEL_ID = "alarm_channel"
    private const val ALARM_CHANNEL_NAME = "Báo thức"
    private const val ALARM_CHANNEL_DESCRIPTION = "Kênh dành cho báo thức phát nhạc"

    private fun createAlarmChannel(manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ALARM_CHANNEL_ID,
                ALARM_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = ALARM_CHANNEL_DESCRIPTION
                enableVibration(true)
            }

            manager.createNotificationChannel(channel)
        }
    }

    fun createAllChannels(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        createAlarmChannel(manager)
    }

}