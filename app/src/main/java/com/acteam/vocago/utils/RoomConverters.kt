package com.acteam.vocago.utils

import androidx.room.TypeConverter
import com.acteam.vocago.data.local.entity.NewsEntityWord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomConverters {
    private val gson = Gson()

    // WordNews
    private val wordNews = object : TypeToken<NewsEntityWord>() {}.type

    @TypeConverter
    fun fromWordNews(words: NewsEntityWord): String {
        return gson.toJson(words)
    }

    @TypeConverter
    fun toWordNews(json: String): NewsEntityWord {
        return gson.fromJson(json, wordNews)
    }
}