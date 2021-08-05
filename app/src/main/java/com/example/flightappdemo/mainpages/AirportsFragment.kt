package com.example.flightappdemo.mainpages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelAirport
import com.example.flightappdemo.utils.AirportsAdapter
import com.google.firebase.Timestamp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AirportsFragment : Fragment() {
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
                    )
                    // filling arrayList
                    airportsList.add(airportObj)
                }
                recyclerAirports.layoutManager = LinearLayoutManager(view.context)
                recyclerAirports.adapter = AirportsAdapter(airportsList)
            }

        return view
    }
}