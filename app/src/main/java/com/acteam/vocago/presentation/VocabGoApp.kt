package com.acteam.vocago.presentation

import android.app.Application
import com.acteam.vocago.di.appModules
import com.acteam.vocago.utils.NotificationChannelHelper
import com.acteam.vocago.utils.StaticProvider
import com.meticha.triggerx.dsl.TriggerX
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class VocabGoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Khởi tạo các kênh thông báo
        NotificationChannelHelper.createAllChannels(this)
        // Khởi tạo Koin
        startKoin {
            androidContext(this@VocabGoApp)
            modules(appModules)
        }
        // Khởi tạo TriggerX
        TriggerX.init(this) {

            /* UI that opens when the alarm fires */
            activityClass = AppAlarmActivity::class.java
            alarmDataProvider = StaticProvider()
            /* Foreground-service notification */
            useDefaultNotification(
                title = "Alarm running",
                message = "Tap to open",
                channelName = "Alarm Notifications"
            )
        }
    }
}