package com.example.flightappdemo.utils

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.pages.ResultPage
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelAirport

class AirportsViewHolder(container: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(container.context).inflate(R.layout.airports_layout, container, false)
) {
    private val tvAirportCode = itemView.findViewById<TextView>(R.id.tvAirportCode)
    private val tvAirportName = itemView.findViewById<TextView>(R.id.tvAirportName)
    private val tvAirportCountry = itemView.findViewById<TextView>(R.id.tvAirportCountry)
    private val ivAirportMap = itemView.findViewById<ImageView>(R.id.ivAirportMap)
    private val ivAirportTelephone = itemView.findViewById<ImageView>(R.id.ivAirportTelephone)

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
            val intent = Intent(it.context, ResultPage::class.java)
            intent.putExtra("id", modelAirport.id)
            it.context.startActivity(intent)
        }
    }
}