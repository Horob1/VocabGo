package com.acteam.vocago.utils

import android.os.Bundle
import com.meticha.triggerx.provider.TriggerXDataProvider
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object AlarmUtils {
    fun millisUntil(hour: Int, minute: Int, second: Int = 0): Long {
        val now = LocalDateTime.now()
        var target = now.withHour(hour)
            .withMinute(minute)
            .withSecond(second)
            .withNano(0)

        if (target.isBefore(now)) {
            target = target.plusDays(1)
        }
        return ChronoUnit.MILLIS.between(now, target)
    }
}

class StaticProvider : TriggerXDataProvider {
    override suspend fun provideData(alarmId: Int, alarmType: String): Bundle {
        return Bundle().apply {
            putInt("alarmId", alarmId)
            putString("alarmType", alarmType)
        }
    }
}