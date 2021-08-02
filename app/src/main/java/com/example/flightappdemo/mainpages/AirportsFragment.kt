package com.example.flightappdemo.mainpages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelAirport
import com.example.flightappdemo.utils.AirportsAdapter
import com.google.firebase.Timestamp
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
                        airport.get("airport").toString(),
                        airport.get("departure") as Timestamp,
                        airport.get("flightTime") as Timestamp,
                        airport.get("destination") as Timestamp,
                        airport.get("flightCodeDepart").toString(),
                        airport.get("flightCodeDest").toString(),
                        airport.get("kilogram").toString(),
                        airport.get("delay").toString(),

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