package com.acteam.vocago.presentation.screen.user.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.domain.usecase.DeleteAlarmByIdUseCase
import com.acteam.vocago.domain.usecase.GetAlarmByIdUseCase
import com.acteam.vocago.domain.usecase.GetAlarmListUseCase
import com.acteam.vocago.domain.usecase.InsertAlarmUseCase
import com.acteam.vocago.domain.usecase.UpdateAlarmUseCase
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.user.alarm.component.DialogMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val getAlarmListUseCase: GetAlarmListUseCase,
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase,
    private val insertAlarmUseCase: InsertAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val deleteAlarmByIdUseCase: DeleteAlarmByIdUseCase,
) : ViewModel() {
    private val _alarmListUiState = MutableStateFlow<UIState<List<Alarm>>>(UIState.UILoading)
    val alarmListUiState = _alarmListUiState

    private val _alarmUiState = MutableStateFlow<UIState<Alarm>>(UIState.UILoading)
    val alarmUiState = _alarmUiState

    init {
        viewModelScope.launch {
            getAlarmList()
        }
    }

    private suspend fun getAlarmList() {
        getAlarmListUseCase().collect {
            _alarmListUiState.value = UIState.UISuccess(it)
        }
    }

    fun findAlarm(id: String) {
        viewModelScope.launch {
            _alarmUiState.value = UIState.UILoading
            val alarm = getAlarmByIdUseCase(id)
            if (alarm != null) {
                _alarmUiState.value = UIState.UISuccess(alarm)
            } else {
                setDefaultAlarm()
            }
        }
    }

    fun setDefaultAlarm() {
        _alarmUiState.value = UIState.UISuccess(
            Alarm(
                id = "",
                hour = 0,
                minute = 0,
                enabled = false,
                isLoop = false,
                label = "",
            )
        )
    }

    fun saveAlarm(alarm: Alarm, mode: DialogMode) {
        viewModelScope.launch {
            if (mode == DialogMode.ADD) {
                insertAlarmUseCase(alarm)
            } else {
                updateAlarmUseCase(alarm)
            }
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            deleteAlarmByIdUseCase(alarm)
        }
    }

}