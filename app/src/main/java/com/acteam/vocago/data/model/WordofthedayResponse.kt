package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WordOfTheDaySimpleResponse(
    val success: Boolean,
    val data: WordOfTheDaySimpleDto,
    val message: String
)


@Serializable
data class WordOfTheDaySimpleDto(
    val word: String,
    val types: List<WordTypeSimpleDto>,
    val phonetics: List<WordPhoneticSimpleDto>,
    val translations: List<WordTranslationSimpleDto>
)

@Serializable
data class WordTypeSimpleDto(
    val targetLanguage: String,
    val type: List<String>
)

@Serializable
data class WordPhoneticSimpleDto(
    val countryCode: String,
    val pronunciation: String
)

@Serializable
data class WordTranslationSimpleDto(
    val targetLanguage: String,
    val translation: String
)
