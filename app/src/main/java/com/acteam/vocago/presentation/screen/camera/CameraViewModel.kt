package com.acteam.vocago.presentation.screen.camera

import OcrModel
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.net.URLEncoder

class CameraViewModel : ViewModel() {
    private val _detectedText = mutableStateOf("")
    val detectedText: State<String> = _detectedText

    var cameraHandler: Camera2Handler? = null

    var ocrBitmap by mutableStateOf<Bitmap?>(null)
    var ocrLines by mutableStateOf<List<OcrModel>>(emptyList())
    var isTranslated by mutableStateOf(false)

    var isTranslating by mutableStateOf(false)
        private set

    fun onTextDetected(text: String) {
        _detectedText.value = text
    }

    fun setOcrImageAndLines(bitmap: Bitmap, lines: List<OcrModel>) {
        ocrBitmap = bitmap
        ocrLines = lines
        _detectedText.value = lines.joinToString("\n") { it.text }
        isTranslated = false
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun reset() {
        ocrBitmap = null
        ocrLines = emptyList()
        _detectedText.value = ""
        isTranslated = false
        isTranslating = false
        cameraHandler?.release()
        cameraHandler?.restartPreview()
    }

    suspend fun translateOverlayText(targetLang: String = "vi") {
        // ✅ Tránh lặp và tránh khi đang dịch
        if (isTranslated || isTranslating) return

        isTranslating = true

        try {
            val client = OkHttpClient()

            val translated = ocrLines.map { line ->
                val response = withContext(Dispatchers.IO) {
                    try {
                        val encodedText = URLEncoder.encode(line.text, "UTF-8")
                        val url =
                            "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=$targetLang&dt=t&q=$encodedText"

                        val request = Request.Builder()
                            .url(url)
                            .get()
                            .build()

                        val result = client.newCall(request).execute()
                        val responseBody = result.body?.string() ?: ""
                        val json = JSONArray(responseBody)
                        val translatedText =
                            json.getJSONArray(0).getJSONArray(0).getString(0)

                        translatedText
                    } catch (e: Exception) {
                        Log.e("TranslateAPI", "Lỗi dịch: ${e.message}", e)
                        "[Dịch lỗi]"
                    }
                }

                line.copy(text = response)
            }

            ocrLines = translated
            isTranslated = true
        } finally {
            isTranslating = false
        }
    }

    override fun onCleared() {
        cameraHandler?.release()
    }
}
