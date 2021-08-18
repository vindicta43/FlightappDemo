package com.example.flightappdemo.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelCard
import com.example.flightappdemo.utils.AlertBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AddCardPage : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card_page)

        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "AddCardPage")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Add Card")
        }

        // textview
        val tvAddCardName = findViewById<TextView>(R.id.tvAddCardName)
        val tvAddCardNumber = findViewById<TextView>(R.id.tvAddCardNumber)
        val tvAddCardValidDate = findViewById<TextView>(R.id.tvAddCardValidDate)
        val tvAddCardCvv = findViewById<TextView>(R.id.tvAddCardCvv)
        val tvAddBalance = findViewById<TextView>(R.id.tvAddBalance)
        tvAddBalance.text = "0"

        // edittext
        val etAddCardName = findViewById<TextInputEditText>(R.id.etAddCardName)
        etAddCardName.addTextChangedListener {
            tvAddCardName.text = it
        }
        val etAddCardNumber = findViewById<MaskedEditText>(R.id.etAddCardNumber)
        etAddCardNumber.addTextChangedListener {
            tvAddCardNumber.text = it
        }
        val etAddCardValidDate = findViewById<MaskedEditText>(R.id.etAddCardValidDate)
        etAddCardValidDate.addTextChangedListener {
            tvAddCardValidDate.text = it
        }

        val etAddCardCvv = findViewById<MaskedEditText>(R.id.etAddCardCvv)
        etAddCardCvv.addTextChangedListener {
            tvAddCardCvv.text = it
        }

        // button
        val btnCardAdd = findViewById<Button>(R.id.btnCardAdd)
        btnCardAdd.setOnClickListener {
            if (etAddCardCvv.text.isNullOrEmpty() ||
                etAddCardName.text.isNullOrEmpty() ||
                etAddCardNumber.text.isNullOrEmpty() ||
                etAddCardValidDate.text.isNullOrEmpty()
            ) {
                AlertBuilder("Uyarı", "Lütfen boş yerleri tamamlayınız", "Tamam").show(this)
            } else {
                val auth = Firebase.auth
                val dbRef = FirebaseFirestore.getInstance()

                val id = dbRef.collection("users/${auth.uid}/cards").document().id
                dbRef.collection("users/${auth.uid}/cards")
                    .document(id)
                    .set(
                        ModelCard(
                            tvAddCardName.text.toString(),
                            tvAddCardNumber.text.toString(),
                            tvAddCardValidDate.text.toString(),
                            tvAddCardCvv.text.toString(),
                            id,
                            tvAddBalance.text.toString().toInt()
                        )
                    )
                    .addOnCompleteListener {
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                            param(FirebaseAnalytics.Param.ITEM_NAME, "btnCardAdd")
                        }

                        AlertBuilder(
                            "Başarılı",
                            "Kartınız başarıyla eklendi. Kartlarım menüsünden kartınızı görüntüleyebilirsiniz",
                            "Tamam"
                        ).show(this, true)
                    }
            }
        }

        val btnCancelAdd = findViewById<Button>(R.id.btnCancelAdd)
        btnCancelAdd.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(FirebaseAnalytics.Param.ITEM_NAME, "btnCancelAdd")
            }
            finish()
        }
    }
}