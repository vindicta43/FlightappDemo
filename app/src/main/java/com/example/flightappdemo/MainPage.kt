package com.example.flightappdemo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.example.flightappdemo.mainpages.AirportsFragment
import com.example.flightappdemo.mainpages.FlightsFragment
import com.example.flightappdemo.mainpages.ProfileFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.zxing.integration.android.IntentIntegrator

class MainPage : AppCompatActivity() {
    // firebase auth instance
    private lateinit var auth: FirebaseAuth

    // remote config instance
    private lateinit var remoteConfig: FirebaseRemoteConfig

    private lateinit var firebaseAnalytics: FirebaseAnalytics

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
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        // bottom nav view click event page transaction
        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navBarAirport -> {
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                        param(FirebaseAnalytics.Param.SCREEN_CLASS, "AirportsFragment")
                        param(FirebaseAnalytics.Param.SCREEN_NAME, "Airports")
                    }
                    val newFragment = AirportsFragment()
                    changeFragment(newFragment)
                }
                R.id.navBarProfile -> {
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                        param(FirebaseAnalytics.Param.SCREEN_CLASS, "ProfileFragment")
                        param(FirebaseAnalytics.Param.SCREEN_NAME, "Profile")
                    }
                    val newFragment = ProfileFragment()
                    changeFragment(newFragment)
                }
            }
            true
        }
        // fab flights
        fabAirplane.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(FirebaseAnalytics.Param.SCREEN_CLASS, "FlightsFragment")
                param(FirebaseAnalytics.Param.SCREEN_NAME, "Flights")
            }
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

    // get the qr results
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this, ResultPage::class.java)
                intent.putExtra("id", result.contents)
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}

