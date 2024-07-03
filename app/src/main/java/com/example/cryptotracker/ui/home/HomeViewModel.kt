package com.example.cryptotracker.ui.home

import com.example.cryptotracker.PortfolioManager
import androidx.lifecycle.*
import com.example.cryptotracker.CoinGeckoClientSingleton
import com.example.cryptotracker.CurrencyService
import com.example.cryptotracker.model.Coin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val portfolioManager: PortfolioManager, private val currencyService: CurrencyService) : ViewModel() {

    var rate: Double = 0.0

    init {
        viewModelScope.launch {
            rate = currencyService.getPLNForUSDRate()
        }
    }

    val coins: LiveData<List<Coin>> = liveData(Dispatchers.IO) {
        portfolioManager.getAllCoinAmountPairs().collect { coinPairs ->
            val coinMarkets = CoinGeckoClientSingleton.getCoinMarkets().markets
            val coins = coinPairs.mapNotNull { pair ->
                coinMarkets.find { it.name == pair.first }?.let { market ->
                    pair.second?.let {
                        Coin(
                            name = market.name,
                            amount = it.toDouble(),
                            pricePerUnit = market.currentPrice,
                            logoUrl = market.image
                        )
                    }
                }
            }

            emit(coins)
        }
    }

    fun removeCoin(coin: String) {
        viewModelScope.launch {
            portfolioManager.removeCoin(coin)
        }
    }

    val totalValue: LiveData<Float> = coins.map { coins ->
        coins.sumOf { it.amount * it.pricePerUnit }.toFloat()
    }
}