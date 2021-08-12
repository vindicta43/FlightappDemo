package com.example.flightappdemo.utils

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.models.ModelCard

class CardsAdapter(private val cardsList: ArrayList<ModelCard>): RecyclerView.Adapter<CardsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        return CardsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        holder.bind(cardsList[position], position)
    }

    override fun getItemCount(): Int {
        return cardsList.size
    }
}