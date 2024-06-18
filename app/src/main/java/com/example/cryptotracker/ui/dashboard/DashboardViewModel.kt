package com.example.cryptotracker.ui.dashboard

import com.example.cryptotracker.PortfolioManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import coingecko.CoinGeckoClient
import coingecko.models.coins.CoinMarkets
import com.example.cryptotracker.model.Coin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(private val portfolioManager: PortfolioManager) : ViewModel() {

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

    fun saveCoinAmount(coin: String, amount: Float) {
        viewModelScope.launch {
            portfolioManager.saveCoinAmount(coin, amount)
        }
    }
}