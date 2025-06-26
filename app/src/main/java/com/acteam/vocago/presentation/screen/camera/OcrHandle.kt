package com.acteam.vocago.presentation.screen.camera

import OcrModel
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.IntSize
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun OcrOverlayImage(bitmap: Bitmap, lines: List<OcrModel>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val imageBitmap = bitmap.asImageBitmap()

        // Scale ảnh để phủ canvas
        drawImage(
            image = imageBitmap,
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )

        val scaleX = size.width / bitmap.width
        val scaleY = size.height / bitmap.height

        for (line in lines) {
            val rect = line.boundingBox

            val left = rect.left * scaleX
            val top = rect.top * scaleY
            val width = rect.width() * scaleX
            val height = rect.height() * scaleY

            // Vẽ nền mờ
            drawRect(
                color = Color.Black.copy(alpha = 0.3f),
                topLeft = Offset(left, top),
                size = Size(width, height)
            )

            // Tính toán cỡ chữ tối ưu (60% chiều cao)
            val textSize = height.coerceAtMost(40f) * 0.8f

            drawContext.canvas.nativeCanvas.drawText(
                line.text,
                left,
                top + height - 8, // Đẩy chữ lên một chút để không bị cắt
                android.graphics.Paint().apply {
                    color = android.graphics.Color.WHITE
                    this.textSize = textSize
                    isAntiAlias = true
                }
            )
        }
    }
}


fun performOcr(
    bitmap: Bitmap,
    onResult: (List<OcrModel>) -> Unit,
    onText: (String) -> Unit
) {
    val inputImage = InputImage.fromBitmap(bitmap, 0)
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    recognizer.process(inputImage)
        .addOnSuccessListener { result ->
            val lines = mutableListOf<OcrModel>()
            for (block in result.textBlocks) {
                for (line in block.lines) {
                    line.boundingBox?.let {
                        lines.add(OcrModel(line.text, it))
                    }
                }
            }
            onResult(lines)
            onText(result.text)
        }
        .addOnFailureListener {
            Log.e("OCR", "Failed: ${it.message}")
        }
}
