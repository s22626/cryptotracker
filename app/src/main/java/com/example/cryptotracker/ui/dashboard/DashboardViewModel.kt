package com.example.cryptotracker.ui.dashboard

import com.example.cryptotracker.PortfolioManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import coingecko.CoinGeckoClient
import coingecko.error.CoinGeckoApiException
import coingecko.models.coins.CoinMarkets
import com.example.cryptotracker.CoinGeckoClientSingleton
import com.example.cryptotracker.model.Coin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(private val portfolioManager: PortfolioManager) : ViewModel() {

    val coins = liveData(Dispatchers.IO) {
        try {
            emit(
                CoinGeckoClientSingleton.getCoinMarkets().markets.map { m: CoinMarkets ->
                    Coin(
                        name = m.name,
                        amount = m.circulatingSupply,
                        pricePerUnit = m.currentPrice,
                        logoUrl = m.image
                    )
                })
        } catch (e: CoinGeckoApiException) {
            if (e.error?.code?.equals(429) != true){
                throw e
            }
        }
    }

    fun saveCoinAmount(coin: String, amount: Float) {
        viewModelScope.launch {
            portfolioManager.saveCoinAmount(coin, amount)
        }
    }
}