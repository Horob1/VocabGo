package com.acteam.vocago.utils

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Enum biểu diễn loại thiết bị và chiều xoay hiện tại.
 */
enum class DeviceType {
    Mobile,
    TabletPortrait,
    TabletLandscape
}

/**
 * Hàm trả về loại thiết bị hiện tại dựa vào chiều rộng màn hình và orientation.
 */
@Composable
fun getDeviceType(): DeviceType {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val orientation = configuration.orientation

    return when {
        screenWidth < 600 -> DeviceType.Mobile
        orientation == Configuration.ORIENTATION_PORTRAIT -> DeviceType.TabletPortrait
        else -> DeviceType.TabletLandscape
    }
}

/**
 * Hàm responsive chung cho các giá trị Int.
 */
@Composable
fun responsiveValue(
    mobile: Int,
    tabletPortrait: Int,
    tabletLandscape: Int
): Int = when (getDeviceType()) {
    DeviceType.Mobile -> mobile
    DeviceType.TabletPortrait -> tabletPortrait
    DeviceType.TabletLandscape -> tabletLandscape
}

/**
 * Hàm responsive cho TextSize.
 */
@Composable
fun responsiveSP(
    mobile: Int,
    tabletPortrait: Int,
    tabletLandscape: Int
): TextUnit = when (getDeviceType()) {
    DeviceType.Mobile -> mobile.sp
    DeviceType.TabletPortrait -> tabletPortrait.sp
    DeviceType.TabletLandscape -> tabletLandscape.sp
}

/**
 * Hàm responsive cho Padding hoặc Spacing.
 */
@Composable
fun responsiveDP(
    mobile: Int,
    tabletPortrait: Int,
    tabletLandscape: Int
): Dp = when (getDeviceType()) {
    DeviceType.Mobile -> mobile.dp
    DeviceType.TabletPortrait -> tabletPortrait.dp
    DeviceType.TabletLandscape -> tabletLandscape.dp
}

/**
 * Dành cho việc kiểm tra bên ngoài Compose nếu cần.
 */
fun Context.isTablet(): Boolean {
    return resources.configuration.smallestScreenWidthDp >= 600
}

/**
 * Wrapper dùng để định nghĩa nhóm giá trị responsive một cách rõ ràng.
 */
data class ResponsiveValue<T>(
    val mobile: T,
    val tabletPortrait: T,
    val tabletLandscape: T
) {
    @Composable
    fun resolve(): T = when (getDeviceType()) {
        DeviceType.Mobile -> mobile
        DeviceType.TabletPortrait -> tabletPortrait
        DeviceType.TabletLandscape -> tabletLandscape
    }
}
