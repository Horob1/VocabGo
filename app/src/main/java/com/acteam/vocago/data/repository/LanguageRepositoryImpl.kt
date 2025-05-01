package com.acteam.vocago.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.acteam.vocago.domain.model.AppLanguage
import com.acteam.vocago.domain.repository.LanguageRepository


class LanguageRepositoryImpl(context: Context) : LanguageRepository {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(LANGUAGE_PREF_NAME, Context.MODE_PRIVATE)

    override fun chooseLanguage(appLanguage: AppLanguage) {
        prefs.edit { putString(LANGUAGE_KEY, appLanguage.languageCode) }
    }

    override fun getLanguage(): String {
        return prefs.getString(LANGUAGE_KEY, AppLanguage.System.languageCode)
            ?: AppLanguage.System.languageCode
    }

    companion object {
        const val LANGUAGE_PREF_NAME = "LANGUAGE_PREF_NAME"
        const val LANGUAGE_KEY = "LANGUAGE_KEY"
    }
}