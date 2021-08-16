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
import com.example.flightappdemo.models.ModelAirport
import com.example.flightappdemo.utils.AirportsAdapter
import com.example.flightappdemo.utils.QrScanner
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AirportsFragment : Fragment() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_airports, container, false)
        val recyclerAirports = view.findViewById<RecyclerView>(R.id.recyclerAirports)

        // airports for recyclerView adapter
        val airportsList = arrayListOf<ModelAirport>()

        // getting airports from firebase
        val dbRef = Firebase.firestore
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "AirportsFragment")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Airports")
        }

        val airportsRef = dbRef.collection("airports")
        airportsRef.get()
            .addOnSuccessListener { airports ->
                for (airport in airports) {
                    val airportObj = ModelAirport(
                        airport.get("airportCode").toString(),
                        airport.get("airportCountry").toString(),
                        airport.get("airportName").toString(),
                        airport.get("airportTelephone").toString(),
                        airport.get("latitude").toString(),
                        airport.get("longitude").toString(),
                        airport.get("id").toString()
                    )
                    // filling arrayList
                    airportsList.add(airportObj)
                }
                recyclerAirports.layoutManager = LinearLayoutManager(view.context)
                recyclerAirports.adapter = AirportsAdapter(airportsList)
            }

        val ibAirportQR = view.findViewById<ImageButton>(R.id.ibAirportQR)
        ibAirportQR.setOnClickListener {
            QrScanner(this.activity)
        }
        return view
    }
}