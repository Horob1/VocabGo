package com.acteam.vocago.domain.repository

import com.acteam.vocago.data.local.entity.VocaEntity
import com.acteam.vocago.data.local.entity.VocaListEntity
import com.acteam.vocago.data.local.entity.VocaListWithVocas
import kotlinx.coroutines.flow.Flow

interface VocaRepository {
    // === VocaListEntity ===
    suspend fun insertVocaList(list: VocaListEntity): Long
    suspend fun updateVocaList(list: VocaListEntity)
    suspend fun deleteVocaList(list: VocaListEntity)
    fun getAllVocaLists(): Flow<List<VocaListEntity>>

    // === VocaEntity ===
    suspend fun insertVoca(voca: VocaEntity): Long
    suspend fun deleteVoca(voca: VocaEntity)
    fun getVocasByListId(listId: Int): Flow<List<VocaEntity>>

    // === Relation ===
    fun getVocaListWithVocas(listId: Int): Flow<VocaListWithVocas>
}
