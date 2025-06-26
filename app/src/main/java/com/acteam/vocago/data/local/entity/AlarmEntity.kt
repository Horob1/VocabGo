package com.acteam.vocago.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acteam.vocago.domain.model.Alarm
import java.util.UUID

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val hour: Int,
    val minute: Int,
    val enabled: Boolean = true,
    val label: String = "",
    val isLoop: Boolean = false,
) {
    fun toAlarm(): Alarm {
        return Alarm(
            id = id,
            hour = hour,
            minute = minute,
            enabled = enabled,
            label = label,
            isLoop = isLoop
        )
    }
}