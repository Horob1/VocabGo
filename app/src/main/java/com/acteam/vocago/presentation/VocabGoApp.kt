package com.acteam.vocago.presentation

import android.app.Application
import com.acteam.vocago.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class VocabGoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Khởi tạo Koin
        startKoin {
            androidContext(this@VocabGoApp)
            modules(appModules)
        }
    }
}