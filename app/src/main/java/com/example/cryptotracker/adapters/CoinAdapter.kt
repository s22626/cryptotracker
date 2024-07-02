package com.example.cryptotracker.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.R
import com.example.cryptotracker.model.Coin
import com.bumptech.glide.Glide

class CoinAdapter(var coins: List<Coin>):
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    var onRemoveClick: ((Coin) -> Unit)? = null

    class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.coinLogo)
        val name: TextView = itemView.findViewById(R.id.coinName)
        val amount: TextView = itemView.findViewById(R.id.coinAmount)
        val pricePerUnit: TextView = itemView.findViewById(R.id.coinPricePerUnit)
        val value: TextView = itemView.findViewById(R.id.coinTotalValue)
        val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coin_item, parent, false)
        return CoinViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = coins[position]
        holder.name.text = coin.name
        holder.amount.text = "${holder.name.context.getString(R.string.amount)}: ${coin.amount}"
        holder.pricePerUnit.text = "${holder.name.context.getString(R.string.ppu)}: ${String.format("%.2f", coin.pricePerUnit)} $"
        holder.value.text = "${holder.name.context.getString(R.string.value)}: ${String.format("%.2f", coin.totalValue)} $"

        holder.removeButton.setOnClickListener {
            onRemoveClick?.invoke(coin)
        }

        Glide.with(holder.logo.context)
            .load(coin.logoUrl)
            .into(holder.logo)
    }

    override fun getItemCount() = coins.size
}