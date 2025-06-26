package com.acteam.vocago.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.acteam.vocago.presentation.MainActivity
import com.acteam.vocago.service.AlarmSoundService

class StopAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val open = intent?.getBooleanExtra("open", false) ?: false
        val alarmId = intent?.getStringExtra("alarmId")
        val soundIntent = Intent(context, AlarmSoundService::class.java)
        context.stopService(soundIntent)
        NotificationManagerCompat.from(context).cancel(alarmId.hashCode())
        if (open) {
            val openIntent = Intent(context, MainActivity::class.java)
            openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(openIntent)
        }
    }
}
