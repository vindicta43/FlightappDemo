package com.example.flightappdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.flightappdemo.models.ModelAirport
import com.example.flightappdemo.tutorialpages.FirstFragment
import com.example.flightappdemo.tutorialpages.SecondFragment
import com.example.flightappdemo.tutorialpages.ThirdFragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pager = findViewById<View>(R.id.viewPager) as ViewPager
        pager.adapter = MyPagerAdapter(supportFragmentManager)

        // filling flights collection
//        var auth: FirebaseAuth = Firebase.auth
//        var dbRef: FirebaseFirestore = Firebase.firestore
//        for (i in 0..15) {
//            dbRef
//                .collection("flights")
//                .add(ModelFlight(
//                    "airport num$i",
//                    Timestamp(System.currentTimeMillis()),
//                    "departure num$i",
//                    "destination num$i",
//                    "num$i".uppercase(Locale.getDefault()),
//                    "${i}00"
//                ))
//        }

        // filling airports collection
//        var auth: FirebaseAuth = Firebase.auth
//        var dbRef: FirebaseFirestore = Firebase.firestore
//        for (i in 0..10) {
//            dbRef
//                .collection("airports")
//                .add(
//                    ModelAirport(
//                        "$i airport",
//                        Timestamp.now(),
//                        Timestamp.now(),
//                        Timestamp.now(),
//                        "DEP$i",
//                        "DEST$i",
//                        "${i*5} kg",
//                        "${i+2} delay"
//                    )
//                )
//        }

        // single use tutorial page
        val preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        val firstTime = preferences.getString("FirstTime", "")

        if (firstTime.equals("true")) {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        } else {
            val editor = preferences.edit()
            editor.putString("FirstTime", "true")
            editor.apply()
        }
    }

    private inner class MyPagerAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!) {
        override fun getItem(pos: Int): Fragment {
            return when (pos) {
                0 -> FirstFragment()
                1 -> SecondFragment()
                2 -> ThirdFragment()
                else -> ThirdFragment()
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}