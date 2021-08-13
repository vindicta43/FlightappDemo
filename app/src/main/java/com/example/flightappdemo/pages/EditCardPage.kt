package com.example.flightappdemo.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import br.com.sapereaude.maskedEditText.MaskedEditText
import com.example.flightappdemo.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class EditCardPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_card_page)

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
        val btnCardEditDelete = findViewById<Button>(R.id.btnCardEditDelete)
        val btnCardEditCancel = findViewById<Button>(R.id.btnCardEditCancel)
    }

    private fun setAllText(
        tvEditCardName: TextView?,
        tvEditCardNumber: TextView?,
        tvEditCardValidDate: TextView?,
        tvEditCardCvv: TextView?,
        tvEditBalance: TextView?,
    ) {
        val dbReb = FirebaseFirestore.getInstance()
        val auth = Firebase.auth
        val cardId = intent.getStringExtra("cardId")
        val cardRef = dbReb.collection("users/${auth.uid}/cards").whereEqualTo("id", cardId)
        cardRef.get()
            .addOnSuccessListener { cards ->
                for (card in cards) {
                    tvEditCardName?.text = card.get("cardName").toString()
                    tvEditCardNumber?.text = card.get("cardNumber").toString()
                    tvEditCardValidDate?.text = card.get("cardValid").toString()
                    tvEditCardCvv?.text = card.get("cardCvv").toString()
                    tvEditBalance?.text = card.get("balance").toString()
                }
            }
    }
}