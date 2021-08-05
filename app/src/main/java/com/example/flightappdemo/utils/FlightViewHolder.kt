package com.example.flightappdemo.utils

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelFlight
import java.util.logging.Handler

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
    val ivMap = itemView.findViewById<ImageView>(R.id.ivMap)
    val ivTelephone = itemView.findViewById<ImageView>(R.id.ivTelephone)

    fun bind(modelFlight: ModelFlight) {
        tvFlightCode.text = modelFlight.flightCode
        tvAirport.text = modelFlight.airport
        tvDeparture.text = modelFlight.departure
        tvDestination.text = modelFlight.destination

        ivMap.setOnClickListener {
            val uri = Uri.parse("https://www.google.com/maps/search/central+park/@40.7744986,-73.9779284,15z/data=!3m1!4b1")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            it.context.startActivity(intent)
        }
        ivTelephone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${tvFlightCode.text}")
            it.context.startActivity(intent)
        }
    }
}