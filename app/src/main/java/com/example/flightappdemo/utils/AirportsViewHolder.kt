package com.example.flightappdemo.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelAirport
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class AirportsViewHolder(container: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(container.context).inflate(R.layout.airports_layout, container, false)
) {
    val tvCardAirport = itemView.findViewById<TextView>(R.id.tvCardAirport)
    val tvCardDeparture = itemView.findViewById<TextView>(R.id.tvCardDeparture)
    val tvCardFlightTime = itemView.findViewById<TextView>(R.id.tvCardFlightTime)
    val tvCardDestination = itemView.findViewById<TextView>(R.id.tvCardDestination)
    val tvCardFlightCodeDeparture = itemView.findViewById<TextView>(R.id.tvCardFlightCodeDeparture)
    val tvCardFlightCodeDestination =
        itemView.findViewById<TextView>(R.id.tvCardFlightCodeDestination)
    val tvCardKilogram = itemView.findViewById<TextView>(R.id.tvCardKilogram)
    val tvCardDelay = itemView.findViewById<TextView>(R.id.tvCardDelay)

    fun bind(modelAirport: ModelAirport) {
        val formatter = SimpleDateFormat("HH:mm")
        val depart = formatter.format(modelAirport.departure.toDate())
        val dest = formatter.format(modelAirport.destination.toDate())
        val flightTime = formatter.format(modelAirport.destination.toDate())

        tvCardAirport.text = modelAirport.airport
        tvCardDeparture.text = depart
        tvCardFlightTime.text = flightTime
        tvCardDestination.text = dest
        tvCardFlightCodeDeparture.text = modelAirport.flightCodeDepart
        tvCardFlightCodeDestination.text = modelAirport.flightCodeDest
        tvCardKilogram.text = modelAirport.kilogram
        tvCardDelay.text = modelAirport.delay
    }
}