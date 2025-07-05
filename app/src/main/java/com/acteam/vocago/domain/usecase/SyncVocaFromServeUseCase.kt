package com.acteam.vocago.domain.usecase

import com.acteam.vocago.domain.remote.VocaRemoteDataSource
import com.acteam.vocago.domain.repository.VocaRepository

class SyncVocaFromServeUseCase(
    val vocaRepository: VocaRepository,
    val remote: VocaRemoteDataSource,
) {
    suspend operator fun invoke() {
        try {

            val result = remote.getVoca()
            if (result.isSuccess) {
                vocaRepository.deleteVoca()
                vocaRepository.deleteAllVocaList()
                val data = result.getOrNull()
                data?.vocaLists?.forEach {
                    vocaRepository.insertVocaList(it)
                }
                data?.vocaWords?.forEach {
                    vocaRepository.insertVoca(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }

    }
}