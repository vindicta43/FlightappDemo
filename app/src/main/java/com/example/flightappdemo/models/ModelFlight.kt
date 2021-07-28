package com.example.flightappdemo.models

data class ModelFlight(
    val airport: String,
    val departTime: com.google.firebase.Timestamp,
    val departure: String,
    val destination: String,
    val flightCode: String,
    val price: String
) {
}