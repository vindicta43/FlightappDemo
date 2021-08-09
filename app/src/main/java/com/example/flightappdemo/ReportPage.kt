package com.example.flightappdemo

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import java.lang.Exception

class ReportPage : AppCompatActivity() {
    private val PHOTO_PICK_CODE = 101

    private lateinit var ivReportImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_page)

        ivReportImage = findViewById(R.id.ivReportImage)
        val btnPickImage = findViewById<Button>(R.id.btnPickImage)
        btnPickImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PHOTO_PICK_CODE)
        }

        val btnEditImage = findViewById<Button>(R.id.btnEditImage)
        btnEditImage.setOnClickListener {
            ivReportImage = findViewById(R.id.ivReportImage)
            val imageSrc: Drawable? = ivReportImage.drawable
            // if image doesn't selected
            if (imageSrc == null) {
                Toast.makeText(this, "Önce bir resim seçin", Toast.LENGTH_SHORT).show()
            } else {
//                try {
//                    var output: String? = ""
//                    val intent = ImageEditorIntentBuilder(
//                        this,
//                        ivReportImage.drawable.constantState.toString(),
//                        output
//                    )
//                        .withAddText() // Add the features you need
//                        .withPaintFeature()
//                        .withFilterFeature()
//                        .withRotateFeature()
//                        .withCropFeature()
//                        .withBrightnessFeature()
//                        .withSaturationFeature()
//                        .withBeautyFeature()
//                        .withStickerFeature()
//                        .forcePortrait(true)  // Add this to force portrait mode (It's set to false by default)
//                        .setSupportActionBarVisibility(false) // To hide app's default action bar
//                        .build();
//
//                    EditImageActivity.start(editResultLauncher, intent, this)
//                } catch (e: Exception) {
//                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
//                }
            }
        }
    }
}