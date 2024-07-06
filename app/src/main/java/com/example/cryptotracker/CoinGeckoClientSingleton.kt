package com.example.cryptotracker

import coingecko.CoinGeckoClient
import coingecko.error.CoinGeckoApiException
import coingecko.models.coins.CoinMarketsList

/**
 * Saves the target price for a specific coin.
 */
object CoinGeckoClientSingleton {
    private var cachedCoinMarketsList: CoinMarketsList = CoinMarketsList(emptyList(), 0, 0, 0, 0)
    private val instance: CoinGeckoClient by lazy { CoinGeckoClient() }

    /**
     * Fetches the coin markets from the CoinGecko API.
     * If the API returns a 429 error (too many requests), the function returns the cached list of coin markets.
     * @return The list of coin markets.
     * @throws CoinGeckoApiException If the API returns an error other than 429.
     */
    suspend fun getCoinMarkets(): CoinMarketsList {
        try {
            val res = instance.getCoinMarkets("usd", page = 1, perPage = 30)
            cachedCoinMarketsList = res
        } catch (e: CoinGeckoApiException) {
            if (e.error?.code?.equals(429) != true) {
                throw e
            }
        }
        return cachedCoinMarketsList
    }
}