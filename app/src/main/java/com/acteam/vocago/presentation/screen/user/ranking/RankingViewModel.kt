package com.acteam.vocago.presentation.screen.user.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.RankingData
import com.acteam.vocago.domain.usecase.GetRankingUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RankingViewModel(
    private val getRankingUseCase: GetRankingUseCase
) : ViewModel() {
    private val _rankingState = MutableStateFlow<UIState<RankingData>>(UIState.UILoading)
    val rankingState: StateFlow<UIState<RankingData>> = _rankingState

    fun getRanking() {
        viewModelScope.launch {
            _rankingState.value = UIState.UILoading
            try {
                val response = getRankingUseCase()
                _rankingState.value = UIState.UISuccess(response.data)
            } catch (e: ApiException) {
                e.printStackTrace()
                _rankingState.value = UIState.UIError(UIErrorType.BadRequestError)
            }
        }
    }
}
