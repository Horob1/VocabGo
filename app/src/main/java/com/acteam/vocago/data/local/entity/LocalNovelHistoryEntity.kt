package com.acteam.vocago.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_novel_history")
data class LocalNovelHistoryEntity(
    @PrimaryKey val novelId: String,
    val chapterId: String,
)