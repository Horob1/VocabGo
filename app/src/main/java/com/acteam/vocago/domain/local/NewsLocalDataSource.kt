package com.acteam.vocago.domain.local

import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {
    fun getChosenCategories(): Flow<List<String>>
    suspend fun updateChosenCategories(categories: List<String>)
    fun getChosenLevel(): Flow<String>
    suspend fun updateChosenLevel(level: String)

}