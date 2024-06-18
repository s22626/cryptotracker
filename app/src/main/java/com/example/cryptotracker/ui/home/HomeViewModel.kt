package com.example.cryptotracker.ui.home

import com.example.cryptotracker.PortfolioManager
import androidx.lifecycle.*
import coingecko.CoinGeckoClient
import com.example.cryptotracker.model.Coin
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private val portfolioManager: PortfolioManager) : ViewModel() {

    private val coinGecko: CoinGeckoClient = CoinGeckoClient()

    val coins: LiveData<List<Coin>> = liveData(Dispatchers.IO) {
        portfolioManager.getAllCoinAmountPairs().collect { coinPairs ->
            val coinMarkets = coinGecko.getCoinMarkets("usd", page = 1, perPage = 30).markets
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

    val totalValue: LiveData<Float> = coins.map { coins ->
        coins.sumOf { it.amount * it.pricePerUnit }.toFloat()
    }
}