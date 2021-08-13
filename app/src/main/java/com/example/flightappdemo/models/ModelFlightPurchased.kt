package com.example.flightappdemo.models

data class ModelFlightPurchased(
    val flight: String,
    val boughtDate: com.google.firebase.Timestamp,
    val price: Int,
    val cardId: String
)