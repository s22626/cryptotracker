package com.example.cryptotracker

import coingecko.CoinGeckoClient
import coingecko.error.CoinGeckoApiException
import coingecko.models.coins.CoinMarketsList

object CoinGeckoClientSingleton {
    private var cachedCoinMarketsList: CoinMarketsList = CoinMarketsList(emptyList(), 0, 0, 0, 0)
    private val instance: CoinGeckoClient by lazy { CoinGeckoClient() }

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