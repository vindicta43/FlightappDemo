package com.example.flightappdemo.utils

import android.app.Notification
import android.util.Log
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.RuntimeException

class FirebaseService: FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.e("TAG", p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.e("TAG", p0.toString())
        // throw RuntimeException("crash test")
    }
}