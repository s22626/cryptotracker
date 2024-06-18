package com.example.cryptotracker.ui.dashboard

import com.example.cryptotracker.PortfolioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cryptotracker.databinding.FragmentDashboardBinding
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DashboardViewModel(PortfolioManager(requireContext())) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        binding.searchBar.setAdapter(adapter)

        viewModel.coins.observe(viewLifecycleOwner) { coins ->
            val coinNames = coins.map { it.name }
            adapter.clear()
            adapter.addAll(coinNames)
            adapter.notifyDataSetChanged()
        }

        binding.saveButton.setOnClickListener {
            val selectedCoin = binding.searchBar.text.toString()
            val amount = binding.amountInput.text.toString().toFloatOrNull()

            if (selectedCoin.isEmpty() || amount == null) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.saveCoinAmount(selectedCoin, amount)
                Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}