package com.example.flightappdemo

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainPage : AppCompatActivity() {
    // firebase auth instance
    private lateinit var auth: FirebaseAuth

    // remote config instance
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        // remote config codes for check updates
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    val versionNum = applicationContext.packageManager.getPackageInfo(
                        this.packageName,
                        0
                    ).versionName
                    val latestVersion = remoteConfig.getString("latest_version")
                    val minimumVersion = remoteConfig.getString("minimum_version")

                    // if an update available and user among minimum version and latest version
                    if (versionNum.toDouble() >= minimumVersion.toDouble() && versionNum.toDouble() < latestVersion.toDouble()) {
                        showAvailableUpdate(remoteConfig.getString("url"))
                    }
                    // if an update available and user have older version than minimum
                    if (versionNum.toDouble() < minimumVersion.toDouble()) {
                        forceUpdate(remoteConfig.getString("url"))
                    }
                }
            }

        // pass the empty page and make as main page
        changeFragment(AirportsFragment())

        // view implementations
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        val fabAirplane = findViewById<FloatingActionButton>(R.id.fabAirplane)

        // for removing shadow and placeholder click event
        bottomNavView.background = null
        bottomNavView.menu.getItem(1).isEnabled = false

        auth = FirebaseAuth.getInstance()

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

    private fun showAvailableUpdate(url: String) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Güncelleme Mevcut")
        alert.setMessage("Yeni güncelleme mevcut. Tamam butonuna basarak yeni sürüme güncelleyin.")
        alert.setNegativeButton("Iptal") { text, listener->
        }
        alert.setPositiveButton("Güncelle") { text, listener->
            val redirectUrl = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, redirectUrl)
            startActivity(intent)
        }
        alert.setCancelable(true)
        alert.show()
    }

    private fun forceUpdate(url: String) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Uyarı")
        alert.setMessage("Uygulamanız güncel değil. Lütfen en son sürüme güncelleyin.")
        alert.setNegativeButton("Iptal") { text, listener->
            finishAffinity()
        }
        alert.setPositiveButton("Güncelle") { text, listener->
            val redirectUrl = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, redirectUrl)
            startActivity(intent)
            finishAffinity()
        }
        alert.setCancelable(false)
        alert.show()
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

