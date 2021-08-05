package com.example.flightappdemo

import android.app.Person
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.flightappdemo.models.ModelUser
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: FirebaseFirestore
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)
        auth = Firebase.auth
        dbRef = Firebase.firestore
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "RegisterPage")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Register Page")
        }

        val etRegisterName = findViewById<TextInputEditText>(R.id.etRegisterName)
        val etRegisterSurname = findViewById<TextInputEditText>(R.id.etRegisterSurname)
        val etRegisterMail = findViewById<TextInputEditText>(R.id.etRegisterMail)
        val etRegisterPasswd = findViewById<TextInputEditText>(R.id.etRegisterPasswd)
        val etRegisterPasswdApply = findViewById<TextInputEditText>(R.id.etRegisterPasswdApply)

        val btnRegisterPage = findViewById<Button>(R.id.btnRegisterPage)
        btnRegisterPage.setOnClickListener {
            // if spaces are empty
            try {
                if (etRegisterName.text.isNullOrBlank() &&
                    etRegisterSurname.text.isNullOrBlank() &&
                    etRegisterMail.text.isNullOrBlank() &&
                    etRegisterPasswd.text.isNullOrBlank() &&
                    etRegisterPasswdApply.text.isNullOrBlank()
                ) {

                    Toast.makeText(this, "All spaces must be filled.", Toast.LENGTH_SHORT).show()
                }
                // login operation
                else {
                    // password equality check
                    if (etRegisterPasswd.text.toString() == etRegisterPasswdApply.text.toString()) {
                        val name = etRegisterName.text.toString()
                        val surname = etRegisterSurname.text.toString()
                        val email = etRegisterMail.text.toString()
                        val password = etRegisterPasswd.text.toString()

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success")

                                    // user object
                                    val userObj = ModelUser(auth.uid, name, surname, email)

                                    // adding data to firebase
                                    dbRef.collection("users").document(auth.uid.toString()).set(userObj).addOnSuccessListener {

                                    }
                                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                                        param(FirebaseAnalytics.Param.METHOD, "sign_up_email")
                                    }
                                    Toast.makeText(this, "Register succeed.", Toast.LENGTH_LONG)
                                        .show()
                                    this.finish()
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        this,
                                        "${task.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Passwords doesn't match.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}