package com.example.flightappdemo.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelFlight
import com.example.flightappdemo.models.ModelFlightPurchased
import com.example.flightappdemo.utils.FlightBoughtAdapter
import com.google.firebase.Timestamp
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
        val flightList = arrayListOf<ModelFlight>()

        val dbRef = Firebase.firestore
        val auth = Firebase.auth

        val boughtRef = dbRef.collection("users/${auth.uid}/flights")
            .orderBy("boughtDate", Query.Direction.DESCENDING)
        boughtRef.get()
            .addOnSuccessListener { boughts ->
                for (bought in boughts) {
                    boughtList.add(
                        ModelFlightPurchased(
                            bought.get("flight").toString(),
                            bought.get("boughtDate") as Timestamp,
                            bought.get("price").toString().toInt(),
                            bought.get("cardId").toString()
                        )
                    )
                }
            }
            .addOnCompleteListener {
                for (bought in boughtList) {
                    val flightRef = dbRef.collection("flights").whereEqualTo("id", bought.flight)
                    flightRef.get()
                        .addOnSuccessListener { flights ->
                            for (flight in flights) {
                                flightList.add(
                                    ModelFlight(
                                        flight.get("flightBaggageCap").toString(),
                                        flight.get("flightCompany").toString(),
                                        flight.get("flightDelay").toString(),
                                        flight.get("flightDepartureCode").toString(),
                                        flight.get("flightDestinationCode").toString(),
                                        flight.get("flightDepartureTime") as Timestamp,
                                        flight.get("flightDestinationTime") as Timestamp,
                                        flight.get("id").toString(),
                                        flight.get("price").toString().toInt()
                                    )
                                )
                            }
                        }
                        .addOnCompleteListener {
                            val boughtDates = arrayListOf<Timestamp>()
                            boughtList.forEach {
                                boughtDates.add(it.boughtDate)
                            }
                            recyclerUserFlights.adapter =
                                FlightBoughtAdapter(flightList, boughtDates)
                        }
                }
            }
    }
}