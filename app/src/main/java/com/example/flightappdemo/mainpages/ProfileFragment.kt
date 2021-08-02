package com.example.flightappdemo.mainpages

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.flightappdemo.LoginPage
import com.example.flightappdemo.R
import com.example.flightappdemo.models.ModelUser
import com.google.android.material.textfield.TextInputEditText
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
            auth.signOut()
            Toast.makeText(view.context, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(view.context, LoginPage::class.java)
            startActivity(intent)
        }

        btnProfileUpdate.setOnClickListener {
            if (etProfileName.text.toString() == name &&
                etProfileSurname.text.toString() == surname &&
                etProfileMail.text.toString() == email
            ) {
                createAlert(view, false, auth.uid, name, surname, email)
            } else {
                createAlert(view, true, auth.uid, name, surname, email)
            }
        }

        return view
    }

    private fun createAlert(
        view: View?,
        isChanged: Boolean,
        id: String?,
        name: String?,
        surname: String?,
        email: String?
    ) {
        if (isChanged) {
            val alert = AlertDialog.Builder(view?.context)
            val dbRef = Firebase.firestore
            val newUser = ModelUser(id, name, surname, email)
            alert.setTitle("Uyarı")
            alert.setMessage("Bilgileriniz güncellenecektir. Onaylıyor musunuz?")
            alert.setCancelable(false)
            alert.setPositiveButton("Evet") { text, listener ->
                var userRef = dbRef.collection("users").document(auth.uid.toString())
                userRef.set(newUser)
                Toast.makeText(view?.context, "Updated successfully", Toast.LENGTH_SHORT).show()
            }
            alert.setNegativeButton("Hayır") { text, listener ->

            }
            alert.setNeutralButton("Iptal") { text, listener ->

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
}