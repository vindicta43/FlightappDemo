package com.example.flightappdemo.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.flightappdemo.R
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
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            Toast.makeText(this, "Login succeed.", Toast.LENGTH_SHORT).show()
                            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                                param(FirebaseAnalytics.Param.METHOD, "email_login")
                            }
                            val intent = Intent(this, MainPage::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)
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
                val alert = AlertDialog.Builder(this)
                    .setTitle("Uyarı")
                    .setMessage("Sıfırlama emaili için yukarıdaki email alanını doldurun.")
                    .setPositiveButton("Tamam") { text, listener ->

                    }
                    .setCancelable(true)
                alert.show()
            }
            else {
                Firebase.auth.sendPasswordResetEmail(email.text.toString()).addOnSuccessListener {
                    val alert = AlertDialog.Builder(this)
                        .setTitle("Başarılı")
                        .setMessage("Sıfırlama emaili gönderildi. Lütfen gelen kutunuzu kontrol edin.")
                        .setPositiveButton("Tamam") { text, listener ->

                        }
                        .setCancelable(true)
                    alert.show()
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