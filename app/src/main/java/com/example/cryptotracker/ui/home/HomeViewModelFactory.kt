package com.example.cryptotracker.ui.home

import com.example.cryptotracker.PortfolioManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory(private val portfolioManager: PortfolioManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(portfolioManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}