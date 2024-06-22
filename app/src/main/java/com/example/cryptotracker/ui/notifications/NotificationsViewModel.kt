package com.example.cryptotracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import coingecko.models.coins.CoinMarkets
import com.example.cryptotracker.model.Coin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class NotificationsViewModel(private val portfolioManager: PortfolioManager) : ViewModel() {

    private var alerts = mutableListOf<Alert>()
    private val alertAdapter = AlertAdapter(alerts) { alert ->
        removeTargetPrice(alert.coin)
    }

    val coins = liveData(Dispatchers.IO) {
        emit(CoinGeckoClientSingleton.getCoinMarkets().markets.map { m: CoinMarkets ->
            Coin(
                name = m.name,
                amount = m.circulatingSupply,
                pricePerUnit = m.currentPrice,
                logoUrl = m.image
            )
        })
    }

    suspend fun getCurrentPrice(coinName: String): Float? {
        return withContext(Dispatchers.IO) {
            val coinData = coins.value?.find { it.name == coinName }
            coinData?.pricePerUnit?.toFloat()
        }
    }

    fun saveTargetPrice(coin: String, targetPrice: Float, isAbove: Boolean) {
        viewModelScope.launch {
            portfolioManager.saveTargetPrice(coin, targetPrice, isAbove)
        }
    }

    fun getSavedAlerts(): Flow<List<Triple<String, Float?, Boolean?>>> {
        return portfolioManager.getAllTargetPrices()
    }

    fun removeTargetPrice(coin: String) {
        viewModelScope.launch {
            portfolioManager.removeTargetPrice(coin)
            val alertToRemove = alerts.find { it.coin == coin }
            alertToRemove?.let { alertAdapter.removeAlert(it) }
        }
    }
}
