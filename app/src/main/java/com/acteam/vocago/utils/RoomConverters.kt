package com.acteam.vocago.utils

import androidx.room.TypeConverter
import com.acteam.vocago.data.model.QuestionNewsDto
import com.acteam.vocago.data.model.TranslationNewsDto
import com.acteam.vocago.data.model.WordNewsDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomConverters {
    private val gson = Gson()

    // WordNewsDto List
    private val wordNewsDtoListType = object : TypeToken<List<WordNewsDto>>() {}.type

    @TypeConverter
    fun fromWordNewsDtoList(list: List<WordNewsDto>?): String? {
        return if (list == null) null else gson.toJson(list)
    }

    @TypeConverter
    fun toWordNewsDtoList(json: String?): List<WordNewsDto>? {
        return if (json == null) null else gson.fromJson(json, wordNewsDtoListType)
    }

    // QuestionNewsDto List
    private val questionNewsDtoListType = object : TypeToken<List<QuestionNewsDto>>() {}.type

    @TypeConverter
    fun fromQuestionNewsDtoList(list: List<QuestionNewsDto>?): String? {
        return if (list == null) null else gson.toJson(list)
    }

    @TypeConverter
    fun toQuestionNewsDtoList(json: String?): List<QuestionNewsDto>? {
        return if (json == null) null else gson.fromJson(json, questionNewsDtoListType)
    }

    // TranslationNewsDto List
    private val translationNewsDtoListType = object : TypeToken<List<TranslationNewsDto>>() {}.type

    @TypeConverter
    fun fromTranslationNewsDtoList(list: List<TranslationNewsDto>?): String? {
        return if (list == null) null else gson.toJson(list)
    }

    @TypeConverter
    fun toTranslationNewsDtoList(json: String?): List<TranslationNewsDto>? {
        return if (json == null) null else gson.fromJson(json, translationNewsDtoListType)
    }

    // String List (for tags)
    private val stringListType = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return if (list == null) null else gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String?): List<String>? {
        return if (json == null) null else gson.fromJson(json, stringListType)
    }
}