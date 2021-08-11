package com.example.flightappdemo.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelFlight
import com.example.flightappdemo.utils.FlightAdapter
import com.google.firebase.Timestamp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResultPage : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    //private val recyclerResult = findViewById<RecyclerView>(R.id.recyclerResult)
    private lateinit var recyclerResult: RecyclerView


    // flights list for recyclerView adapter
    private val flightsList = arrayListOf<ModelFlight>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_page)

        // getting results from firebase
        val dbRef = Firebase.firestore
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        val itemID = intent.getStringExtra("id")
        val resultsRef = dbRef.collection("flights").whereEqualTo("id", itemID)
        resultsRef.get()
            .addOnSuccessListener { results ->
                // if id is an airport id
                if (results.isEmpty) {
                    listAirportFlights(itemID)
                }
                // if id is a flight it
                else {
                    listFlight(itemID)
                }
            }
    }

    private fun listAirportFlights(id: String?) {
        val dbRef = Firebase.firestore
        var airportRef = dbRef.collection("airports").whereEqualTo("id", id)
        airportRef.get()
            .addOnSuccessListener { airport ->
                // after getting airportCode we will make another query
                val departureCode = airport.documents[0].get("airportCode")
                airportRef =
                    dbRef.collection("flights").whereEqualTo("flightDepartureCode", departureCode)
                airportRef.get()
                    .addOnSuccessListener { flights ->
                        for (flight in flights) {
                            val flightObj = ModelFlight(
                                flight.get("flightBaggageCap").toString(),
                                flight.get("flightCompany").toString(),
                                flight.get("flightDelay").toString(),
                                flight.get("flightDepartureCode").toString(),
                                flight.get("flightDestinationCode").toString(),
                                flight.get("flightDepartureTime") as Timestamp,
                                flight.get("flightDestinationTime") as Timestamp,
                                flight.get("id").toString()
                            )
                            // filling arrayList
                            flightsList.add(flightObj)
                        }
                        val tvAirportFlights = findViewById<TextView>(R.id.tvResultPage)
                        tvAirportFlights.text = "${flightsList[0].flightDepartureCode} Flights"
                        recyclerResult = findViewById(R.id.recyclerResult)
                        recyclerResult.layoutManager = LinearLayoutManager(this)
                        recyclerResult.adapter = FlightAdapter(flightsList)
                    }
            }
    }

    private fun listFlight(id: String?) {
        val dbRef = Firebase.firestore
        val flightsRef = dbRef.collection("flights").whereEqualTo("id", id)
        flightsRef.get()
            .addOnSuccessListener { flights ->
                for (flight in flights) {
                    val flightObj = ModelFlight(
                        flight.get("flightBaggageCap").toString(),
                        flight.get("flightCompany").toString(),
                        flight.get("flightDelay").toString(),
                        flight.get("flightDepartureCode").toString(),
                        flight.get("flightDestinationCode").toString(),
                        flight.get("flightDepartureTime") as Timestamp,
                        flight.get("flightDestinationTime") as Timestamp,
                        flight.get("id").toString()
                    )
                    // filling arrayList
                    flightsList.add(flightObj)
                }
                val tvAirportFlights = findViewById<TextView>(R.id.tvResultPage)
                tvAirportFlights.text = "Flight of ${flightsList[0].flightCompany}"
                recyclerResult = findViewById(R.id.recyclerResult)
                recyclerResult.layoutManager = LinearLayoutManager(this)
                recyclerResult.adapter = FlightAdapter(flightsList)
            }
    }
}