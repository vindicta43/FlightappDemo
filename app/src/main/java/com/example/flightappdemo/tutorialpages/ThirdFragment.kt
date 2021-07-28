package com.example.flightappdemo.tutorialpages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.flightappdemo.LoginPage
import com.example.flightappdemo.R


class ThirdFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        val btnSkip2 = view.findViewById<Button>(R.id.btnSkip2)
        btnSkip2.setOnClickListener {
            val intent = Intent(context, LoginPage::class.java)
            startActivity(intent)
        }
        return view
    }
}