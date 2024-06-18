package com.example.cryptotracker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "crypto_tracker")

interface PortfolioManagerInterface {
    suspend fun saveCoinAmount(coin: String, amount: Float)
    fun getAmountForCoin(coin: String): Flow<Float?>
    fun getAllCoinAmountPairs(): Flow<List<Pair<String, Float?>>>
}

class PortfolioManager(context: Context): PortfolioManagerInterface {

    private val dataStore: DataStore<Preferences> = context.dataStore

    override suspend fun saveCoinAmount(coin: String, amount: Float) {
        val coinKey = floatPreferencesKey(coin)
        dataStore.edit { preferences ->
            preferences[coinKey] = amount
        }
    }

    override fun getAmountForCoin(coin: String): Flow<Float?> {
        val coinKey = floatPreferencesKey(coin)
        return dataStore.data.map { preferences ->
            preferences[coinKey]
        }
    }

    override fun getAllCoinAmountPairs(): Flow<List<Pair<String, Float?>>> {
        return dataStore.data.map { preferences ->
            preferences.asMap().entries.map { entry ->
                val coin = entry.key.name
                val amount = entry.value as Float?
                Pair(coin, amount)
            }
        }
    }
}