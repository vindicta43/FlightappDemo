package com.example.flightappdemo.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelFlightPurchased
import com.example.flightappdemo.utils.FlightBoughtAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserFlightsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_flights_page)

        val recyclerUserFlights = findViewById<RecyclerView>(R.id.recyclerUserFlights)
        recyclerUserFlights.layoutManager = LinearLayoutManager(this)
        val boughtList = arrayListOf<ModelFlightPurchased>()

        val dbRef = Firebase.firestore
        val auth = Firebase.auth

        val boughtRef = dbRef.collection("users/${auth.uid}/flights")
            .orderBy("boughtDate", Query.Direction.DESCENDING)
        boughtRef.get()
            .addOnSuccessListener { boughts ->
                for (bought in boughts) {
                    val boughtObj = ModelFlightPurchased(
                        bought.get("flight").toString(),
                        bought.getTimestamp("boughtDate")!!,
                        bought.get("price").toString().toInt(),
                        bought.get("cardId").toString(),
                        bought.get("flightBaggageCap").toString(),
                        bought.get("flightCompany").toString(),
                        bought.get("flightDelay").toString(),
                        bought.get("flightDepartureCode").toString(),
                        bought.get("flightDestinationCode").toString(),
                        bought.getTimestamp("flightDepartureTime")!!,
                        bought.getTimestamp("flightDestinationTime")!!,
                    )
                    boughtList.add(boughtObj)
                }
            }.addOnCompleteListener {
                recyclerUserFlights.adapter = FlightBoughtAdapter(boughtList)
            }
    }
}