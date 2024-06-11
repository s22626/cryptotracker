package com.example.cryptotracker.model

data class Coin(
    val name: String,
    val amount: Double,
    val pricePerUnit: Double,
    val logoUrl: String?
) {
    val totalValue: Double
        get() = amount * pricePerUnit
}
