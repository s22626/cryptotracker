package com.example.cryptotracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import coingecko.CoinGeckoClient
import coingecko.models.coins.CoinMarkets
import com.example.cryptotracker.model.Coin
import kotlinx.coroutines.Dispatchers

class HomeViewModel : ViewModel() {

    private val coinGecko: CoinGeckoClient = CoinGeckoClient()

    val coins = liveData(Dispatchers.IO) {
        emit(coinGecko.getCoinMarkets("usd", page = 1, perPage = 10).markets.map { m: CoinMarkets ->
            Coin(
                name = m.name,
                amount = m.circulatingSupply,
                pricePerUnit = m.currentPrice,
                logoUrl = m.image
            )
        })
    }
}