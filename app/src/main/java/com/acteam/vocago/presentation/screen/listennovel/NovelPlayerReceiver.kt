package com.acteam.vocago.presentation.screen.listennovel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NovelPlayerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val serviceIntent = Intent(context, NovelPlayerService::class.java).apply {
            this.action = action
        }
        context.startService(serviceIntent)
    }
}