package com.example.flightappdemo.utils

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelCard
import com.example.flightappdemo.pages.EditCardPage
import com.example.flightappdemo.pages.PurchasePage

class CardsViewHolder(container: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(container.context).inflate(
    R.layout.single_card_layout, container, false)) {

    private val tvCardName = itemView.findViewById<TextView>(R.id.tvCardName)
    private val tvCardNumber = itemView.findViewById<TextView>(R.id.tvCardNumber)
    private val tvCardValidDate = itemView.findViewById<TextView>(R.id.tvCardValidDate)
    private val tvCardCvv = itemView.findViewById<TextView>(R.id.tvCardCvv)
    private val singleCardItem = itemView.findViewById<CardView>(R.id.singleCardItem)
    private val tvBalance = itemView.findViewById<TextView>(R.id.tvBalance)

    fun bind(modelCard: ModelCard, position: Int) {
        tvCardName.text = modelCard.cardName
        tvCardNumber.text = modelCard.cardNumber
        tvCardValidDate.text = modelCard.cardValidDate
        tvCardCvv.text = modelCard.cardCvv
        tvBalance.text = modelCard.balance.toString()

        // colorful card backgrounds
        when(position % 3) {
            0 -> singleCardItem.setCardBackgroundColor(Color.rgb(100, 160, 255))
            1 -> singleCardItem.setCardBackgroundColor(Color.rgb(75, 135, 255))
            2 -> singleCardItem.setCardBackgroundColor(Color.rgb(50, 110, 255))
        }

        this.itemView.setOnClickListener {
            val intent = Intent(it.context, EditCardPage::class.java)
            intent.putExtra("cardId", modelCard.id)
            it.context.startActivity(intent)
        }
    }
}