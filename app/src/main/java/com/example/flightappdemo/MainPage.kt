package com.example.flightappdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flightappdemo.models.ModelFlight
import com.example.flightappdemo.models.ModelUser
import com.example.flightappdemo.utils.FlightAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.text.SimpleDateFormat

class MainPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        // view implementations
        val tvMainPage = findViewById<TextView>(R.id.tvMainPage)
        val recyclerMain = findViewById<RecyclerView>(R.id.recyclerMain)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        // val dbRef = FirebaseFirestore.getInstance()
        val dbRef = Firebase.firestore

        // getting user from firebase
        val userRef = dbRef.collection("users").whereEqualTo("id", auth.uid)
        userRef.get()
            .addOnSuccessListener { doc ->
                val allData = doc.documents[0].data

                val name = allData?.get("name")
                val surname = allData?.get("surname")
                val email = allData?.get("email")
                val id = allData?.get("id")

                tvMainPage.text = "Welcome $name $surname"
            }

        // flights list for recyclerView adapter
        var flightsList = arrayListOf<ModelFlight>()

        // getting flights from firebase
        val flightRef = dbRef.collection("flights")
        flightRef.get()
            .addOnSuccessListener { flights ->
                for (flight in flights) {
                    var flightObj = ModelFlight(
                        flight.get("airport").toString(),
                        flight.get("departTime") as com.google.firebase.Timestamp,
                        flight.get("departure").toString(),
                        flight.get("destination").toString(),
                        flight.get("flightCode").toString(),
                        flight.get("price").toString()
                    )
                    // filling arrayList
                    flightsList.add(flightObj)
                }
                recyclerMain.layoutManager = GridLayoutManager(this, 2)
                recyclerMain.adapter = FlightAdapter(flightsList)
            }
    }
}