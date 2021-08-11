package com.example.flightappdemo.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.ResultPage
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
    private val tvFlightCompany = itemView.findViewById<TextView>(R.id.tvFlightCompany)
    private val tvFlightDepartureTime = itemView.findViewById<TextView>(R.id.tvFlightDepartureTime)
    private val tvFlightTime = itemView.findViewById<TextView>(R.id.tvFlightTime)
    private val tvFlightDestinationTime = itemView.findViewById<TextView>(R.id.tvFlightDestinationTime)
    private val tvFlightCodeDestination = itemView.findViewById<TextView>(R.id.tvFlightCodeDestination)
    private val tvFlightCodeDeparture = itemView.findViewById<TextView>(R.id.tvFlightCodeDeparture)
    private val tvFlightBaggageCap = itemView.findViewById<TextView>(R.id.tvFlightBaggageCap)
    private val tvFlightDelay = itemView.findViewById<TextView>(R.id.tvFlightDelay)


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
            val intent = Intent(it.context, ResultPage::class.java)
            intent.putExtra("id", modelFlight.id)
            it.context.startActivity(intent)
        }
    }
}