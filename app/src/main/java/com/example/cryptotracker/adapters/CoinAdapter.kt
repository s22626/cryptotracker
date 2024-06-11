package com.example.cryptotracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.R
import com.example.cryptotracker.model.Coin
import com.bumptech.glide.Glide

class CoinAdapter(var coins: List<Coin>) :
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    class CoinViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coin_item, parent, false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = coins[position]
        val imageView: ImageView = holder.view.findViewById(R.id.coinLogo)
        val nameTextView: TextView = holder.view.findViewById(R.id.coinName)
        val amountTextView: TextView = holder.view.findViewById(R.id.coinAmount)
        val pricePerUnitTextView: TextView = holder.view.findViewById(R.id.coinPricePerUnit)
        val totalValueTextView: TextView = holder.view.findViewById(R.id.coinTotalValue)

        // Load the image from coin.logoUrl into imageView
        Glide.with(holder.view.context)
            .load(coin.logoUrl)
            .into(imageView)

        nameTextView.text = coin.name
        amountTextView.text = coin.amount.toString()
        pricePerUnitTextView.text = coin.pricePerUnit.toString()
        totalValueTextView.text = coin.totalValue.toString()
    }

    override fun getItemCount() = coins.size
}