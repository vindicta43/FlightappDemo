package com.example.flightappdemo.models

data class ModelFlightPurchased(
    val flight: String,
    val boughtDate: com.google.firebase.Timestamp,
    val price: Int,
    val cardId: String,
    val flightBaggageCap: String,
    val flightCompany: String,
    val flightDelay: String,
    val flightDepartureCode: String,
    val flightDestinationCode: String,
    val flightDepartureTime: com.google.firebase.Timestamp,
    val flightDestinationTime: com.google.firebase.Timestamp,
)