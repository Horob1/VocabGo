package com.acteam.vocago.domain.repository

import com.acteam.vocago.data.local.entity.AlarmEntity
import com.acteam.vocago.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun getAlarmList(): Flow<List<Alarm>>

    suspend fun getAlarmById(id: Int): Alarm?

    suspend fun insertAlarm(alarm: AlarmEntity): Int

    suspend fun updateAlarm(alarm: Alarm)

    suspend fun deleteAlarmById(id: Int)
}