package com.example.flightappdemo.mainpages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelFlight
import com.example.flightappdemo.utils.FlightAdapter
import com.example.flightappdemo.utils.QrScanner
import com.google.firebase.Timestamp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FlightsFragment : Fragment() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flights, container, false)
        val recyclerFlights = view.findViewById<RecyclerView>(R.id.recyclerFlights)

        // flights list for recyclerView adapter
        val flightsList = arrayListOf<ModelFlight>()

        // getting flights from firebase
        val dbRef = Firebase.firestore
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "FlightsFragment")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Flights")
        }

        val flightRef = dbRef.collection("flights")
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
                        flight.get("id").toString(),
                        flight.get("price").toString().toInt()
                    )
                    // filling arrayList
                    flightsList.add(flightObj)
                }
                recyclerFlights.layoutManager = LinearLayoutManager(view.context)
                recyclerFlights.adapter = FlightAdapter(flightsList)
            }

        val ibFlightQR = view.findViewById<ImageButton>(R.id.ibFlightQR)
        ibFlightQR.setOnClickListener {
            QrScanner(this.activity)
        }
        return view
    }
}