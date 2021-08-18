package com.example.flightappdemo.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context

open class AlertBuilder(
    private var title: String? = null,
    private var message: String? = null,
    private var positiveText: String? = null,
    private var negativeText: String? = null,
    private var isCancelable: Boolean = true
) {
    fun show(context: Context, makeFinish: Boolean = false) {
        if (makeFinish) {
            val dialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(isCancelable)
                .setMessage(message)
                .setPositiveButton(positiveText) { _, _ -> (context as Activity).finish() }
                .setNegativeButton(negativeText) { _, _ -> }
            dialog.show()
        } else {
            val dialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(isCancelable)
                .setMessage(message)
                .setPositiveButton(positiveText) { _, _ -> }
                .setNegativeButton(negativeText) { _, _ -> }
            dialog.show()
        }
    }
}