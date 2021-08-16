package com.example.flightappdemo.utils

import androidx.fragment.app.FragmentActivity
import com.google.zxing.integration.android.IntentIntegrator

open class QrScanner(fragmentActivity: FragmentActivity?) {
    init {
        val scanner = IntentIntegrator(fragmentActivity)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            .setBeepEnabled(false)
            .setOrientationLocked(false)
            .initiateScan()
    }
}


