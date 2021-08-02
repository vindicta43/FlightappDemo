package com.example.flightappdemo.mainpages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelFlight
import com.example.flightappdemo.utils.FlightAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FlightsFragment : Fragment() {
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
        val flightRef = dbRef.collection("flights")
        flightRef.get()
            .addOnSuccessListener { flights ->
                for (flight in flights) {
                    val flightObj = ModelFlight(
                        flight.get("airport").toString(),
                        flight.get("departTime") as Timestamp,
                        flight.get("departure").toString(),
                        flight.get("destination").toString(),
                        flight.get("flightCode").toString(),
                        flight.get("price").toString()
                    )
                    // filling arrayList
                    flightsList.add(flightObj)
                }
                recyclerFlights.layoutManager = GridLayoutManager(view.context, 2)
                recyclerFlights.adapter = FlightAdapter(flightsList)
            }
        return view
    }
}