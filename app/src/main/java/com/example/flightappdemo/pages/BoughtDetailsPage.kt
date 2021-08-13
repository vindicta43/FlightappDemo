package com.example.flightappdemo.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.flightappdemo.R

class BoughtDetailsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_details_page)

        // flight details view implementation
        val tvBoughtDateDetail = findViewById<TextView>(R.id.tvBoughtDateDetail)
        val tvBoughtFlightCompany = findViewById<TextView>(R.id.tvBoughtFlightCompany)
        val tvBoughtFlightDestinationTime = findViewById<TextView>(R.id.tvBoughtFlightDestinationTime)
        val tvBoughtFlightCodeDestination = findViewById<TextView>(R.id.tvBoughtFlightCodeDestination)
        val tvBoughtFlightCodeDeparture = findViewById<TextView>(R.id.tvBoughtFlightCodeDeparture)
        val tvBoughtFlightBaggageCap = findViewById<TextView>(R.id.tvBoughtFlightBaggageCap)
        val tvBoughtFlightDelay = findViewById<TextView>(R.id.tvBoughtFlightDelay)

        // card details view implementation
        val tvBoughtCardName = findViewById<TextView>(R.id.tvBoughtCardName)
        val tvBoughtCardNumber = findViewById<TextView>(R.id.tvBoughtCardNumber)
        val tvBoughtCardValidDate = findViewById<TextView>(R.id.tvBoughtCardValidDate)
        val tvBoughtCardCvv = findViewById<TextView>(R.id.tvBoughtCardCvv)
        val tvBoughtBalance = findViewById<TextView>(R.id.tvBoughtBalance)
    }
}