package com.example.flightappdemo.mainpages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.flightappdemo.pages.LoginPage
import com.example.flightappdemo.R
import com.example.flightappdemo.pages.ReportPage
import com.example.flightappdemo.pages.EditProfilePage
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        val btnNavigateUpdate = view.findViewById<Button>(R.id.btnNavigateUpdate)
        btnNavigateUpdate.setOnClickListener {
            val intent = Intent(view.context, EditProfilePage::class.java)
            startActivity(intent)
        }

        val btnOpenReport = view.findViewById<Button>(R.id.btnOpenReport)
        btnOpenReport.setOnClickListener {
            val intent = Intent(view.context, ReportPage::class.java)
            startActivity(intent)
        }

        val btnProfileLogout = view.findViewById<Button>(R.id.btnProfileLogout)
        btnProfileLogout.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(FirebaseAnalytics.Param.ITEM_NAME, "btnProfileLogout")
            }
            auth.signOut()
            Toast.makeText(view.context, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(view.context, LoginPage::class.java)
            startActivity(intent)
        }

        return view
    }

}