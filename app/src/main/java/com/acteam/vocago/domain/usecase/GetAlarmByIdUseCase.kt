package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.domain.repository.AlarmRepository

class GetAlarmByIdUseCase(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(id: Int): Alarm? = alarmRepository.getAlarmById(id)
}