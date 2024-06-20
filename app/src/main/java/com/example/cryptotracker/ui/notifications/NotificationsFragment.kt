package com.example.cryptotracker.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.Alert
import com.example.cryptotracker.AlertAdapter
import com.example.cryptotracker.NotificationsViewModel
import com.example.cryptotracker.PortfolioManager
import com.example.cryptotracker.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NotificationsViewModel(PortfolioManager(requireContext())) as T
            }
        }
    }

    private val alerts = mutableListOf<Alert>()
    private val alertAdapter = AlertAdapter(alerts) { alert ->
        removeAlert(alert)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        binding.coinNameInput.setAdapter(adapter)

        viewModel.coins.observe(viewLifecycleOwner) { coins ->
            val coinNames = coins.map { it.name }
            adapter.clear()
            adapter.addAll(coinNames)
            adapter.notifyDataSetChanged()
        }

        binding.saveTargetButton.setOnClickListener {
            val selectedCoin = binding.coinNameInput.text.toString()
            val targetPrice = binding.targetPriceInput.text.toString().toFloatOrNull()

            if (selectedCoin.isEmpty() || targetPrice == null) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    // Fetch the current price for the selected coin
                    val currentPrice = viewModel.getCurrentPrice(selectedCoin)
                    withContext(Dispatchers.Main) {
                        if (currentPrice != null) {
                            val isAbove = targetPrice > currentPrice
                            viewModel.saveTargetPrice(selectedCoin, targetPrice, isAbove)
                            Toast.makeText(context, "Target saved successfully", Toast.LENGTH_SHORT).show()
                            alerts.add(Alert(selectedCoin, targetPrice, isAbove))
                            alertAdapter.notifyItemInserted(alerts.size - 1)
                        } else {
                            Toast.makeText(context, "Error fetching current price for $selectedCoin", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.alertsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.alertsRecyclerView.adapter = alertAdapter

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getSavedAlerts().collect { savedAlerts ->
                withContext(Dispatchers.Main) {
                    alerts.clear()
                    alerts.addAll(savedAlerts.map { Alert(it.first, it.second ?: 0f, it.third ?: true) })
                    alertAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun removeAlert(alert: Alert) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.removeTargetPrice(alert.coin)
            withContext(Dispatchers.Main) {
                val position = alerts.indexOf(alert)
                alerts.remove(alert)
                alertAdapter.notifyItemRemoved(position)
                Toast.makeText(context, "Alert removed successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
