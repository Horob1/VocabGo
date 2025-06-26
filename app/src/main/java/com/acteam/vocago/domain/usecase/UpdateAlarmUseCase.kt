package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.domain.repository.AlarmRepository
import com.acteam.vocago.utils.AlarmHelper

class UpdateAlarmUseCase(
    private val alarmRepository: AlarmRepository,
    private val alarmHelper: AlarmHelper,
) {
    suspend operator fun invoke(alarm: Alarm) {
        alarmRepository.updateAlarm(alarm)
        // cancel and reschedule the alarm
        alarmHelper.cancelAlarm(alarm)
        alarmHelper.setAlarm(alarm)
    }

}