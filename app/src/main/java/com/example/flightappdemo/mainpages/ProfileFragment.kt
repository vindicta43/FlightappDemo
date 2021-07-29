package com.example.flightappdemo.mainpages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.flightappdemo.LoginPage
import com.example.flightappdemo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val dbRef = Firebase.firestore
        auth = FirebaseAuth.getInstance()

        val tvProfile = view.findViewById<TextView>(R.id.tvProfile)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        // getting user from firebase
        val userRef = dbRef.collection("users").whereEqualTo("id", auth.uid)
        userRef.get()
            .addOnSuccessListener { doc ->
                val allData = doc.documents[0].data

                val name = allData?.get("name")
                val surname = allData?.get("surname")
                val email = allData?.get("email")
                val id = allData?.get("id")

                tvProfile.text = "Welcome $name $surname\n" +
                        "email: $email\n" +
                        "id: $id"
            }

        btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(view.context, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(view.context, LoginPage::class.java)
            startActivity(intent)
        }

        return view
    }
}