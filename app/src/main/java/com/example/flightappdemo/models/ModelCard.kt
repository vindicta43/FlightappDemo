package com.example.flightappdemo.models

data class ModelCard(
    val cardName: String,
    val cardNumber: String,
    val cardValidDate: String,
    val cardCvv: String,
    val id: String,
    val balance: Int
)