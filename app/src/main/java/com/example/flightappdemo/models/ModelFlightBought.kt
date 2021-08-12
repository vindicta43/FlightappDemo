package com.example.flightappdemo.models

data class ModelFlightBought(
    val flight: String,
    val id: String,
    val boughtDate: com.google.firebase.Timestamp
)