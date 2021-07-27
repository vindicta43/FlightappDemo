package com.example.flightappdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.flightappdemo.models.ModelUser
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

class MainPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val tvMainPage = findViewById<TextView>(R.id.tvMainPage)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        // val dbRef = FirebaseFirestore.getInstance()
        val dbRef = Firebase.firestore
        val colRef = dbRef.collection("users")

        val docRef = dbRef.collection("users").whereEqualTo("id", auth.uid)
        docRef.get()
            .addOnSuccessListener { doc ->
                // tvMainPage.text = doc.data
                val allData = doc.documents[0].data

                val name = allData?.get("name")
                val surname = allData?.get("surname")
                val email = allData?.get("email")
                val id = allData?.get("id")

                tvMainPage.text = "name: $name\n" +
                        "surname: $surname\n" +
                        "email: $email\n" +
                        "id: $id"
            }
            .addOnFailureListener {

            }
    }
}