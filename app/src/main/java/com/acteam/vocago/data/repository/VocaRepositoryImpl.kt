package com.acteam.vocago.data.repository


import com.acteam.vocago.data.local.dao.VocaDao
import com.acteam.vocago.data.local.entity.VocaEntity
import com.acteam.vocago.data.local.entity.VocaListEntity
import com.acteam.vocago.data.local.entity.VocaListWithVocas
import com.acteam.vocago.domain.repository.VocaRepository
import kotlinx.coroutines.flow.Flow

class VocaRepositoryImpl(private val dao: VocaDao) : VocaRepository {
    override suspend fun insertVocaList(list: VocaListEntity): Long = dao.insertVocaList(list)

    override suspend fun updateVocaList(list: VocaListEntity) = dao.updateVocaList(list)

    override suspend fun deleteVocaList(list: VocaListEntity) = dao.deleteVocaList(list)

    override fun getAllVocaLists(): Flow<List<VocaListEntity>> = dao.getAllVocaLists()

    override suspend fun insertVoca(voca: VocaEntity): Long = dao.insertVoca(voca)

    override suspend fun deleteVoca(voca: VocaEntity) = dao.deleteVoca(voca)

    override fun getVocasByListId(listId: Int): Flow<List<VocaEntity>> =
        dao.getVocasByListId(listId)

    override fun getVocaListWithVocas(listId: Int): Flow<VocaListWithVocas> =
        dao.getVocaListWithWords(listId)
}
