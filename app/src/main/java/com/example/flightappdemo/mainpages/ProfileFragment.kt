package com.example.flightappdemo.mainpages

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.flightappdemo.LoginPage
import com.example.flightappdemo.R
import com.example.flightappdemo.ReportPage
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

class ProfileFragment : Fragment() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val dbRef = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        var name: String? = ""
        var surname: String? = ""
        var email: String? = ""
        val etProfileName = view.findViewById<TextInputEditText>(R.id.etProfileName)
        val etProfileSurname = view.findViewById<TextInputEditText>(R.id.etProfileSurname)
        val etProfileMail = view.findViewById<TextInputEditText>(R.id.etProfileMail)
        val etProfilePasswd = view.findViewById<TextInputEditText>(R.id.etProfilePasswd)
        val btnProfileUpdate = view.findViewById<Button>(R.id.btnProfileUpdate)
        val btnProfileLogout = view.findViewById<Button>(R.id.btnProfileLogout)

        // getting user from firebase
        val userRef = dbRef.collection("users").whereEqualTo("id", auth.uid)
        userRef.get()
            .addOnSuccessListener { doc ->
                val allData = doc.documents[0].data

                name = allData?.get("name").toString()
                surname = allData?.get("surname").toString()
                email = allData?.get("email").toString()

                etProfileName.text = Editable.Factory.getInstance().newEditable(name.toString())
                etProfileSurname.text =
                    Editable.Factory.getInstance().newEditable(surname.toString())
                etProfileMail.text = Editable.Factory.getInstance().newEditable(email.toString())
            }

        btnProfileLogout.setOnClickListener {
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(FirebaseAnalytics.Param.ITEM_NAME, "btnProfileLogout")
            }
            auth.signOut()
            Toast.makeText(view.context, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(view.context, LoginPage::class.java)
            startActivity(intent)
        }

        btnProfileUpdate.setOnClickListener {
            if (etProfileName.text.toString() == name &&
                etProfileSurname.text.toString() == surname &&
                etProfileMail.text.toString() == email &&
                etProfilePasswd.text.isNullOrEmpty()
            ) {
                createAlert(
                    view,
                    false,
                    auth.uid,
                    etProfileName.text.toString(),
                    etProfileSurname.text.toString(),
                    etProfileMail.text.toString()
                )
            } else {
                createAlert(
                    view,
                    true,
                    auth.uid,
                    etProfileName.text.toString(),
                    etProfileSurname.text.toString(),
                    etProfileMail.text.toString()
                )
            }
        }

        val btnOpenReport = view.findViewById<Button>(R.id.btnOpenReport)
        btnOpenReport.setOnClickListener {
            val intent = Intent(view.context, ReportPage::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun createAlert(
        view: View?,
        isChanged: Boolean,
        funId: String?,
        funName: String?,
        funSurname: String?,
        funEmail: String?
    ) {
        if (isChanged) {
            val alert = AlertDialog.Builder(view?.context)
            val dbRef = Firebase.firestore
            val newUser = ModelUser(funId, funName, funSurname, funEmail)
            alert.setTitle("Uyarı")
            alert.setMessage("Bilgileriniz güncellenecektir. Onaylıyor musunuz?")
            alert.setCancelable(false)
            alert.setPositiveButton("Evet") { text, listener ->
                // if email changed
                if (funEmail != auth.currentUser?.email) {
                    // new alert dialog for validate password
                    alert.setTitle("Onayla")
                    alert.setMessage("Bilgilerinizi değiştirmek için parolanızı girin")
                    val input = EditText(view?.context)
                    input.setHint("Parola")
                    input.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    input.transformationMethod = PasswordTransformationMethod.getInstance()
                    alert.setView(input)

                    alert.setPositiveButton("Onayla") { text, listener ->
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
                                        val passField =
                                            view?.findViewById<TextInputEditText>(R.id.etProfilePasswd)
                                        if (passField?.text.toString().isNotEmpty()) {
                                            user.updatePassword(passField?.text.toString()).addOnCompleteListener {

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
                                        Toast.makeText(view?.context, e.message, Toast.LENGTH_LONG)
                                            .show()
                                    }
                                } else {
                                    Toast.makeText(
                                        view?.context,
                                        "Authentication failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                    alert.setNegativeButton("Iptal") { text, listener ->
                        Toast.makeText(view?.context, "Cancelled", Toast.LENGTH_SHORT).show()
                    }
                    alert.show()
                } else {
                    val userRef = dbRef.collection("users").document(auth.uid.toString())
                    userRef.set(newUser)
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                        param(FirebaseAnalytics.Param.ITEM_NAME, "btnProfileUpdate")
                    }
                    Toast.makeText(view?.context, "Updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
            alert.setNegativeButton("Hayır") { text, listener ->

            }
            alert.show()
        } else {
            val alert = AlertDialog.Builder(view?.context)
            alert.setTitle("Uyarı")
            alert.setMessage("Hiçbir bilgi değiştirilmedi.")
            alert.setCancelable(false)
            alert.setPositiveButton("Tamam") { text, listener ->
            }
            alert.show()
        }
    }

    private fun logout() {
        // new alert for reauthenticate
        val alert = AlertDialog.Builder(view?.context)
        alert.setTitle("Başarılı")
        alert.setMessage("Bilgileriniz güncellendi. Yeniden giriş yapmanız gerekli.")
        alert.setCancelable(false)
        alert.setPositiveButton("Tamam") { text, listener ->
            val intent = Intent(view?.context, LoginPage::class.java)
            startActivity(intent)
            auth.signOut()
        }
        alert.show()
    }
}