package com.example.flightappdemo.pages

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelUser
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditProfilePage : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_page)

        val dbRef = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "EditProfilePage")
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Edit Profile")
        }

        var name: String? = ""
        var surname: String? = ""
        var email: String? = ""
        val etProfileName = findViewById<TextInputEditText>(R.id.etProfileName)
        val etProfileSurname = findViewById<TextInputEditText>(R.id.etProfileSurname)
        val etProfileMail = findViewById<TextInputEditText>(R.id.etProfileMail)
        val etProfilePasswd = findViewById<TextInputEditText>(R.id.etProfilePasswd)

        // getting user from firebase
        val userRef = dbRef.collection("users").whereEqualTo("id", auth.uid)
        userRef.get()
            .addOnSuccessListener { doc ->
                val allData = doc.documents[0].data

                name = allData?.get("name").toString()
                surname = allData?.get("surname").toString()
                email = allData?.get("email").toString()

                etProfileName.setText(name.toString())
                etProfileSurname.setText(surname.toString())
                etProfileMail.setText(email.toString())
            }

        val btnProfileUpdate = findViewById<Button>(R.id.btnProfileUpdate)
        btnProfileUpdate.setOnClickListener {
            if (etProfileName.text.toString() == name &&
                etProfileSurname.text.toString() == surname &&
                etProfileMail.text.toString() == email &&
                etProfilePasswd.text.isNullOrEmpty()
            ) {
                createAlert(
                    false,
                    auth.uid,
                    etProfileName.text.toString(),
                    etProfileSurname.text.toString(),
                    etProfileMail.text.toString()
                )
            } else {
                createAlert(
                    true,
                    auth.uid,
                    etProfileName.text.toString(),
                    etProfileSurname.text.toString(),
                    etProfileMail.text.toString()
                )
            }
        }
    }

    private fun createAlert(
        isChanged: Boolean,
        funId: String?,
        funName: String?,
        funSurname: String?,
        funEmail: String?
    ) {
        if (isChanged) {
            val alert = AlertDialog.Builder(this)
            val dbRef = Firebase.firestore
            val newUser = ModelUser(funId, funName, funSurname, funEmail)
            val password = findViewById<TextInputEditText>(R.id.etProfilePasswd)
            alert.setTitle("Uyarı")
            alert.setMessage("Bilgileriniz güncellenecektir. Onaylıyor musunuz?")
            alert.setCancelable(false)
            alert.setPositiveButton("Evet") { _, _ ->
                // if email changed or password changed
                if (funEmail != auth.currentUser?.email || password.text.toString().isNotEmpty()) {
                    // new alert dialog for validate password
                    alert.setTitle("Onayla")
                    alert.setMessage("Bilgilerinizi değiştirmek için parolanızı girin")
                    val input = EditText(this)
                    input.hint = "Parola"
                    input.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    input.transformationMethod = PasswordTransformationMethod.getInstance()
                    alert.setView(input)

                    alert.setPositiveButton("Onayla") { _, _ ->
                        val authEmail = auth.currentUser?.email.toString()
                        val authPass = input.text.toString()
                        val credential = EmailAuthProvider.getCredential(authEmail, authPass)
                        val user = Firebase.auth.currentUser
                        user?.reauthenticate(credential)
                            // update email
                            ?.addOnCompleteListener {
                                // authenticade verified
                                if (it.isSuccessful) {
                                    try {
                                        // update email
                                        user.updateEmail(funEmail!!).addOnCompleteListener {

                                        }
                                        // if password changed uptade
                                        if (password.text.toString().isNotEmpty()) {
                                            user.updatePassword(password.text.toString()).addOnCompleteListener {

                                            }
                                        }
                                        // update part
                                        val userRef =
                                            dbRef.collection("users").document(auth.uid.toString())
                                        userRef.set(newUser)
                                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                                            param(FirebaseAnalytics.Param.ITEM_NAME, "btnProfileUpdate")
                                        }
                                        // page transaction
                                        logout()
                                    } catch (e: Exception) {
                                        Toast.makeText(this, e.message, Toast.LENGTH_LONG)
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Authentication failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                    alert.setNegativeButton("Iptal") { _, _ ->
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    alert.show()
                } else {
                    val userRef = dbRef.collection("users").document(auth.uid.toString())
                    userRef.set(newUser)
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                        param(FirebaseAnalytics.Param.ITEM_NAME, "btnProfileUpdate")
                    }
                    Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            alert.setNegativeButton("Hayır") { _, _ ->

            }
            alert.show()
        } else {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Uyarı")
            alert.setMessage("Hiçbir bilgi değiştirilmedi.")
            alert.setCancelable(false)
            alert.setPositiveButton("Tamam") { _, _ ->
            }
            alert.show()
        }
    }

    private fun logout() {
        // new alert for reauthenticate
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Başarılı")
        alert.setMessage("Bilgileriniz güncellendi. Yeniden giriş yapmanız gerekli.")
        alert.setCancelable(false)
        alert.setPositiveButton("Tamam") { _, _ ->
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            auth.signOut()
        }
        alert.show()
    }
}