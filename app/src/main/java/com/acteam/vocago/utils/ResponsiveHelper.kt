package com.acteam.vocago.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class DeviceType {
    Mobile,
    Tablet
}

@Composable
fun getDeviceType(): DeviceType {
    val configuration = LocalConfiguration.current
    return if (configuration.screenWidthDp < 600) {
        DeviceType.Mobile
    } else {
        DeviceType.Tablet
    }
}

@Composable
fun responsiveValue(
    phone: Int,
    tablet: Int
) = if (getDeviceType() == DeviceType.Mobile) phone else tablet

@Composable
fun responsiveFontSize(
    mobile: Int,
    tablet: Int
) = if (getDeviceType() == DeviceType.Mobile) mobile.sp else tablet.sp

@Composable
fun responsivePadding(
    mobile: Int,
    tablet: Int
) = if (getDeviceType() == DeviceType.Mobile) mobile.dp else tablet.dp


@Composable
fun responsiveSpacing(
    mobile: Int,
    tablet: Int
) = if (getDeviceType() == DeviceType.Mobile) mobile.dp else tablet.dp

fun Context.isTablet(): Boolean {
    return resources.configuration.smallestScreenWidthDp >= 600
}