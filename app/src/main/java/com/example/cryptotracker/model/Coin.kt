package com.example.cryptotracker.model

/**
 * Data class representing a coin.
 * @property name The name of the coin.
 * @property amount The amount of the coin.
 * @property pricePerUnit The price per unit of the coin.
 * @property logoUrl The URL of the coin's logo.
 * @property totalValue The total value of the coin, calculated as the amount times the price per unit.
 */
data class Coin(
    val name: String,
    val amount: Double,
    val pricePerUnit: Double,
    val logoUrl: String?
) {
    val totalValue: Double
        get() = amount * pricePerUnit
}
