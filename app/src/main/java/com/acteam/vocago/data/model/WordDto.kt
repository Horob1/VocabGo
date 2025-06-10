package com.acteam.vocago.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WordTypeDto(
    val type: List<String>,
    val targetLanguage: String,
)

@Serializable
data class WordPhoneticDto(
    val countryCode: String,
    val pronunciation: String,
)

@Serializable
data class WordTranslationDto(
    val targetLanguage: String,
    val translation: String,
)

@Serializable
data class WordDefinitionDto(
    val definition: String,
    val translations: List<WordTranslationDto>,
    val examples: List<WordExampleDto>,
)

@Serializable
data class WordExampleDto(
    val original: String,
    val translations: WordTranslationDto,
)

@Serializable
data class WordIdiomDto(
    val idiom: String,
    val translations: List<WordTranslationDto>,
)

@Serializable
data class WordDto(
    val _id: String,
    val word: String,
    val types: List<WordTypeDto>,
    val phonetics: List<WordPhoneticDto>,
    val translations: List<WordTranslationDto>,
    val examples: List<WordDefinitionDto>,
    val level: String,
    val frequency: String,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val idioms: List<WordIdiomDto>,
    val createdAt: String,
    val updatedAt: String,
)

