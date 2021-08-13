package com.example.flightappdemo.utils

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelCard

class CardsViewHolder(container: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(container.context).inflate(
    R.layout.single_card_layout, container, false)) {

    val tvCardName = itemView.findViewById<TextView>(R.id.tvCardName)
    val tvCardNumber = itemView.findViewById<TextView>(R.id.tvCardNumber)
    val tvCardValidDate = itemView.findViewById<TextView>(R.id.tvCardValidDate)
    val tvCardCvv = itemView.findViewById<TextView>(R.id.tvCardCvv)
    val singleCardItem = itemView.findViewById<CardView>(R.id.singleCardItem)
    val tvBalance = itemView.findViewById<TextView>(R.id.tvBalance)

    fun bind(modelCard: ModelCard, position: Int) {
        tvCardName.text = modelCard.cardName
        tvCardNumber.text = modelCard.cardNumber
        tvCardValidDate.text = modelCard.validDate
        tvCardCvv.text = modelCard.cvv
        tvBalance.text = modelCard.balance.toString()

        val colorCode = position % 3
        // colorful card backgrounds
        when(colorCode) {
            0 -> singleCardItem.setCardBackgroundColor(Color.rgb(100, 160, 255))
            1 -> singleCardItem.setCardBackgroundColor(Color.rgb(75, 135, 255))
            2 -> singleCardItem.setCardBackgroundColor(Color.rgb(50, 110, 255))
        }
    }
}