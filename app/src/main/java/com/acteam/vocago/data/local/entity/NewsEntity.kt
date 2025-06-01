package com.acteam.vocago.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

data class NewsEntityWord(
    val a1: Int,
    val a2: Int,
    val b1: Int,
    val b2: Int,
)

@Entity(tableName = "news")
data class NewsEntity(
    // auto id để dễ get nhé anh Công, nó sẽ tự tăng theo thứ tự tăng dần
    @PrimaryKey(autoGenerate = true)
    val autoId: Long = 0L,
    val _id: String,
    val category: String,
    val title: String,
    val url: String,
    val coverImage: String,
    val views: Int,
    val level: String,
    val words: NewsEntityWord,
    val createdAt: String,
    // Cái này dùng để dùng với paging nhé
    val page: Int,
)