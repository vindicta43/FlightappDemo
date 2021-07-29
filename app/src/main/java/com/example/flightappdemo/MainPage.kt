package com.example.flightappdemo

import android.app.PendingIntent.getActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.flightappdemo.models.ModelFlight
import com.example.flightappdemo.models.ModelUser
import com.example.flightappdemo.utils.FlightAdapter
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.navigation.fragment.NavHostFragment
import com.example.flightappdemo.mainpages.AirportsFragment
import com.example.flightappdemo.mainpages.FlightsFragment
import com.example.flightappdemo.mainpages.ProfileFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        // pass the empty page
        changeFragment(AirportsFragment())

        // view implementations
        // val tvMainPage = findViewById<TextView>(R.id.tvMainPage)
        // val recyclerMain = findViewById<RecyclerView>(R.id.recyclerMain)
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        val fabAirplane = findViewById<FloatingActionButton>(R.id.fabAirplane)

        // for removing shadow and placeholder click event
        bottomNavView.background = null
        bottomNavView.menu.getItem(1).isEnabled = false

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

                // tvMainPage.text = "Welcome $name $surname"
            }

        // flights list for recyclerView adapter
        val flightsList = arrayListOf<ModelFlight>()

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
                //recyclerMain.layoutManager = GridLayoutManager(this, 2)
                //recyclerMain.adapter = FlightAdapter(flightsList)
            }

        // bottom nav view click event page transaction
        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navBarAirport -> {
                    val newFragment = AirportsFragment()
                    changeFragment(newFragment)
                }
                R.id.navBarProfile -> {
                    val newFragment = ProfileFragment()
                    changeFragment(newFragment)
                }
            }
            true
        }
        // fab flights
        fabAirplane.setOnClickListener {
            val newFragment = FlightsFragment()
            changeFragment(newFragment)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun changeFragment(frag: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containerFragment, frag)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

