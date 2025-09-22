package com.acteam.vocago.domain.usecase

import android.content.Context
import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.domain.repository.AlarmRepository
import com.meticha.triggerx.TriggerXAlarmScheduler

class DeleteAlarmByIdUseCase(
    private val alarmRepository: AlarmRepository,
    private val context: Context,
) {
    suspend operator fun invoke(alarm: Alarm) {
        alarmRepository.deleteAlarmById(alarm.id)
        TriggerXAlarmScheduler().cancelAlarm(context, alarm.id)
    }
}