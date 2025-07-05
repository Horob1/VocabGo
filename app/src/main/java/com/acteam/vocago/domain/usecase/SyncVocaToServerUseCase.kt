package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.VocaSyncDto
import com.acteam.vocago.domain.remote.VocaRemoteDataSource
import com.acteam.vocago.domain.repository.VocaRepository

class SyncVocaToServerUseCase(
    private val repository: VocaRepository,
    private val remote: VocaRemoteDataSource,
) {
    suspend operator fun invoke() {
        try {
            val vocaLists = repository.getAllVocaList()
            val vocaWords = repository.getAllVoca()
            val vocaSyncDto = VocaSyncDto(vocaLists, vocaWords)
            val result = remote.sync(vocaSyncDto)
            if (result.isSuccess) {
                val data = result.getOrNull()
                repository.deleteVoca()
                repository.deleteAllVocaList()
                data?.vocaLists?.forEach {
                    repository.insertVocaList(it)
                }
                data?.vocaWords?.forEach {
                    repository.insertVoca(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}