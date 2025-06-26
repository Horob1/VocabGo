package com.acteam.vocago.domain.model

import com.acteam.vocago.data.local.entity.AlarmEntity

data class Alarm(
    val id: String,
    val hour: Int,
    val minute: Int,
    val enabled: Boolean,
    val label: String,
    val isLoop: Boolean,
) {
    fun toEntity(): AlarmEntity {
        return AlarmEntity(
            id = id,
            hour = hour,
            minute = minute,
            enabled = enabled,
            label = label,
            isLoop = isLoop
        )
    }
}