package com.acteam.vocago.presentation.screen.dictionary

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class DictionaryViewModel : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions

    fun onSearchTextChange(newText: String) {
        Log.d("DictionaryVM", "onSearchTextChange: $newText")
        _searchText.value = newText
        fetchSuggestions(newText)
    }

    private fun fetchSuggestions(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isBlank()) {
                _suggestions.value = emptyList()
                return@launch
            }

            val urlString = "https://api.datamuse.com/words?sp=${query.lowercase()}*&max=5"
            Log.d("DictionaryVM", "🔗 URL: $urlString")

            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val responseCode = connection.responseCode
                Log.d("DictionaryVM", "📦 Response code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_ACCEPTED) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    Log.d("DictionaryVM", "✅ JSON Response: $response")

                    val jsonArray = JSONArray(response)
                    val words = List(jsonArray.length()) { i ->
                        jsonArray.getJSONObject(i).getString("word")
                    }
                    _suggestions.value = words
                } else {
                    Log.e("DictionaryVM", "❌ HTTP Error: $responseCode")
                    _suggestions.value = emptyList()
                }

                connection.disconnect()
            } catch (e: Exception) {
                Log.e("DictionaryVM", "❌ Lỗi fetchSuggestions: ${e.message}")
                _suggestions.value = emptyList()
            }
        }
    }

}
