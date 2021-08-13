package com.example.flightappdemo.models

data class ModelFlight(
    val flightBaggageCap: String,
    val flightCompany: String,
    val flightDelay: String,
    val flightDepartureCode: String,
    val flightDestinationCode: String,
    val flightDepartureTime: com.google.firebase.Timestamp,
    val flightDestinationTime: com.google.firebase.Timestamp,
    val id: String,
    val price: Int
)