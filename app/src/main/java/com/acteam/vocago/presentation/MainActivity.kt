package com.acteam.vocago.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.acteam.vocago.domain.model.AppTheme
import com.acteam.vocago.domain.usecase.GetDynamicColorUseCase
import com.acteam.vocago.domain.usecase.GetStartScreenUseCase
import com.acteam.vocago.domain.usecase.GetThemeUseCase
import com.acteam.vocago.presentation.navigation.SetupNavGraph
import com.acteam.vocago.presentation.socket.SocketViewModel
import com.acteam.vocago.presentation.ui.theme.VocaGoTheme
import com.acteam.vocago.utils.isTablet
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : AppCompatActivity(), KoinComponent {
    private val getOnBoardingStateUseCase: GetStartScreenUseCase by inject()
    private val getThemeUseCase: GetThemeUseCase by inject()
    private val getDynamicColorUseCase: GetDynamicColorUseCase by inject()
    private var isReady = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("MainActivity", if (isGranted)
            "✅ Notification permission granted"
        else
            "⚠️ Notification permission denied")
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
        requestNotificationPermission()

        if (!isTablet()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !isReady }
        enableEdgeToEdge()

        setContent {
            val userTheme by getThemeUseCase().collectAsState(initial = AppTheme.SYSTEM)
            val dynamicColor by getDynamicColorUseCase().collectAsState(initial = false)

            LaunchedEffect(userTheme, dynamicColor) { isReady = true }

            val startDestination = remember { getOnBoardingStateUseCase() }
            koinViewModel<SocketViewModel>()

            VocaGoTheme(userTheme = userTheme, dynamicColorEnabled = dynamicColor) {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController, startDestination = startDestination)
            }
        }
    }

    /**
     * 🔔 Tạo Notification Channel duy nhất (ID "default")
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Thông báo chung",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Thông báo quan trọng từ ứng dụng"
                enableVibration(true)
                enableLights(true)
                setShowBadge(true)
            }

            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)

            Log.d("MainActivity", "✅ Default notification channel created")
        }
    }

    /**
     * 🪪 Xin quyền thông báo (Android 13+)
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d("MainActivity", "✅ Notification permission already granted")
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    Log.d("MainActivity", "ℹ️ Showing rationale for notification permission")
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    Log.d("MainActivity", "📱 Requesting notification permission...")
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            Log.d("MainActivity", "✅ Permission not required (Android < 13)")
        }
    }
}
