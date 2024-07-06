package com.example.cryptotracker

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Interface for the currency service.
 */
interface CurrencyServiceI {
    /**
     * Fetches the exchange rate for USD to PLN.
     * @return The exchange rate as a Double.
     */
    suspend fun getPLNForUSDRate(): Double
}

/**
 * Implementation of the CurrencyServiceI interface.
 * Fetches data from the NBP API.
 */
class CurrencyService: CurrencyServiceI {
    private val baseUrl = "https://api.nbp.pl/api/exchangerates/rates/c/usd?format=json"
    private val client: HttpClient = HttpClient()

    companion object {
        @Serializable
        data class CurrencyResponse(
            val table: String,
            val currency: String,
            val code: String,
            val rates: List<Rate>
        )

        /**
         * Data class for the rate in the response from the NBP API.
         */
        @Serializable
        data class Rate(
            val no: String,
            val effectiveDate: String,
            val bid: Double,
            val ask: Double
        )
    }

    /**
     * Fetches the exchange rate for USD to PLN from the NBP API.
     * @return The exchange rate as a Double.
     */
    override suspend fun getPLNForUSDRate(): Double {
        client.get(baseUrl).let { response ->
            val currencyResponse = Json.decodeFromString<CurrencyResponse>(response.bodyAsText())
            return currencyResponse.rates[0].bid
        }
    }
}