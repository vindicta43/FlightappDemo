package com.example.flightappdemo.pages

import android.annotation.SuppressLint
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
        val tvPurchaseDetailDate = findViewById<TextView>(R.id.tvPurchaseDetailDate)
        val tvPurchaseDetailFlightCompany = findViewById<TextView>(R.id.tvPurchaseDetailFlightCompany)
        val tvPurchaseDetailFlightDepartureTime = findViewById<TextView>(R.id.tvPurchaseDetailFlightDepartureTime)
        val tvPurchaseDetailFlightTime = findViewById<TextView>(R.id.tvPurchaseDetailFlightTime)
        val tvPurchaseDetailFlightDestinationTime = findViewById<TextView>(R.id.tvPurchaseDetailFlightDestinationTime)
        val tvPurchaseDetailFlightCodeDestination = findViewById<TextView>(R.id.tvPurchaseDetailFlightCodeDestination)
        val tvPurchaseDetailFlightCodeDeparture = findViewById<TextView>(R.id.tvPurchaseDetailFlightCodeDeparture)
        val tvPurchaseDetailFlightBaggageCap = findViewById<TextView>(R.id.tvPurchaseDetailFlightBaggageCap)
        val tvPurchaseDetailFlightDelay = findViewById<TextView>(R.id.tvPurchaseDetailFlightDelay)
        val tvPurchaseDetailFlightPrice = findViewById<TextView>(R.id.tvPurchaseDetailFlightPrice)

        // card details view implementation
        val tvPurchaseDetailCardName = findViewById<TextView>(R.id.tvPurchaseDetailCardName)
        val tvPurchaseDetailCardNumber = findViewById<TextView>(R.id.tvPurchaseDetailCardNumber)
        val tvPurchaseDetailCardValidDate = findViewById<TextView>(R.id.tvPurchaseDetailCardValidDate)
        val tvPurchaseDetailCardCvv = findViewById<TextView>(R.id.tvPurchaseDetailCardCvv)
        val tvPurchaseDetailBalance = findViewById<TextView>(R.id.tvPurchaseDetailBalance)

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
                    tvPurchaseDetailDate.text = date
                    tvPurchaseDetailFlightCompany.text = flight.get("flightCompany").toString()
                    tvPurchaseDetailFlightDepartureTime.text = convertTime(flight.get("flightDepartureTime")as Timestamp)
                    tvPurchaseDetailFlightTime.text = calculateDiff(depart, dest)
                    tvPurchaseDetailFlightDestinationTime.text = convertTime(flight.get("flightDestinationTime")as Timestamp)
                    tvPurchaseDetailFlightCodeDestination.text = flight.get("flightDestinationCode").toString()
                    tvPurchaseDetailFlightCodeDeparture.text = flight.get("flightDepartureCode").toString()
                    tvPurchaseDetailFlightBaggageCap.text = flight.get("flightBaggageCap").toString()
                    tvPurchaseDetailFlightDelay.text = flight.get("flightDelay").toString()
                    tvPurchaseDetailFlightPrice.text = flight.get("price").toString()
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
                            tvPurchaseDetailCardName.text = card.get("cardName").toString()
                            tvPurchaseDetailCardNumber.text = card.get("cardNumber").toString()
                            tvPurchaseDetailCardValidDate.text = card.get("cardValidDate").toString()
                            tvPurchaseDetailCardCvv.text = card.get("cardCvv").toString()
                            tvPurchaseDetailBalance.text = card.get("balance").toString()
                        }
                    }
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertTime(timestamp: Timestamp): CharSequence {
        val convertionTime = timestamp.toDate()

        // formatting to string and 24h format
        // https://www.geeksforgeeks.org/find-the-duration-of-difference-between-two-dates-in-java/
        val dateFormat = SimpleDateFormat("kk:mm")
        return dateFormat.format(convertionTime).toString()
    }

    private fun calculateDiff(depart: Timestamp, dest: Timestamp): CharSequence {
        val departDate = depart.toDate()
        val destDate = dest.toDate()

        // difference between departure and destination
        val flightTime = destDate.time - departDate.time
        val diffHour = (flightTime / 3600000) % 24
        val diffMin = (flightTime / 60000) % 60
        return "${diffHour}h ${diffMin}m"
    }
}