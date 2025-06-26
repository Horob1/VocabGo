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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CameraViewModel : ViewModel() {
    private val _detectedText = mutableStateOf("")
    val detectedText: State<String> = _detectedText

    var cameraHandler: Camera2Handler? = null

    var ocrBitmap by mutableStateOf<Bitmap?>(null)
    var ocrLines by mutableStateOf<List<OcrModel>>(emptyList())
    var isTranslated by mutableStateOf(false)

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
        cameraHandler?.release() // Giải phóng tài nguyên trước
        cameraHandler?.restartPreview() // Khởi động lại preview
    }

    suspend fun translateOverlayText(targetLang: String = "vi") {
        if (isTranslated) return

        val translated = ocrLines.map { line ->
            val response = withContext(Dispatchers.IO) {
                try {
                    val body = JSONObject().apply {
                        put("q", line.text)
                        put("source", "auto")
                        put("target", targetLang)
                        put("format", "text")
                    }

                    val requestBody =
                        body.toString().toRequestBody("application/json".toMediaType())
                    val request = Request.Builder()
                        .url("https://translate.argosopentech.com/translate")
                        .post(requestBody)
                        .build()

                    val client = OkHttpClient()
                    val result = client.newCall(request).execute()
                    val responseBody = result.body?.string() ?: ""

                    val json = JSONObject(responseBody)
                    if (json.has("translatedText")) {
                        json.getString("translatedText")
                    } else {
                        "[Dịch lỗi]"
                    }
                } catch (e: Exception) {
                    Log.e("TranslateAPI", "Lỗi dịch: ${e.message}")
                    "[Dịch lỗi]"
                }
            }

            line.copy(text = response)
        }

        ocrLines = translated
        isTranslated = true
    }


    override fun onCleared() {
        cameraHandler?.release()
    }
}
