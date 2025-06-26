package com.acteam.vocago.receiver

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.service.AlarmSoundService
import com.acteam.vocago.utils.AlarmHelper
import com.acteam.vocago.utils.NotificationChannelHelper

class AlarmReceiver : BroadcastReceiver() {
    private val alarmHelper: AlarmHelper by lazy {
        org.koin.core.context.GlobalContext.get().get<AlarmHelper>()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent?) {
        val alarmId = intent?.getStringExtra("alarmId")
        val isLoop = intent?.getBooleanExtra("isLoop", false) ?: false
        val label = intent?.getStringExtra("label") ?: ""
        val hour = intent?.getIntExtra("hour", 0) ?: 0
        val minute = intent?.getIntExtra("minute", 0) ?: 0

        val alarm = Alarm(
            id = alarmId ?: "",
            hour = hour,
            minute = minute,
            enabled = true,
            label = label,
            isLoop = isLoop
        )


        // Bắt đầu phát nhạc
        val soundIntent = Intent(context, AlarmSoundService::class.java)
        context.startService(soundIntent)

        // Nếu là loop thì đặt lại báo thức
        if (isLoop) {
            alarmHelper.setAlarm(alarm)
        }

        // PendingIntent mở app (đồng thời dừng nhạc)
        val openIntent = Intent(context, StopAlarmReceiver::class.java).apply {
            putExtra("open", true)
            putExtra("alarmId", alarmId)
        }
        val openPendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            openIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // PendingIntent chỉ tắt nhạc
        val stopIntent = Intent(context, StopAlarmReceiver::class.java).apply {
            putExtra("open", false)
            putExtra("alarmId", alarmId)
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            1002,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val largeImage = BitmapFactory.decodeResource(context.resources, R.drawable.alarm_img)

        // Gửi thông báo
        val notification =
            NotificationCompat.Builder(context, NotificationChannelHelper.ALARM_CHANNEL_ID)
                .setSmallIcon(R.drawable.sun)
                .setContentTitle(context.getString(R.string.title_alarm))
                .setContentText(label)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(largeImage)
                )
                .setLargeIcon(largeImage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .addAction(
                    R.drawable.baseline_open_in_new_24,
                    context.getString(R.string.btn_open),
                    openPendingIntent
                )
                .addAction(
                    R.drawable.outline_close_24,
                    context.getString(R.string.btn_cancel),
                    stopPendingIntent
                )
                .build()

        NotificationManagerCompat.from(context).notify(alarmId.hashCode(), notification)
    }
}
