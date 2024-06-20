package com.example.cryptotracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Alert(val coin: String, val targetPrice: Float,  val isAbove: Boolean)

class AlertAdapter(
    private val alerts: MutableList<Alert>,
    private val onRemoveClick: (Alert) -> Unit
) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    class AlertViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coinTextView: TextView = view.findViewById(R.id.coin_text_view)
        val targetPriceTextView: TextView = view.findViewById(R.id.target_price_text_view)
        val removeButton: Button = view.findViewById(R.id.remove_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alert_item, parent, false)
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = alerts[position]
        holder.coinTextView.text = alert.coin
        holder.targetPriceTextView.text = alert.targetPrice.toString()
        holder.removeButton.setOnClickListener {
            onRemoveClick(alert)
        }
    }

    fun removeAlert(alert: Alert) {
        alerts.remove(alert)
        notifyDataSetChanged()
    }


    override fun getItemCount() = alerts.size
}
