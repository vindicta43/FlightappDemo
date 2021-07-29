package com.example.flightappdemo

import android.app.PendingIntent.getActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
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

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun changeFragment(frag: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containerFragment, frag)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

