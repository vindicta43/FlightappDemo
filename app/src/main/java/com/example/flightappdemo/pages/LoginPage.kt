package com.example.flightappdemo.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.flightappdemo.R
import com.example.flightappdemo.utils.AlertBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = Firebase.auth
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "LoginPage")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Login Page")
        }

        // checking login status
        // if user already logged in navigate to main page
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                param(FirebaseAnalytics.Param.METHOD, "email_already_registered")
            }
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }

        val email = findViewById<TextInputEditText>(R.id.etLoginMail)
        val password = findViewById<TextInputEditText>(R.id.etLoginPasswd)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            if (email.text.isNullOrBlank() && password.text.isNullOrBlank()) {
                Toast.makeText(this, "Email and password must be filled.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            AlertBuilder(
                                "Ba??ar??l??",
                                "Giri?? ba??ar??l??.",
                                "Tamam",
                                isCancelable = false
                            )
                                .show(this)
                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                                param(FirebaseAnalytics.Param.METHOD, "email_login")
                            }
                            val intent = Intent(this, MainPage::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                baseContext, "${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        val tvForgotPasswd = findViewById<TextView>(R.id.tvForgotPasswd)
        tvForgotPasswd.setOnClickListener {
            // if email field is empty
            if (email.text.isNullOrEmpty()) {
                email.requestFocus()
                AlertBuilder(
                    "Uyar??",
                    "S??f??rlama emaili i??in yukar??daki email alan??n?? doldurun.",
                    "Tamam"
                )
                    .show(this)
            } else {
                Firebase.auth.sendPasswordResetEmail(email.text.toString()).addOnSuccessListener {
                    AlertBuilder(
                        "Ba??ar??l??",
                        "S??f??rlama emaili g??nderildi. L??tfen gelen kutunuzu kontrol edin.",
                        "Tamam"
                    ).show(this)
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}