package com.example.cryptotracker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extension property to get the DataStore instance from the Context.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "crypto_tracker")

/**
 * Interface for the portfolio manager.
 */
interface PortfolioManagerInterface {
    /**
     * Saves the amount of a specific coin.
     */
    suspend fun saveCoinAmount(coin: String, amount: Float)

    /**
     * Gets the amount of a specific coin.
     */
    fun getAmountForCoin(coin: String): Flow<Float?>

    /**
     * Gets all coin-amount pairs.
     */
    fun getAllCoinAmountPairs(): Flow<List<Pair<String, Float?>>>

    /**
     * Saves the target price for a specific coin.
     */
    suspend fun saveTargetPrice(coin: String, targetPrice: Float, isAbove: Boolean)

    /**
     * Gets the target price for a specific coin.
     */
    fun getTargetPriceForCoin(coin: String): Flow<Pair<Float?, Boolean?>>

    /**
     * Gets all coin-target price pairs.
     */
    fun getAllTargetPrices(): Flow<List<Triple<String, Float?, Boolean?>>>

    /**
     * Removes the target price for a specific coin.
     */
    suspend fun removeTargetPrice(coin: String)

    /**
     * Removes a specific coin.
     */
    suspend fun removeCoin(coin: String)
}

/**
 * Implementation of the PortfolioManagerInterface.
 * Manages the portfolio of coins.
 */
class PortfolioManager(context: Context) : PortfolioManagerInterface {

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
            preferences.asMap().entries.mapNotNull { entry ->
                val key = entry.key.name
                val value = entry.value
                if (value is Float) {
                    Pair(key, value)
                } else {
                    null
                }
            }
        }
    }

    override suspend fun saveTargetPrice(coin: String, targetPrice: Float, isAbove: Boolean) {
        val targetPriceKey = floatPreferencesKey("${coin}_target")
        val isAboveKey = stringPreferencesKey("${coin}_isAbove")
        dataStore.edit { preferences ->
            preferences[targetPriceKey] = targetPrice
            preferences[isAboveKey] = isAbove.toString()
        }
    }

    override fun getTargetPriceForCoin(coin: String): Flow<Pair<Float?, Boolean?>> {
        val targetPriceKey = floatPreferencesKey("${coin}_target")
        val isAboveKey = stringPreferencesKey("${coin}_isAbove")
        return dataStore.data.map { preferences ->
            val targetPrice = preferences[targetPriceKey]
            val isAbove = preferences[isAboveKey]?.toBoolean()
            Pair(targetPrice, isAbove)
        }
    }

    override fun getAllTargetPrices(): Flow<List<Triple<String, Float?, Boolean?>>> {
        return dataStore.data.map { preferences ->
            preferences.asMap().entries.mapNotNull { entry ->
                val key = entry.key.name
                if (key.endsWith("_target")) {
                    val coin = key.removeSuffix("_target")
                    val targetPrice = entry.value as? Float
                    val isAbove = preferences[stringPreferencesKey("${coin}_isAbove")]?.toBoolean()
                    Triple(coin, targetPrice, isAbove)
                } else {
                    null
                }
            }
        }
    }

    override suspend fun removeTargetPrice(coin: String) {
        val targetPriceKey = floatPreferencesKey("${coin}_target")
        val isAboveKey = stringPreferencesKey("${coin}_isAbove")
        dataStore.edit { preferences ->
            preferences.remove(targetPriceKey)
            preferences.remove(isAboveKey)
        }
    }

    override suspend fun removeCoin(coin: String) {
        val coinKey = floatPreferencesKey(coin)
        dataStore.edit { preferences ->
            preferences.remove(coinKey)
        }
    }
}
