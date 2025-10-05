package com.acteam.vocago.presentation.screen.main.chat.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun TvNoise(
    modifier: Modifier = Modifier.fillMaxSize(),
    intensity: Float = 1.0f // Cho phép điều chỉnh độ mạnh của nhiễu
) {
    var noiseBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var time by remember { mutableStateOf(0f) }
    val density = LocalDensity.current

    var canvasSize by remember { mutableStateOf(Size(0f, 0f)) }

    LaunchedEffect(canvasSize, intensity) {
        if (canvasSize.width > 0 && canvasSize.height > 0) {
            while (true) {
                val width = with(density) { canvasSize.width.toDp().value.toInt() }
                val height = with(density) { canvasSize.height.toDp().value.toInt() }

                // Kích thước nhỏ hơn để tối ưu performance
                val scaledWidth = min(width / 6, 150)
                val scaledHeight = min(height / 6, 200)

                noiseBitmap = generateLightOmeTvNoise(scaledWidth, scaledHeight, time, intensity)
                time += 0.08f
                delay(50) // Giảm frequency để mượt hơn
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        canvasSize = size

        // Background gradient - chỉ khi intensity cao
        if (intensity > 0.7f) {
            drawRect(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(
                        Color(0x15000000),
                        Color(0x25000000),
                        Color(0x35000000)
                    ),
                    radius = size.width * 0.7f
                ),
                size = size
            )
        }

        noiseBitmap?.let { bitmap ->
            drawIntoCanvas { canvas ->
                // Sử dụng paint với alpha để không che khuất hoàn toàn
                val paint = android.graphics.Paint().apply {
                    alpha = (intensity * 180).toInt().coerceIn(0, 255) // Giảm opacity
                }
                canvas.nativeCanvas.drawBitmap(
                    bitmap,
                    null,
                    android.graphics.RectF(0f, 0f, size.width, size.height),
                    paint
                )
            }
        }

        // Hiệu ứng overlay nhẹ
        drawLightOmeTvEffects(time, intensity)
    }
}

private fun DrawScope.drawLightOmeTvEffects(time: Float, intensity: Float) {
    val baseAlpha = intensity * 0.15f // Giảm alpha tổng thể

    // Scan lines - ít hơn và nhẹ hơn
    if (intensity > 0.3f) {
        val scanLineCount = 8
        repeat(scanLineCount) { i ->
            val progress = (time + i * 0.5f) % 3f
            val y = (progress / 3f) * size.height
            val alpha = (sin(progress * 3.14159f) * baseAlpha).coerceIn(0f, 0.1f)

            drawRect(
                color = Color.Cyan.copy(alpha = alpha),
                topLeft = Offset(0f, y),
                size = Size(size.width, 1.dp.toPx())
            )
        }
    }

    // Glitch rectangles - ít và nhỏ hơn
    if (intensity > 0.5f) {
        repeat(3) {
            if (Random.nextFloat() < 0.3f) { // Chỉ hiện ngẫu nhiên
                val x = Random.nextFloat() * size.width * 0.9f
                val y = Random.nextFloat() * size.height * 0.9f
                val w = Random.nextFloat() * 40f + 10f
                val h = Random.nextFloat() * 8f + 2f
                val alpha = baseAlpha * 0.3f

                val glitchColors = listOf(
                    Color.Red.copy(alpha = alpha),
                    Color.Green.copy(alpha = alpha),
                    Color.Blue.copy(alpha = alpha)
                )

                drawRect(
                    color = glitchColors[it % glitchColors.size],
                    topLeft = Offset(x, y),
                    size = Size(w, h)
                )
            }
        }
    }

    // RGB split effect - rất nhẹ
    if (intensity > 0.8f) {
        val splitOffset = sin(time * 1.5f) * 1f

        repeat(10) {
            if (Random.nextFloat() < 0.1f) {
                val x = Random.nextFloat() * size.width
                val y = Random.nextFloat() * size.height
                val radius = 0.5f

                drawCircle(
                    color = Color.Red.copy(alpha = baseAlpha * 0.2f),
                    radius = radius,
                    center = Offset(x + splitOffset, y)
                )

                drawCircle(
                    color = Color.Blue.copy(alpha = baseAlpha * 0.2f),
                    radius = radius,
                    center = Offset(x - splitOffset, y)
                )
            }
        }
    }

    // Subtle static lines - chỉ ở viền
    repeat(5) {
        val isTop = Random.nextBoolean()
        val y = if (isTop) Random.nextFloat() * 50f else size.height - Random.nextFloat() * 50f
        val x = Random.nextFloat() * size.width
        val width = Random.nextFloat() * 20f + 5f
        val alpha = baseAlpha * 0.4f

        drawRect(
            color = Color.White.copy(alpha = alpha),
            topLeft = Offset(x, y),
            size = Size(width, 1f)
        )
    }
}

fun generateLightOmeTvNoise(
    width: Int,
    height: Int,
    time: Float,
    intensity: Float
): android.graphics.Bitmap {
    val bitmap = createBitmap(width, height)
    val pixels = IntArray(width * height)

    for (y in 0 until height) {
        for (x in 0 until width) {
            val index = y * width + x

            val noise = Random.nextFloat()
            val timeNoise = sin(time + x * 0.1f + y * 0.1f) * 0.3f + 0.7f
            val combined = (noise * 0.7f + timeNoise * 0.3f) * intensity

            val color = when {
                combined < 0.3f -> Color.Transparent.toArgb() // Trong suốt nhiều hơn
                combined < 0.5f -> {
                    // Color noise nhẹ
                    val r = Random.nextFloat() * 0.2f
                    val g = Random.nextFloat() * 0.3f
                    val b = Random.nextFloat() * 0.4f
                    Color(r, g, b, 0.15f * intensity).toArgb()
                }

                combined < 0.7f -> {
                    // Grayscale static nhẹ
                    val intensity_val = Random.nextFloat() * 0.3f
                    Color(intensity_val, intensity_val, intensity_val, 0.2f * intensity).toArgb()
                }

                else -> {
                    // White static rất nhẹ
                    Color.White.copy(alpha = Random.nextFloat() * 0.25f * intensity).toArgb()
                }
            }

            pixels[index] = color
        }
    }

    // Horizontal lines - ít hơn và mờ hơn
    if (intensity > 0.6f) {
        repeat(2) {
            val lineY = Random.nextInt(height)
            for (x in 0 until width) {
                val index = lineY * width + x
                if (Random.nextFloat() < 0.3f) {
                    pixels[index] = Color.Cyan.copy(alpha = 0.1f * intensity).toArgb()
                }
            }
        }
    }

    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return bitmap
}