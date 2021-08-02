package com.example.flightappdemo.models

data class ModelAirport(
    val airport: String,
    val departure: com.google.firebase.Timestamp,
    val flightTime: com.google.firebase.Timestamp,
    val destination: com.google.firebase.Timestamp,
    val flightCodeDepart: String,
    val flightCodeDest: String,
    val kilogram: String,
    val delay: String,
)