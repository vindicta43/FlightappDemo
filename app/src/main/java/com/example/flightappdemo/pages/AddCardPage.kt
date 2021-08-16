package com.example.flightappdemo.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelCard
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AddCardPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card_page)

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
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Uyarı")
                    .setCancelable(true)
                    .setMessage("Lütfen boş yerleri tamamlayınız.")
                    .setPositiveButton("Tamam") { _, _ -> }
                dialog.show()
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
                        val dialog = AlertDialog.Builder(this)
                            .setTitle("Başarılı")
                            .setCancelable(false)
                            .setMessage("Kartınız başarıyla eklendi. Kartlarım menüsünden kartınızı görüntüleyebilirsiniz")
                            .setPositiveButton("Tamam") { _, _ ->
                                finish()
                            }
                        dialog.show()
                    }
            }
        }

        val btnCancelAdd = findViewById<Button>(R.id.btnCancelAdd)
        btnCancelAdd.setOnClickListener {
            finish()
        }
    }
}