package com.example.flightappdemo.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelFlight
import java.text.SimpleDateFormat
import java.util.*

class FlightViewHolder(container: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(container.context).inflate(
            R.layout.flight_layout,
            container,
            false
        )
    ) {
    val tvFlightCompany = itemView.findViewById<TextView>(R.id.tvFlightCompany)
    val tvFlightDepartureTime = itemView.findViewById<TextView>(R.id.tvFlightDepartureTime)
    val tvFlightTime = itemView.findViewById<TextView>(R.id.tvFlightTime)
    val tvFlightDestinationTime = itemView.findViewById<TextView>(R.id.tvFlightDestinationTime)
    val tvFlightCodeDestination = itemView.findViewById<TextView>(R.id.tvFlightCodeDestination)
    val tvFlightCodeDeparture = itemView.findViewById<TextView>(R.id.tvFlightCodeDeparture)
    val tvFlightBaggageCap = itemView.findViewById<TextView>(R.id.tvFlightBaggageCap)
    val tvFlightDelay = itemView.findViewById<TextView>(R.id.tvFlightDelay)


    fun bind(modelFlight: ModelFlight) {
        val millisecondsDepart = modelFlight.flightDepartureTime.seconds * 1000 + modelFlight.flightDepartureTime.nanoseconds / 1000000
        val millisecondsDest = modelFlight.flightDestinationTime.seconds * 1000 + modelFlight.flightDestinationTime.nanoseconds / 1000000
        val millisecondsFlight = millisecondsDest - millisecondsDepart

        val dateFormat = SimpleDateFormat("kk:mm")
        val newDateDepart = Date(millisecondsDepart)
        val newDateDest = Date(millisecondsDest)
        val newDateFlight = Date(millisecondsFlight)

        val departTime = dateFormat.format(newDateDepart).toString()
        val destTime = dateFormat.format(newDateDest).toString()
        val flight = dateFormat.format(newDateFlight).toString()

        tvFlightCompany.text = modelFlight.flightCompany
        tvFlightDepartureTime.text = departTime
        tvFlightTime.text = flight
        tvFlightDestinationTime.text = destTime
        tvFlightCodeDeparture.text = modelFlight.flightDepartureCode
        tvFlightCodeDestination.text = modelFlight.flightDestinationCode
        tvFlightBaggageCap.text = modelFlight.flightBaggageCap
        tvFlightDelay.text = modelFlight.flightDelay
    }
}