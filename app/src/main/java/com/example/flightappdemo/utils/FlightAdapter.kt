package com.example.flightappdemo.utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.models.ModelFlight

class FlightAdapter(val flightsList: ArrayList<ModelFlight>): RecyclerView.Adapter<FlightViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        return FlightViewHolder(parent)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        holder.bind(flightsList[position])
    }

    override fun getItemCount(): Int {
        return flightsList.size
    }
}