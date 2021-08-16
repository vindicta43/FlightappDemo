package com.example.flightappdemo.utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.models.ModelFlightPurchased

class FlightBoughtAdapter(
    private val boughtList: ArrayList<ModelFlightPurchased>,
) : RecyclerView.Adapter<FlightBoughtViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightBoughtViewHolder {
        return FlightBoughtViewHolder(parent)
    }

    override fun onBindViewHolder(holder: FlightBoughtViewHolder, position: Int) {
        holder.bind(boughtList[position])
    }

    override fun getItemCount(): Int {
        return boughtList.size
    }
}