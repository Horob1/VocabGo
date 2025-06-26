package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.domain.repository.AlarmRepository
import com.acteam.vocago.utils.AlarmHelper

class DeleteAlarmByIdUseCase(
    private val alarmRepository: AlarmRepository,
    private val alarmHelper: AlarmHelper,
) {
    suspend operator fun invoke(alarm: Alarm) {
        alarmRepository.deleteAlarmById(alarm.id)
        alarmHelper.cancelAlarm(alarm)
    }
}