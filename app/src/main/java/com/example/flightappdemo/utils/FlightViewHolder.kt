package com.example.flightappdemo.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelFlight

class FlightViewHolder(container: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(container.context).inflate(
            R.layout.flight_layout,
            container,
            false
        )
    ) {
        val tvFlightCode = itemView.findViewById<TextView>(R.id.tvFlightCode)
        val tvAirport = itemView.findViewById<TextView>(R.id.tvAirport)
        val tvDeparture = itemView.findViewById<TextView>(R.id.tvDeparture)
        val tvDestination = itemView.findViewById<TextView>(R.id.tvDestination)

    fun bind(modelFlight: ModelFlight) {
        tvFlightCode.text = modelFlight.flightCode
        tvAirport.text = modelFlight.airport
        tvDeparture.text = modelFlight.departure
        tvDestination.text = modelFlight.destination
    }
}