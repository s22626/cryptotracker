package com.example.cryptotracker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coingecko.error.CoinGeckoApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Worker class for checking price alerts.
 * This class extends CoroutineWorker and overrides the doWork function to check if any of the coins have reached their target price.
 * If a coin has reached its target price, a notification is sent.
 * @property portfolioManager The PortfolioManager instance used to get the target prices for the coins.
 */
class PriceAlertWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val portfolioManager = PortfolioManager(appContext)

    /**
     * The function that is called when the worker is executed.
     * This function checks if any of the coins have reached their target price and sends a notification if they have.
     * @return The result of the worker execution.
     */
    override suspend fun doWork(): Result {
        Log.i("PriceAlertWorker", "doWork called")
        val coinsWithTargets = portfolioManager.getAllTargetPrices().first()
        Log.i("PriceAlertWorker", "coinsWithTargets: $coinsWithTargets")

        // Retrieve market data for all these coins
        val coinMarketsList = withContext(Dispatchers.IO) {
            CoinGeckoClientSingleton.getCoinMarkets()
        }

        Log.i("PriceAlertWorker", "coinMarketsList: $coinMarketsList")

        for ((coin, targetPrice, isAbove) in coinsWithTargets) {
            Log.i("PriceAlertWorker", "Checking coin: $coin")
            if (targetPrice != null && isAbove != null) {
                Log.i("PriceAlertWorker", "Target price for $coin: $targetPrice, isAbove: $isAbove")
                val coinData = coinMarketsList.markets.find { it.name == coin }
                Log.i("PriceAlertWorker", "coinData for $coin: $coinData")
                val currentPrice = coinData?.currentPrice
                Log.i("PriceAlertWorker", "Current price for $coin: $currentPrice")
                if (currentPrice != null) {
                    if ((isAbove && currentPrice >= targetPrice) || (!isAbove && currentPrice <= targetPrice)) {
                        Log.i(
                            "PriceAlertWorker",
                            "$coin has reached the target price: $currentPrice"
                        )
                        sendNotification(coin, currentPrice)
                    }
                }
            }
        }

        return Result.success()
    }

    /**
     * Sends a notification that a coin has reached its target price.
     * @param coin The name of the coin.
     * @param currentPrice The current price of the coin.
     */
    @SuppressLint("MissingPermission")
    private fun sendNotification(coin: String, currentPrice: Double) {
        Log.i("PriceAlertWorker", "sendNotification called for $coin")
        val channelId = "price_alert_channel"
        val notificationId = coin.hashCode() // Unique notification ID for each coin

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Price Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for price alerts"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Price Alert")
            .setContentText("$coin has reached the target price: $currentPrice")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            Log.i("PriceAlertWorker", "Sending notification for $coin")
            try {
                notify(notificationId, builder.build())
                Log.i("PriceAlertWorker", "Notification sent successfully for $coin")
            } catch (e: Exception) {
                Log.e("PriceAlertWorker", "Error sending notification for $coin", e)
            }
        }
    }
}
