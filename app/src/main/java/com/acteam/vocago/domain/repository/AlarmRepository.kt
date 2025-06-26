package com.acteam.vocago.domain.repository

import com.acteam.vocago.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun getAlarmList(): Flow<List<Alarm>>

    suspend fun getAlarmById(id: String): Alarm?

    suspend fun insertAlarm(alarm: Alarm)

    suspend fun updateAlarm(alarm: Alarm)

    suspend fun deleteAlarmById(id: String)
}