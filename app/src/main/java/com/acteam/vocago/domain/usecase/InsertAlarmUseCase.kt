package com.acteam.vocago.domain.usecase

import android.content.Context
import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.domain.repository.AlarmRepository
import com.acteam.vocago.utils.AlarmUtils.millisUntil
import com.meticha.triggerx.TriggerXAlarmScheduler

class InsertAlarmUseCase(
    private val alarmRepository: AlarmRepository,
    private val context: Context,
) {
    suspend operator fun invoke(alarm: Alarm) {
        val alarmId = alarmRepository.insertAlarm(alarm.toEntity())
        if (!alarm.enabled) return

        val triggerTime = System.currentTimeMillis() + millisUntil(alarm.hour, alarm.minute)

        TriggerXAlarmScheduler().scheduleAlarm(
            context = context,
            triggerAtMillis = triggerTime,
            type = "LEARNING_ALARM",
            alarmId = alarmId,
        )
    }
}
