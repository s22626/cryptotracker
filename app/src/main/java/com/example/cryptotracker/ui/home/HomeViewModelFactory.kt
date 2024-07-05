package com.example.cryptotracker.ui.home

import com.example.cryptotracker.PortfolioManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptotracker.CurrencyServiceI

/**
 * Factory for creating instances of HomeViewModel.
 * @property portfolioManager The PortfolioManager instance to be passed to the HomeViewModel.
 * @property currencyService The CurrencyServiceI instance to be passed to the HomeViewModel.
 */
class HomeViewModelFactory(private val portfolioManager: PortfolioManager, private val currencyService: CurrencyServiceI) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given ViewModel class.
     * @param modelClass The class of the ViewModel to create an instance of.
     * @return A new instance of the given ViewModel class.
     * @throws IllegalArgumentException If the given class is not assignable from HomeViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(portfolioManager, currencyService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}