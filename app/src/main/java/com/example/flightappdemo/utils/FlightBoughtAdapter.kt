package com.example.flightappdemo.utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.models.ModelFlight
import com.google.firebase.Timestamp

class FlightBoughtAdapter(
    private val flightsList: ArrayList<ModelFlight>,
    private val boughtDate: ArrayList<Timestamp>
) : RecyclerView.Adapter<FlightBoughtViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightBoughtViewHolder {
        return FlightBoughtViewHolder(parent)
    }

    override fun onBindViewHolder(holder: FlightBoughtViewHolder, position: Int) {
        holder.bind(flightsList[position], boughtDate, position)
    }

    override fun getItemCount(): Int {
        return flightsList.size
    }
}