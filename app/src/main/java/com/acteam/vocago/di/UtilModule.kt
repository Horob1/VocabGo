package com.acteam.vocago.di

import com.acteam.vocago.utils.AlarmHelper
import org.koin.dsl.module

val untilModule = module {
    single {
        AlarmHelper(get())
    }
}