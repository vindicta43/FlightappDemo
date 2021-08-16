package com.example.flightappdemo.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelCard
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class EditCardPage : AppCompatActivity() {
    private lateinit var dbRef: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var cardId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_card_page)

        dbRef = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        // textview
        val tvEditCardName = findViewById<TextView>(R.id.tvEditCardName)
        val tvEditCardNumber = findViewById<TextView>(R.id.tvEditCardNumber)
        val tvEditCardValidDate = findViewById<TextView>(R.id.tvEditCardValidDate)
        val tvEditCardCvv = findViewById<TextView>(R.id.tvEditCardCvv)
        val tvEditBalance = findViewById<TextView>(R.id.tvEditBalance)

        // edittext
        val etEditCardName = findViewById<TextInputEditText>(R.id.etEditCardName)
        val etEditCardNumber = findViewById<MaskedEditText>(R.id.etEditCardNumber)
        val etEditCardValidDate = findViewById<MaskedEditText>(R.id.etEditCardValidDate)
        val etEditCardCvv = findViewById<MaskedEditText>(R.id.etEditCardCvv)

        setAllText(
            tvEditCardName,
            tvEditCardNumber,
            tvEditCardValidDate,
            tvEditCardCvv,
            tvEditBalance,
            etEditCardName,
            etEditCardNumber,
            etEditCardValidDate,
            etEditCardCvv
        )

        // card name
        etEditCardName.addTextChangedListener {
            tvEditCardName.text = it
        }
        // card number
        etEditCardNumber.addTextChangedListener {
            tvEditCardNumber.text = it
        }
        // valid date
        etEditCardValidDate.addTextChangedListener {
            tvEditCardValidDate.text = it
        }
        // cvv
        etEditCardCvv.addTextChangedListener {
            tvEditCardCvv.text = it
        }

        // button
        val btnCardEditUpdate = findViewById<Button>(R.id.btnCardEditUpdate)
        btnCardEditUpdate.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Uyarı")
                .setMessage("Kartınızı güncellemek istiyor musunuz?")
                .setNegativeButton("Hayır") { _, _ ->

                }
                .setPositiveButton("Evet") { _, _ ->
                    cardId = intent.getStringExtra("cardId")!!
                    dbRef.collection("users/${auth.uid}/cards")
                        .document(cardId)
                        .set(
                            ModelCard(
                                tvEditCardName.text.toString(),
                                tvEditCardNumber.text.toString(),
                                tvEditCardValidDate.text.toString(),
                                tvEditCardCvv.text.toString(),
                                cardId,
                                tvEditBalance.text.toString()
                                    .toInt() + isBalanceEmpty()
                            )
                        )
                        .addOnSuccessListener {
                            val dialog = AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setTitle("Başarılı")
                                .setMessage("Kartınız başarıyla güncellendi.")
                                .setPositiveButton("Tamam") { _, _ ->
                                    finish()
                                }
                            dialog.show()
                        }
                }
            dialog.show()
        }

        val btnCardEditDelete = findViewById<Button>(R.id.btnCardEditDelete)
        btnCardEditDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Uyarı")
                .setMessage("Kartı silmek istediğinize emin misiniz?")
                .setPositiveButton("Evet") { _, _ ->
                    cardId = intent.getStringExtra("cardId")!!
                    dbRef.collection("users/${auth.uid}/cards").document(cardId).delete()
                        .addOnSuccessListener {
                            val dialog = AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setTitle("Başarılı")
                                .setMessage("Kartınız başarıyla silindi.")
                                .setPositiveButton("Tamam") { _, _ ->
                                    finish()
                                }
                            dialog.show()
                        }
                }
                .setNegativeButton("Hayır") { _, _ ->

                }
            dialog.show()
        }


        val btnCardEditCancel = findViewById<Button>(R.id.btnCardEditCancel)
        btnCardEditCancel.setOnClickListener {
            finish()
        }
    }

    private fun isBalanceEmpty(): Int {
        val balance = findViewById<EditText>(R.id.etEditCardBalance)
        return if (balance.text.isNullOrEmpty()) {
            0
        } else {
            balance.text.toString().toInt()
        }
    }

    private fun setAllText(
        tvEditCardName: TextView?,
        tvEditCardNumber: TextView?,
        tvEditCardValidDate: TextView?,
        tvEditCardCvv: TextView?,
        tvEditBalance: TextView?,
        etEditCardName: TextInputEditText?,
        etEditCardNumber: MaskedEditText?,
        etEditCardValidDate: MaskedEditText?,
        etEditCardCvv: MaskedEditText?
    ) {
        cardId = intent.getStringExtra("cardId")!!
        val cardRef = dbRef.collection("users/${auth.uid}/cards").whereEqualTo("id", cardId)
        cardRef.get()
            .addOnSuccessListener { cards ->
                for (card in cards) {
                    etEditCardName?.setText(card.get("cardName").toString())
                    etEditCardNumber?.setText(formatInput(card.get("cardNumber")))
                    etEditCardValidDate?.setText(formatInput(card.get("cardValidDate")))
                    etEditCardCvv?.setText(card.get("cardCvv").toString())

                    tvEditCardName?.text = card.get("cardName").toString()
                    tvEditCardNumber?.text = card.get("cardNumber").toString()
                    tvEditCardValidDate?.text = card.get("cardValidDate").toString()
                    tvEditCardCvv?.text = card.get("cardCvv").toString()
                    tvEditBalance?.text = card.get("balance").toString()
                }
            }
    }

    // format database string
    private fun formatInput(get: Any?): CharSequence {
        var outputString = get.toString()
        outputString = outputString.replace(" ", "").replace("/", "")
        return outputString
    }
}