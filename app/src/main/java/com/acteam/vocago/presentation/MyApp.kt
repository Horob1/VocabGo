package com.acteam.vocago.presentation

import android.app.Application
import com.acteam.vocago.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin


class MyApp : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        // Khởi tạo Koin
        startKoin {
            androidContext(this@MyApp)
            modules(appModules)
        }
    }
}