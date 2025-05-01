package com.acteam.vocago.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.acteam.vocago.domain.model.AppTheme
import com.acteam.vocago.domain.usecase.GetThemeUseCase

// === LIGHT THEME ===
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF9E6B3F), // Tăng độ tương phản
    primaryContainer = Color(0xFF70441E),
    secondary = Color(0xFFFBDF8D), // Màu nhạt hơn
    secondaryContainer = Color(0xFFF7D58C),
    tertiary = Color(0xFFDBAB6C),
    background = Color(0xFFF5F5F5), // Nền sáng, dễ đọc
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF2A1D0F), // Chữ trên các phần tử thứ cấp rõ ràng
    onBackground = Color(0xFF2C1C14), // Màu văn bản sáng, dễ đọc
    onSurface = Color(0xFF3E2A19),
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF),
    outline = Color(0xFF8C7A4F), // Tăng độ tương phản cho outline
)

// === DARK THEME ===
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD4A373),
    primaryContainer = Color(0xFF8D6E52),
    secondary = Color(0xFFF3CBA5),
    secondaryContainer = Color(0xFFB48E6B),
    tertiary = Color(0xFFE6B17E),
    background = Color(0xFF121212), // Nền tối, giúp các phần tử sáng nổi bật hơn
    surface = Color(0xFF2A2420),
    onPrimary = Color(0xFF3E2B15),
    onSecondary = Color(0xFF3E2B1A),
    onBackground = Color(0xFFEFE7DD), // Màu chữ sáng, dễ đọc trên nền tối
    onSurface = Color(0xFFEFE7DD),
    error = Color(0xFFCF6679),
    onError = Color(0xFF1E1A16),
    outline = Color(0xFF7A7A7A), // Tăng độ tương phản cho outline tối
)

@Composable
fun VocaGoTheme(
    getTheme: GetThemeUseCase,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val userTheme = getTheme().collectAsState(initial = AppTheme.SYSTEM)

    val isDarkTheme = when (userTheme.value) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        else -> darkTheme
    }

    // Sử dụng dynamic color cho Android 12+ và màu sắc mặc định cho các hệ thống cũ hơn
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Áp dụng theme với typography và màu sắc đã chọn
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Bạn có thể mở rộng typography nếu cần
        content = content
    )
}
