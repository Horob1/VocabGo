package com.acteam.vocago.utils

object ClickGuard {
    private val lastClickMap = mutableMapOf<String, Long>()

    fun canClick(key: String, threshold: Long = 800L): Boolean {
        val now = System.currentTimeMillis()
        val lastClickTime = lastClickMap[key] ?: 0L
        return if (now - lastClickTime >= threshold) {
            lastClickMap[key] = now
            true
        } else {
            false
        }
    }
}