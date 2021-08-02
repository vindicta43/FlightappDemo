package com.example.flightappdemo.utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.models.ModelAirport

class AirportsAdapter(val airportList: ArrayList<ModelAirport>): RecyclerView.Adapter<AirportsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportsViewHolder {
        return AirportsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: AirportsViewHolder, position: Int) {
        holder.bind(airportList[position])
    }

    override fun getItemCount(): Int {
        return airportList.size
    }
}