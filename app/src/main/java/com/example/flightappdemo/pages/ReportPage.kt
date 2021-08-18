package com.example.flightappdemo.pages

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.example.flightappdemo.R
import com.example.flightappdemo.utils.AlertBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import iamutkarshtiwari.github.io.ananas.editimage.EditImageActivity
import iamutkarshtiwari.github.io.ananas.editimage.ImageEditorIntentBuilder
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ReportPage : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var imagePath: Uri
    private lateinit var oldPath: Uri
    private lateinit var editResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var ivReportImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_page)
        // init result launchers
        setupActivityResultLaunchers()
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ReportPage")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Report")
        }

        ivReportImage = findViewById(R.id.ivReportImage)

        val btnPickImage = findViewById<Button>(R.id.btnPickImage)
        btnPickImage.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(FirebaseAnalytics.Param.ITEM_NAME, "btnPickImage")
            }
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            pickResultLauncher.launch(intent)
            setResult(RESULT_OK, intent)
        }

        val btnEditImage = findViewById<Button>(R.id.btnEditImage)
        btnEditImage.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(FirebaseAnalytics.Param.ITEM_NAME, "btnEditImage")
            }
            ivReportImage = findViewById(R.id.ivReportImage)
            val imageSrc: Drawable? = ivReportImage.drawable
            // if image doesn't selected
            if (imageSrc == null) {
                Toast.makeText(this, "Önce bir fotoğraf seçin", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val outputFile = generateEditFile()
                    val intent = ImageEditorIntentBuilder(
                        this,
                        getRealPathFromURI(imagePath, this),
                        outputFile?.absolutePath
                    )
                        .withAddText()
                        .withPaintFeature()
                        .withFilterFeature()
                        .withRotateFeature()
                        .withCropFeature()
                        .withBrightnessFeature()
                        .withSaturationFeature()
                        .withStickerFeature()
                        .withEditorTitle("Düzenle")
                        .forcePortrait(true)
                        .setSupportActionBarVisibility(false)
                        .build()
                    EditImageActivity.start(editResultLauncher, intent, this)
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    Log.e("TAG", e.message.toString())
                }
            }
        }

        val etReport = findViewById<TextInputEditText>(R.id.etReport)

        val btnSendReport = findViewById<Button>(R.id.btnSendReport)
        btnSendReport.setOnClickListener {
            if (!etReport.text.isNullOrEmpty()) {
                val dialog = ProgressDialog(this)
                try {
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                        param(FirebaseAnalytics.Param.ITEM_NAME, "btnSendReport")
                    }
                    val auth = Firebase.auth
                    val db = Firebase.firestore

                    dialog.setMessage("Gönderiliyor")
                    dialog.show()

                    // format for email--time name
                    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
                    val now = Date()
                    var fileName = auth.currentUser?.email + "--" + formatter.format(now)
                    val storageRef = FirebaseStorage.getInstance().getReference(fileName)

                    // if a photo has selected, empty will be changed
                    fileName = "empty"
                    if (this::imagePath.isInitialized) {
                        fileName = auth.currentUser?.email + "--" + formatter.format(now)
                        storageRef.putFile(imagePath)
                            .addOnSuccessListener {

                            }
                    }

                    val report = hashMapOf(
                        "sender" to auth.currentUser?.email,
                        "reportText" to etReport.text.toString(),
                        "storageLocation" to fileName
                    )
                    db.collection("reports")
                        .add(report)
                        .addOnSuccessListener {
                            AlertBuilder("Başarılı", "Şikayetiniz başarıyla gönderildi.", "Tamam", isCancelable = false)
                                .show(this, true)
                        }
                } catch (e: Exception) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
            } else {
                AlertBuilder("Hata","Lütfen bir şikayet metni yazınız.", "Tamam")
                    .show(this)
            }
        }
    }

    // all of below are photo editor codes
    private fun setupActivityResultLaunchers() {
        pickResultLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                imagePath = intent?.data!!
                oldPath = intent.data!!
            }
        }

        editResultLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    val isImageEdit = intent?.getBooleanExtra(EditImageActivity.IS_IMAGE_EDITED, false)
                    if (isImageEdit == true) {
                        val outputPath = intent.getStringExtra(ImageEditorIntentBuilder.OUTPUT_PATH)
                        imagePath = Uri.fromFile(File(outputPath!!))
                        ivReportImage.setImageURI(imagePath)
                    } else {
                        ivReportImage.setImageURI(oldPath)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            ivReportImage.setImageURI(data?.data)
        }
    }

    private fun generateEditFile(): File? {
        return getEmptyFile(
            "flight_app_edited"
                    + System.currentTimeMillis() + ".jpg"
        )
    }

    private fun getEmptyFile(name: String?): File? {
        val folder: File? = createFolders()
        if (folder != null) {
            if (folder.exists()) {
                return File(folder, name)
            }
        }
        return null
    }

    private fun createFolders(): File? {
        val baseDir: File = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            ?: return Environment.getExternalStorageDirectory()
        val cacheFolder = File(baseDir, "Flight_App")
        if (cacheFolder.exists()) return cacheFolder
        if (cacheFolder.isFile) cacheFolder.delete()
        return if (cacheFolder.mkdirs()) cacheFolder else Environment.getExternalStorageDirectory()
    }

    private fun getRealPathFromURI(uri: Uri, activity: Activity): String? {
        val cursor: Cursor =
            activity.contentResolver
                .query(uri, null, null, null, null)!!
        cursor.moveToFirst()
        val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

}