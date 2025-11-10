package com.screensnap.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.screensnap.core.notification.ScreenSnapNotificationConstants
import com.screensnap.sdk.ScreenSnapSDK
import com.screensnap.sdk.ScreenSnapSDKConfig
import com.screensnap.sdk.VideoQuality
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        setupNotificationChannel()
        initializeSDK()
    }

    private fun initializeSDK() {
        val config = ScreenSnapSDKConfig(
            enableUpload = true,
            uploadEndpoint = "https://httpbin.org/post", // Test endpoint for demo
            maxRecordingDurationMinutes = 3, // 3 minutes for demo
            videoQuality = VideoQuality.MEDIUM, // Balanced quality for demo
            enableAudio = true,
            enableFloatingButton = true,
            uploadTimeout = 30000L, // 30 seconds timeout
            enableNotifications = true,
            appName = "QA Sample App"
        )

        ScreenSnapSDK.initialize(this, config)
    }

    private fun setupNotificationChannel() {
        val notificationChannel =
            NotificationChannel(
                ScreenSnapNotificationConstants.CHANNEL_ID,
                ScreenSnapNotificationConstants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW,
            )
        notificationChannel.description =
            ScreenSnapNotificationConstants.CHANNEL_DESCRIPTION
        notificationManager.createNotificationChannel(notificationChannel)
    }
}