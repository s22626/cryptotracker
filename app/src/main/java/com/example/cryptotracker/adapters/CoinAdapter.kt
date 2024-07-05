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

/**
 * Adapter for the RecyclerView that displays the list of coins.
 * @property coins List of coins to be displayed.
 * @property rate The exchange rate between USD and PLN.
 * @property onRemoveClick Lambda function that is invoked when the remove button is clicked.
 */
class CoinAdapter(var coins: List<Coin>, var rate: Double):
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    var onRemoveClick: ((Coin) -> Unit)? = null

    /**
     * ViewHolder for the RecyclerView items.
     * @property logo ImageView that displays the coin logo.
     * @property name TextView that displays the coin name.
     * @property amount TextView that displays the coin amount.
     * @property pricePerUnit TextView that displays the coin price per unit.
     * @property value TextView that displays the total value of the coin.
     * @property removeButton ImageButton that removes the coin from the list when clicked.
     */
    class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.coinLogo)
        val name: TextView = itemView.findViewById(R.id.coinName)
        val amount: TextView = itemView.findViewById(R.id.coinAmount)
        val pricePerUnit: TextView = itemView.findViewById(R.id.coinPricePerUnit)
        val value: TextView = itemView.findViewById(R.id.coinTotalValue)
        val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)
    }

    /**
     * Inflates the layout for the RecyclerView items.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coin_item, parent, false)
        return CoinViewHolder(view)
    }

    /**
     * Binds the data to the RecyclerView items.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = coins[position]
        holder.name.text = coin.name
        holder.amount.text = "${holder.name.context.getString(R.string.amount)}: ${coin.amount}"

        val currentLocale = holder.name.context.resources.configuration.locales[0]

        if (currentLocale.language == "pl") {
            holder.pricePerUnit.text = "${holder.name.context.getString(R.string.ppu)}: ${String.format("%.2f", coin.pricePerUnit * rate)} PLN"
            holder.value.text = "${holder.name.context.getString(R.string.value)}: ${String.format("%.2f", coin.totalValue * rate)} PLN"
        } else {
            holder.pricePerUnit.text = "${holder.name.context.getString(R.string.ppu)}: ${String.format("%.2f", coin.pricePerUnit)} $"
            holder.value.text = "${holder.name.context.getString(R.string.value)}: ${String.format("%.2f", coin.totalValue)} $"
        }

        holder.removeButton.setOnClickListener {
            onRemoveClick?.invoke(coin)
        }

        Glide.with(holder.logo.context)
            .load(coin.logoUrl)
            .into(holder.logo)
    }

    /**
     * Returns the size of the coin list.
     */
    override fun getItemCount() = coins.size
}