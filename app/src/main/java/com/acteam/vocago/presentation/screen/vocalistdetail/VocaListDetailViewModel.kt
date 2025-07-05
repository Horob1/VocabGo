package com.acteam.vocago.presentation.screen.vocalistdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.local.entity.VocaEntity
import com.acteam.vocago.data.local.entity.VocaListWithVocas
import com.acteam.vocago.domain.usecase.DeleteVocaUseCase
import com.acteam.vocago.domain.usecase.GetVocaListDetailUseCase
import com.acteam.vocago.domain.usecase.LoadImageUseCase
import com.acteam.vocago.domain.usecase.SaveWordToVocaListUseCase
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VocaListDetailViewModel(
    private val getVocaListDetailUseCase: GetVocaListDetailUseCase,
    private val saveWordToVocaListUseCase: SaveWordToVocaListUseCase,
    private val loadImagesUseCase: LoadImageUseCase,
    private val deleteVocaUseCase: DeleteVocaUseCase,
) : ViewModel() {
    private val _listId = MutableStateFlow<Int?>(null)
    val listId: StateFlow<Int?> = _listId

    fun setId(id: Int) {
        _listId.value = id
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val vocaListDetailData: StateFlow<VocaListWithVocas?> = listId
        .filterNotNull()
        .flatMapLatest { id ->
            getVocaListDetailUseCase(id) // giả sử đây trả về Flow<VocaListDetail?>
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _images = MutableStateFlow<UIState<List<String>>>(UIState.UILoading)
    val images: StateFlow<UIState<List<String>>> = _images

    fun loadImages(word: String) {
        _images.value = UIState.UILoading
        viewModelScope.launch {
            _images.value = UIState.UISuccess(loadImagesUseCase(word))
        }
    }

    fun saveWordToVocalist(voca: VocaEntity) {
        viewModelScope.launch {
            try {
                saveWordToVocaListUseCase(voca)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteVoca(voca: VocaEntity) {
        viewModelScope.launch {
            try {
                deleteVocaUseCase(voca)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}