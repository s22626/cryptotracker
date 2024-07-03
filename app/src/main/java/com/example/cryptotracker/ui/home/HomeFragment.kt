package com.example.cryptotracker.ui.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.CurrencyService
import com.example.cryptotracker.PortfolioManager
import com.example.cryptotracker.R
import com.example.cryptotracker.adapters.CoinAdapter
import com.example.cryptotracker.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: CoinAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var portfolioManager: PortfolioManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        portfolioManager = PortfolioManager(requireContext())
        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(portfolioManager, CurrencyService()))[HomeViewModel::class.java]
        return binding.root
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = CoinAdapter(listOf(), homeViewModel.rate)
        recyclerView.adapter = adapter

        adapter.onRemoveClick = { coin ->
            homeViewModel.removeCoin(coin.name)
        }

        homeViewModel.coins.observe(viewLifecycleOwner) { coins ->
            adapter.coins = coins
            adapter.notifyDataSetChanged()
        }

        val currentLocale = resources.configuration.locales[0]

        homeViewModel.totalValue.observe(viewLifecycleOwner) { totalValue ->
            if (currentLocale.language == "pl")
                binding.totalValue.text = getString(R.string.total_value) + ": ${String.format(Locale.getDefault(), "%.2f", totalValue * homeViewModel.rate)} PLN"
            else
                binding.totalValue.text = getString(R.string.total_value) + ": $totalValue $"
        }

        binding.languageSwitch.isChecked = currentLocale.language == "pl"

        binding.languageSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setLocale("pl")
            } else {
                setLocale("en")
            }
        }
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        requireContext().createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Restart the activity to apply the language change
        activity?.recreate()
    }
}
