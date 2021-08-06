package com.example.flightappdemo.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.AirportFlights
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelAirport
import java.text.SimpleDateFormat
import java.util.*

class AirportsViewHolder(container: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(container.context).inflate(R.layout.airports_layout, container, false)
) {
    val tvAirportCode = itemView.findViewById<TextView>(R.id.tvAirportCode)
    val tvAirportName = itemView.findViewById<TextView>(R.id.tvAirportName)
    val tvAirportCountry = itemView.findViewById<TextView>(R.id.tvAirportCountry)
    val ivAirportMap = itemView.findViewById<ImageView>(R.id.ivAirportMap)
    val ivAirportTelephone = itemView.findViewById<ImageView>(R.id.ivAirportTelephone)

    fun bind(modelAirport: ModelAirport) {
        tvAirportCode.text = modelAirport.airportCode
        tvAirportName.text = modelAirport.airportName
        tvAirportCountry.text = modelAirport.airportCountry
        val latitude = modelAirport.latitude
        val longitude = modelAirport.longitude
        val airportTelephone = modelAirport.airportTelephone

        ivAirportMap.setOnClickListener {
            val uri = Uri.parse("http://maps.google.com/maps?saddr=$latitude,$longitude")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            it.context.startActivity(intent)
        }
        ivAirportTelephone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$airportTelephone")
            it.context.startActivity(intent)
        }

        this.itemView.setOnClickListener {
            val intent = Intent(it.context, AirportFlights::class.java)
            intent.putExtra("code", modelAirport.airportCode)
            it.context.startActivity(intent)
        }
    }
}