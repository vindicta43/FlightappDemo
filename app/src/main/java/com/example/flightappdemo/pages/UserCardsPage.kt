package com.example.flightappdemo.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelCard
import com.example.flightappdemo.utils.CardsAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserCardsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_cards_page)

        val auth = Firebase.auth
        val dbRef = Firebase.firestore
        val cardsRef = dbRef.collection("users/${auth.uid}/cards")
        cardsRef.get()
            .addOnSuccessListener { cards ->
                var cardsList = arrayListOf<ModelCard>()
                for (card in cards) {
                    cardsList.add(
                        ModelCard(
                            card.get("cardName").toString(),
                            card.get("cardNumber").toString(),
                            card.get("cardValid").toString(),
                            card.get("cardCvv").toString(),
                            card.get("id").toString(),
                            card.get("balance").toString().toInt()
                        )
                    )
                }
                val recyclerUserCards = findViewById<RecyclerView>(R.id.recyclerUserCards)
                recyclerUserCards.layoutManager = LinearLayoutManager(this)
                recyclerUserCards.adapter = CardsAdapter(cardsList)
            }
    }
}