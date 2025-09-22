package com.acteam.vocago.presentation.screen.noveldetail

interface DownloadServiceState {
    object Idle : DownloadServiceState
    data class Downloading(
        val progress: Float,
    ) : DownloadServiceState
}