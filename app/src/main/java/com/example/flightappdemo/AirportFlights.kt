package com.example.flightappdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.models.ModelFlight
import com.example.flightappdemo.utils.FlightAdapter
import com.google.firebase.Timestamp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AirportFlights : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airport_flights)

        val recyclerAirportFlights = findViewById<RecyclerView>(R.id.recyclerAirportFlights)

        // flights list for recyclerView adapter
        val flightsList = arrayListOf<ModelFlight>()

        // getting flights from firebase
        val dbRef = Firebase.firestore
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        val code = intent.getStringExtra("code")
        val tvAirportFlights = findViewById<TextView>(R.id.tvAirportFlights)
        tvAirportFlights.text = "Flights of $code"

        val flightRef = dbRef.collection("flights").whereEqualTo("flightDepartureCode", code)
        flightRef.get()
            .addOnSuccessListener { flights ->
                for (flight in flights) {
                    val flightObj = ModelFlight(
                        flight.get("flightBaggageCap").toString(),
                        flight.get("flightCompany").toString(),
                        flight.get("flightDelay").toString(),
                        flight.get("flightDepartureCode").toString(),
                        flight.get("flightDestinationCode").toString(),
                        flight.get("flightDepartureTime")as Timestamp,
                        flight.get("flightDestinationTime")as Timestamp,
                    )
                    // filling arrayList
                    flightsList.add(flightObj)
                }
                recyclerAirportFlights.layoutManager = LinearLayoutManager(this)
                recyclerAirportFlights.adapter = FlightAdapter(flightsList)
            }
    }
}