package com.example.cryptotracker.ui.home

import com.example.cryptotracker.PortfolioManager
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.R
import com.example.cryptotracker.adapters.CoinAdapter
import com.example.cryptotracker.databinding.FragmentHomeBinding

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
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(portfolioManager)).get(HomeViewModel::class.java)
        return binding.root
    }
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = CoinAdapter(listOf())
        recyclerView.adapter = adapter

        adapter.onRemoveClick = { coin ->
            homeViewModel.removeCoin(coin.name)
        }

        homeViewModel.coins.observe(viewLifecycleOwner) { coins ->
            adapter.coins = coins
            adapter.notifyDataSetChanged()
        }

        homeViewModel.totalValue.observe(viewLifecycleOwner) { totalValue ->
            binding.totalValue.text = "Total Value: $totalValue USD"
        }
    }
}