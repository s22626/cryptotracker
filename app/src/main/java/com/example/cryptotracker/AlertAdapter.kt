package com.example.cryptotracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Data class representing an alert.
 * @property coin The name of the coin.
 * @property targetPrice The target price for the alert.
 * @property isAbove Boolean indicating whether the alert is for when the price is above the target price.
 */
data class Alert(val coin: String, val targetPrice: Float,  val isAbove: Boolean)

/**
 * Adapter for the RecyclerView that displays the list of alerts.
 * @property alerts List of alerts to be displayed.
 * @property onRemoveClick Lambda function that is invoked when the remove button is clicked.
 */
class AlertAdapter(
    private val alerts: MutableList<Alert>,
    private val onRemoveClick: (Alert) -> Unit
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    /**
     * ViewHolder for the RecyclerView items.
     * @property coinTextView TextView that displays the coin name.
     * @property targetPriceTextView TextView that displays the target price.
     * @property removeButton Button that removes the alert from the list when clicked.
     */
    class AlertViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coinTextView: TextView = view.findViewById(R.id.coin_text_view)
        val targetPriceTextView: TextView = view.findViewById(R.id.target_price_text_view)
        val removeButton: Button = view.findViewById(R.id.remove_button)
    }

    /**
     * Inflates the layout for the RecyclerView items.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alert_item, parent, false)
        return AlertViewHolder(view)
    }

    /**
     * Binds the data to the RecyclerView items.
     */
    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = alerts[position]
        holder.coinTextView.text = alert.coin
        holder.targetPriceTextView.text = alert.targetPrice.toString()
        holder.removeButton.setOnClickListener {
            onRemoveClick(alert)
        }
    }

    /**
     * Removes an alert from the list and notifies the adapter of the change.
     * @param alert The alert to be removed.
     */
    fun removeAlert(alert: Alert) {
        alerts.remove(alert)
        notifyDataSetChanged()
    }


    override fun getItemCount() = alerts.size
}
