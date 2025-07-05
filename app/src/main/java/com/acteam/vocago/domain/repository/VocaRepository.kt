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

    suspend fun deleteAllVocaList()
    fun getAllVocaLists(): Flow<List<VocaListEntity>>

    suspend fun getAllVocaList(): List<VocaListEntity>

    // === VocaEntity ===
    suspend fun insertVoca(voca: VocaEntity): Long
    suspend fun deleteVoca(voca: VocaEntity)
    fun getVocasByListId(listId: Int): Flow<List<VocaEntity>>

    suspend fun getAllVoca(): List<VocaEntity>

    suspend fun deleteVoca()

    // === Relation ===
    fun getVocaListWithVocas(listId: Int): Flow<VocaListWithVocas>
}
