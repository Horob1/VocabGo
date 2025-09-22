package com.acteam.vocago.data.repository

import com.acteam.vocago.data.local.dao.AlarmDao
import com.acteam.vocago.data.local.entity.AlarmEntity
import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlarmRepositoryImpl(
    private val alarmDao: AlarmDao,
) : AlarmRepository {
    override fun getAlarmList(): Flow<List<Alarm>> =
        alarmDao.getAllAlarms().map { it.map(AlarmEntity::toAlarm) }

    override suspend fun getAlarmById(id: Int): Alarm? = alarmDao.getAlarmById(id)?.toAlarm()

    override suspend fun insertAlarm(alarm: AlarmEntity) = alarmDao.insertAlarm(alarm).toInt()
    
    override suspend fun updateAlarm(alarm: Alarm) = alarmDao.updateAlarm(alarm.toEntity())

    override suspend fun deleteAlarmById(id: Int) = alarmDao.deleteAlarmById(id)
}