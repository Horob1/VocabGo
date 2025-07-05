package com.acteam.vocago.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.acteam.vocago.data.local.entity.VocaEntity
import com.acteam.vocago.data.local.entity.VocaListEntity
import com.acteam.vocago.data.local.entity.VocaListWithVocas
import kotlinx.coroutines.flow.Flow

@Dao
interface VocaDao {
    @Insert
    suspend fun insertVocaList(list: VocaListEntity): Long

    @Update
    suspend fun updateVocaList(list: VocaListEntity)

    @Delete
    suspend fun deleteVocaList(list: VocaListEntity)

    @Query("SELECT * FROM voca_list")
    fun getAllVocaLists(): Flow<List<VocaListEntity>>

    @Query("DELETE FROM voca_list")
    suspend fun deleteAllVocaList()

    @Query("SELECT * FROM voca_list")
    suspend fun getAllVocaList(): List<VocaListEntity>

    @Insert
    suspend fun insertVoca(voca: VocaEntity): Long

    @Delete
    suspend fun deleteVoca(voca: VocaEntity)

    @Query("SELECT * FROM voca WHERE list_id = :listId")
    fun getVocasByListId(listId: Int): Flow<List<VocaEntity>>

    @Transaction
    @Query("SELECT * FROM voca_list WHERE id = :listId")
    fun getVocaListWithWords(listId: Int): Flow<VocaListWithVocas>

    @Query("SELECT * FROM voca")
    suspend fun getAllVoca(): List<VocaEntity>

    //delete all voca in a list by listId
    @Query("DELETE FROM voca WHERE list_id = :listId")
    suspend fun deleteVocaByListId(listId: Int)

    @Query("DELETE FROM voca")
    suspend fun deleteVocas()
}
