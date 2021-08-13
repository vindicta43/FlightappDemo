package com.example.flightappdemo.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.flightappdemo.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class PurchaseDetailsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_details_page)

        // flight details view implementation
        val tvPurchaseDate = findViewById<TextView>(R.id.tvPurchaseDate)
        val tvPurchaseFlightCompany = findViewById<TextView>(R.id.tvPurchaseFlightCompany)
        val tvPurchaseFlightDepartureTime = findViewById<TextView>(R.id.tvPurchaseFlightDepartureTime)
        val tvPurchaseFlightTime = findViewById<TextView>(R.id.tvPurchaseFlightTime)
        val tvPurchaseFlightDestinationTime = findViewById<TextView>(R.id.tvPurchaseFlightDestinationTime)
        val tvPurchaseFlightCodeDestination = findViewById<TextView>(R.id.tvPurchaseFlightCodeDestination)
        val tvPurchaseFlightCodeDeparture = findViewById<TextView>(R.id.tvPurchaseFlightCodeDeparture)
        val tvPurchaseFlightBaggageCap = findViewById<TextView>(R.id.tvPurchaseFlightBaggageCap)
        val tvPurchaseFlightDelay = findViewById<TextView>(R.id.tvPurchaseFlightDelay)
        val tvPurchaseFlightPrice = findViewById<TextView>(R.id.tvPurchaseFlightPrice)

        // card details view implementation
        val tvBoughtCardName = findViewById<TextView>(R.id.tvPurchaseCardName)
        val tvBoughtCardNumber = findViewById<TextView>(R.id.tvPurchaseCardNumber)
        val tvBoughtCardValidDate = findViewById<TextView>(R.id.tvPurchaseCardValidDate)
        val tvBoughtCardCvv = findViewById<TextView>(R.id.tvPurchaseCardCvv)
        val tvBoughtBalance = findViewById<TextView>(R.id.tvPurchaseBalance)

        val date = intent.getStringExtra("date")
        val id = intent.getStringExtra("flightId")
        val dbRef = FirebaseFirestore.getInstance()
        val auth = Firebase.auth

        // fill flight details
        val flightRef = dbRef.collection("flights").whereEqualTo("id", id)
        flightRef.get()
            .addOnSuccessListener { flights ->
                for (flight in flights) {
                    val dest = flight.get("flightDestinationTime")as Timestamp
                    val depart = flight.get("flightDepartureTime")as Timestamp
                    tvPurchaseDate.text = date
                    tvPurchaseFlightCompany.text = flight.get("flightCompany").toString()
                    tvPurchaseFlightDepartureTime.text = convertTime(flight.get("flightDepartureTime")as Timestamp)
                    tvPurchaseFlightTime.text = calculateDiff(depart, dest)
                    tvPurchaseFlightDestinationTime.text = convertTime(flight.get("flightDestinationTime")as Timestamp)
                    tvPurchaseFlightCodeDestination.text = flight.get("flightDestinationCode").toString()
                    tvPurchaseFlightCodeDeparture.text = flight.get("flightDepartureCode").toString()
                    tvPurchaseFlightBaggageCap.text = flight.get("flightBaggageCap").toString()
                    tvPurchaseFlightDelay.text = flight.get("flightDelay").toString()
                    tvPurchaseFlightPrice.text = flight.get("price").toString()
                }
            }

        // get card id from flights
        var cardId = ""
        val cardIdRef = dbRef.collection("users/${auth.uid}/flights").whereEqualTo("flight", id)
        cardIdRef.get()
            .addOnSuccessListener { cards ->
                for (card in cards) {
                    cardId = card.get("cardId").toString()
                }
            }
            .addOnCompleteListener {
                // fill card details
                val cardRef = dbRef.collection("users/${auth.uid}/cards").whereEqualTo("id", cardId)
                cardRef.get()
                    .addOnSuccessListener { cards ->
                        for (card in cards) {
                            tvBoughtCardName.text = card.get("cardName").toString()
                            tvBoughtCardNumber.text = card.get("cardNumber").toString()
                            tvBoughtCardValidDate.text = card.get("cardValid").toString()
                            tvBoughtCardCvv.text = card.get("cardCvv").toString()
                            tvBoughtBalance.text = card.get("balance").toString()
                        }
                    }
            }
    }

    private fun convertTime(timestamp: Timestamp): CharSequence? {
        var convertionTime = timestamp.toDate()

        // formatting to string and 24h format
        // https://www.geeksforgeeks.org/find-the-duration-of-difference-between-two-dates-in-java/
        val dateFormat = SimpleDateFormat("kk:mm")
        return dateFormat.format(convertionTime).toString()
    }

    private fun calculateDiff(depart: Timestamp, dest: Timestamp): CharSequence? {
        val departDate = depart.toDate()
        val destDate = dest.toDate()

        // difference between departure and destination
        val flightTime = destDate.time - departDate.time
        val diffHour = (flightTime / 3600000) % 24
        val diffMin = (flightTime / 60000) % 60
        return "${diffHour}h ${diffMin}m"
    }
}