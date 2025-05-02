package com.acteam.vocago.di

import org.koin.core.module.Module

val appModules = listOf<Module>(
    networkModule,
    viewModelModule,
    repositoryModule,
    useCaseModule,
    localModule,
    remoteModule
)