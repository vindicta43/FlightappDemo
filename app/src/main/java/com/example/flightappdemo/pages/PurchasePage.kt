package com.example.flightappdemo.pages

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelCard
import com.example.flightappdemo.models.ModelFlight
import com.example.flightappdemo.models.ModelFlightPurchased
import com.example.flightappdemo.utils.AlertBuilder
import com.google.firebase.Timestamp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class PurchasePage : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var cardsList: ArrayList<ModelCard>
    private var cardIndex: Int = 0

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_page)

        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "PurchasePage")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Purchase")
        }

        val dbRef = FirebaseFirestore.getInstance()
        val auth = Firebase.auth
        val spCards = findViewById<Spinner>(R.id.spCards)

        // getting all user card
        cardsList = arrayListOf()
        val cardsRef = dbRef.collection("users/${auth.uid}/cards")
        cardsRef.get()
            .addOnSuccessListener { cards ->
                for (card in cards) {
                    val cardObj = ModelCard(
                        card.get("cardName").toString(),
                        card.get("cardNumber").toString(),
                        card.get("cardValidDate").toString(),
                        card.get("cardCvv").toString(),
                        card.get("id").toString(),
                        card.get("balance").toString().toInt()
                    )
                    cardsList.add(cardObj)
                }
            }
            .addOnCompleteListener {
                // filling spinner with card names
                val cardNamesList = arrayOfNulls<String>(cardsList.size)
                for (i in 0 until cardsList.size) {
                    cardNamesList[i] = cardsList[i].cardName
                }

                val arrayAdapter =
                    ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, cardNamesList)
                spCards.adapter = arrayAdapter
                spCards.setSelection(0)
                spCards.onItemSelectedListener = this
            }

        val flightId = intent.getStringExtra("flightId")
        // get selected flight
        val flightsList = arrayListOf<ModelFlight>()
        val flightRef = dbRef.collection("flights").whereEqualTo("id", flightId)
        flightRef.get()
            .addOnSuccessListener { flights ->
                for (flight in flights) {
                    val flightObj = ModelFlight(
                        flight.get("flightBaggageCap").toString(),
                        flight.get("flightCompany").toString(),
                        flight.get("flightDelay").toString(),
                        flight.get("flightDepartureCode").toString(),
                        flight.get("flightDestinationCode").toString(),
                        flight.get("flightDepartureTime") as Timestamp,
                        flight.get("flightDestinationTime") as Timestamp,
                        flight.get("id").toString(),
                        flight.get("price").toString().toInt()
                    )
                    flightsList.add(flightObj)
                }
                val tvPurchaseFlightCompany = findViewById<TextView>(R.id.tvPurchaseFlightCompany)
                val tvPurchaseFlightDepartureTime =
                    findViewById<TextView>(R.id.tvPurchaseFlightDepartureTime)
                val tvPurchaseFlightTime = findViewById<TextView>(R.id.tvPurchaseFlightTime)
                val tvPurchaseFlightDestinationTime =
                    findViewById<TextView>(R.id.tvPurchaseFlightDestinationTime)
                val tvPurchaseFlightCodeDestination =
                    findViewById<TextView>(R.id.tvPurchaseFlightCodeDestination)
                val tvPurchaseFlightCodeDeparture =
                    findViewById<TextView>(R.id.tvPurchaseFlightCodeDeparture)
                val tvPurchaseFlightBaggageCap =
                    findViewById<TextView>(R.id.tvPurchaseFlightBaggageCap)
                val tvPurchaseFlightDelay = findViewById<TextView>(R.id.tvPurchaseFlightDelay)
                val tvPurchaseFlightPrice = findViewById<TextView>(R.id.tvPurchaseFlightPrice)

                tvPurchaseFlightCompany.text = flightsList[0].flightCompany
                tvPurchaseFlightDepartureTime.text = convertTime(flightsList[0].flightDepartureTime)
                tvPurchaseFlightTime.text = calculateDiff(
                    flightsList[0].flightDepartureTime,
                    flightsList[0].flightDestinationTime
                )
                tvPurchaseFlightDestinationTime.text =
                    convertTime(flightsList[0].flightDestinationTime)
                tvPurchaseFlightCodeDestination.text = flightsList[0].flightDestinationCode
                tvPurchaseFlightCodeDeparture.text = flightsList[0].flightDepartureCode
                tvPurchaseFlightBaggageCap.text = flightsList[0].flightBaggageCap
                tvPurchaseFlightDelay.text = flightsList[0].flightDelay
                tvPurchaseFlightPrice.text = flightsList[0].price.toString()
            }

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }

        val btnPurchase = findViewById<Button>(R.id.btnPurchase)
        btnPurchase.setOnClickListener {
            val balanceText = findViewById<TextView>(R.id.tvPurchaseBalance)
            val priceText = findViewById<TextView>(R.id.tvPurchaseFlightPrice)
            val balance = balanceText.text.toString().toInt()
            val price = priceText.text.toString().toInt()
            if (balance >= price) {
                val purchasedFlight =
                    ModelFlightPurchased(
                        flightId!!,
                        Timestamp.now(),
                        price,
                        cardsList[cardIndex].id,
                        flightsList[0].flightBaggageCap,
                        flightsList[0].flightCompany,
                        flightsList[0].flightDelay,
                        flightsList[0].flightDepartureCode,
                        flightsList[0].flightDestinationCode,
                        flightsList[0].flightDepartureTime,
                        flightsList[0].flightDestinationTime,
                    )
                // reduce balance
                dbRef.collection("users/${auth.uid}/cards").document(cardsList[cardIndex].id).set(
                    ModelCard(
                        cardsList[cardIndex].cardName,
                        cardsList[cardIndex].cardNumber,
                        cardsList[cardIndex].cardValidDate,
                        cardsList[cardIndex].cardCvv,
                        cardsList[cardIndex].id,
                        balance - price
                    )
                )

                // purchase flight
                dbRef.collection("users/${auth.uid}/flights").document().set(purchasedFlight)
                    .addOnSuccessListener {
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                            param(FirebaseAnalytics.Param.ITEM_NAME, "btnPurchase")
                        }

                        AlertBuilder(
                            "Başarılı",
                            "Uçuş satın alındı. Profilinizdeki uçuşlarım sekmesinden uçuşunuzun detaylarını görüntüleyebilirsiniz.",
                            "Tamam",
                            isCancelable = false
                        ).show(this, true)
                    }
            } else {
                AlertBuilder("Hata", "Yetersiz bakiye.", "Tamam").show(this)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val tvPurchaseCardName = findViewById<TextView>(R.id.tvPurchaseCardName)
        val tvPurchaseCardNumber = findViewById<TextView>(R.id.tvPurchaseCardNumber)
        val tvPurchaseCardValidDate = findViewById<TextView>(R.id.tvPurchaseCardValidDate)
        val tvPurchaseCardCvv = findViewById<TextView>(R.id.tvPurchaseCardCvv)
        val tvPurchaseBalance = findViewById<TextView>(R.id.tvPurchaseBalance)

        tvPurchaseCardName.text = cardsList[position].cardName
        tvPurchaseCardNumber.text = cardsList[position].cardNumber
        tvPurchaseCardValidDate.text = cardsList[position].cardValidDate
        tvPurchaseCardCvv.text = cardsList[position].cardCvv
        tvPurchaseBalance.text = cardsList[position].balance.toString()

        cardIndex = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
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