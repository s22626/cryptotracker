package com.example.cryptotracker

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class CurrencyService {
    private val baseUrl = "https://api.nbp.pl/api/exchangerates/rates/c/usd/today?format=json"
    private val client: HttpClient = HttpClient()

    companion object {
        @Serializable
        data class CurrencyResponse(
            val table: String,
            val currency: String,
            val code: String,
            val rates: List<Rate>
        )

        @Serializable
        data class Rate(
            val no: String,
            val effectiveDate: String,
            val bid: Double,
            val ask: Double
        )
    }

    suspend fun getPLNForUSDRate(): Double {
        client.get(baseUrl).let { response ->
            val currencyResponse = Json.decodeFromString<CurrencyResponse>(response.bodyAsText())
            return currencyResponse.rates[0].bid
        }
    }
}