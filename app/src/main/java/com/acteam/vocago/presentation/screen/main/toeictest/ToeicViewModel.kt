package com.acteam.vocago.presentation.screen.main.toeictest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.SubmitRequest
import com.acteam.vocago.data.model.TestResultDto
import com.acteam.vocago.data.model.TestResultListDto
import com.acteam.vocago.data.model.ToeicDetailDto
import com.acteam.vocago.data.model.ToeicDto
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.domain.usecase.GetTestResultDetailUseCase
import com.acteam.vocago.domain.usecase.GetToeicDetailUseCase
import com.acteam.vocago.domain.usecase.GetToeicListUseCase
import com.acteam.vocago.domain.usecase.GetToeicResultUseCase
import com.acteam.vocago.domain.usecase.SubmitToeicUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ToeicViewModel(
    private val getToeicListUseCase: GetToeicListUseCase,
    private val getToeicDetailUseCase: GetToeicDetailUseCase,
    private val submitToeicUseCase: SubmitToeicUseCase,
    private val getLocalUserProfileUseCase: GetLocalUserProfileUseCase,
    private val getToeicResultUseCase: GetToeicResultUseCase,
    private val getTestResultDetailUseCase: GetTestResultDetailUseCase
) : ViewModel() {

    private val _toeicListState = MutableStateFlow<UIState<ToeicDto>>(UIState.UILoading)
    val toeicListState: StateFlow<UIState<ToeicDto>> = _toeicListState.asStateFlow()

    private val _toeicDetailState = MutableStateFlow<UIState<ToeicDetailDto>>(UIState.UILoading)
    val toeicDetailState: StateFlow<UIState<ToeicDetailDto>> = _toeicDetailState

    val _submitState = MutableStateFlow<UIState<TestResultDto>?>(null)
    val submitState: StateFlow<UIState<TestResultDto>?> = _submitState

    private val _resultState = MutableStateFlow<UIState<List<TestResultListDto>>>(UIState.UILoading)
    val resultState: StateFlow<UIState<List<TestResultListDto>>> = _resultState

    private val _resultDetailState =
        MutableStateFlow<UIState<List<TestResultListDto>>>(UIState.UILoading)
    val resultDetailState: StateFlow<UIState<List<TestResultListDto>>> = _resultDetailState

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId


    fun loadCurrentUserId() {
        viewModelScope.launch {
            getLocalUserProfileUseCase().collect { user ->
                _userId.value = user?._id
            }
        }
    }

    fun getToeicList() {
        viewModelScope.launch {
            _toeicListState.value = UIState.UILoading

            val result = getToeicListUseCase()
            if (result.isSuccess && result.getOrNull() != null) {
                val data = result.getOrNull()!!
                _toeicListState.value = UIState.UISuccess(data)
            } else {
                _toeicListState.value = UIState.UIError(UIErrorType.NotFoundError)
            }
        }
    }

    fun getToeicDetail(id: String) {
        viewModelScope.launch {
            _toeicDetailState.value = UIState.UILoading

            val result = getToeicDetailUseCase(id)
            if (result.isSuccess && result.getOrNull() != null) {
                val detail = result.getOrNull()!!
                _toeicDetailState.value = UIState.UISuccess(detail)
            } else {
                _toeicDetailState.value = UIState.UIError(UIErrorType.NotFoundError)
            }
        }
    }

    fun submitToeicTest(request: SubmitRequest) {
        viewModelScope.launch {
            _submitState.value = UIState.UILoading
            val result = submitToeicUseCase(request)

            if (result.isSuccess && result.getOrNull() != null) {
                val data = result.getOrNull()!!
                _submitState.value = UIState.UISuccess(data)
            } else {
                _submitState.value = UIState.UIError(UIErrorType.ServerError)
            }
        }
    }


    fun getToeicResult(id: String) {
        viewModelScope.launch {
            _resultState.value = UIState.UILoading
            val result = getToeicResultUseCase(id)
            if (result.isSuccess && result.getOrNull() != null) {
                val data = result.getOrNull()!!
                _resultState.value = UIState.UISuccess(data)
            } else {
                _resultState.value = UIState.UIError(UIErrorType.ServerError)
            }
        }
    }

    fun getToeicResultDetail(id: String) {
        viewModelScope.launch {
            _resultDetailState.value = UIState.UILoading
            val result = getTestResultDetailUseCase(id)
            if (result.isSuccess && result.getOrNull() != null) {
                val data = result.getOrNull()!!
                _resultDetailState.value = UIState.UISuccess(data)
            } else {
                _resultDetailState.value = UIState.UIError(UIErrorType.ServerError)
            }
        }
    }


}