package com.example.flightappdemo.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelFlight
import java.text.SimpleDateFormat

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
        val departDate = modelFlight.flightDepartureTime.toDate()
        val destDate = modelFlight.flightDestinationTime.toDate()

        // difference between departure and destination
        val flightTime = destDate.time - departDate.time
        val diffHour = (flightTime / 3600000) % 24
        val diffMin = (flightTime / 60000) % 60

        // formatting to string and 24h format
        // https://www.geeksforgeeks.org/find-the-duration-of-difference-between-two-dates-in-java/
        val dateFormat = SimpleDateFormat("kk:mm")
        val departTime = dateFormat.format(departDate).toString()
        val destTime = dateFormat.format(destDate).toString()

        tvFlightCompany.text = modelFlight.flightCompany
        tvFlightDepartureTime.text = departTime
        tvFlightTime.text = "${diffHour}h ${diffMin}m"
        tvFlightDestinationTime.text = destTime
        tvFlightCodeDeparture.text = modelFlight.flightDepartureCode
        tvFlightCodeDestination.text = modelFlight.flightDestinationCode
        tvFlightBaggageCap.text = modelFlight.flightBaggageCap
        tvFlightDelay.text = modelFlight.flightDelay

        this.itemView.setOnClickListener {
            Toast.makeText(
                it.context,
                "${modelFlight.flightCompany} ${modelFlight.flightDepartureCode} to ${modelFlight.flightDestinationCode}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}