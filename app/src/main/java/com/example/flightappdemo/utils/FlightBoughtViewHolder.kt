package com.example.flightappdemo.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelFlightPurchased
import com.example.flightappdemo.pages.PurchaseDetailsPage
import java.text.SimpleDateFormat

class FlightBoughtViewHolder(container: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(container.context).inflate(
            R.layout.flight_bought_layout,
            container,
            false
        )
    ) {
    private val tvBoughtFlightCompany = itemView.findViewById<TextView>(R.id.tvBoughtFlightCompany)
    private val tvBoughtFlightDepartureTime = itemView.findViewById<TextView>(R.id.tvBoughtFlightDepartureTime)
    private val tvBoughtFlightTime = itemView.findViewById<TextView>(R.id.tvBoughtFlightTime)
    private val tvBoughtFlightDestinationTime = itemView.findViewById<TextView>(R.id.tvBoughtFlightDestinationTime)
    private val tvBoughtFlightCodeDestination = itemView.findViewById<TextView>(R.id.tvBoughtFlightCodeDestination)
    private val tvBoughtFlightCodeDeparture = itemView.findViewById<TextView>(R.id.tvBoughtFlightCodeDeparture)
    private val tvBoughtFlightBaggageCap = itemView.findViewById<TextView>(R.id.tvBoughtFlightBaggageCap)
    private val tvBoughtFlightDelay = itemView.findViewById<TextView>(R.id.tvBoughtFlightDelay)
    private val tvBoughtDate = itemView.findViewById<TextView>(R.id.tvBoughtDate)
    private val tvBoughtPrice = itemView.findViewById<TextView>(R.id.tvBoughtPrice)

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun bind(modelFlight: ModelFlightPurchased) {
        val departDate = modelFlight.flightDepartureTime.toDate()
        val destDate = modelFlight.flightDestinationTime.toDate()

        // difference between departure and destination
        val flightTime = destDate.time - departDate.time
        val diffHour = (flightTime / 3600000) % 24
        val diffMin = (flightTime / 60000) % 60

        // formatting to string and 24h format
        // https://www.geeksforgeeks.org/find-the-duration-of-difference-between-two-dates-in-java/
        val dateFormat = SimpleDateFormat("kk:mm")
        val departTime = dateFormat.format(departDate).toString()
        val destTime = dateFormat.format(destDate).toString()

        tvBoughtFlightCompany.text = modelFlight.flightCompany
        tvBoughtFlightDepartureTime.text = departTime
        tvBoughtFlightTime.text = "${diffHour}h ${diffMin}m"
        tvBoughtFlightDestinationTime.text = destTime
        tvBoughtFlightCodeDestination.text = modelFlight.flightDepartureCode
        tvBoughtFlightCodeDeparture.text = modelFlight.flightDestinationCode
        tvBoughtFlightBaggageCap.text = modelFlight.flightBaggageCap
        tvBoughtFlightDelay.text = modelFlight.flightDelay
        tvBoughtDate.text = modelFlight.boughtDate.toDate().toString()
        tvBoughtPrice.text = modelFlight.price.toString()

        this.itemView.setOnClickListener {
            val intent = Intent(it.context, PurchaseDetailsPage::class.java)
            intent.putExtra("date", modelFlight.boughtDate.toDate().toString())
            intent.putExtra("flightId", modelFlight.flight)
            //intent.putExtra("cardId", modelFlight.)
            it.context.startActivity(intent)
        }
    }
}