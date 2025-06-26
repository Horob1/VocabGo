package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class GetAlarmListUseCase(
    private val alarmRepository: AlarmRepository,
) {
    operator fun invoke(): Flow<List<Alarm>> = alarmRepository.getAlarmList()
}