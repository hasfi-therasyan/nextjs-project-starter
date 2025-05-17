package com.skincare.apitest

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.skincare.apitest.databinding.ActivityMainBinding
import com.skincare.apitest.model.ApiResponse
import com.skincare.apitest.ui.ProductAdapter
import com.skincare.apitest.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProductViewModel by viewModels()
    private val productAdapter = ProductAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        // Setup RecyclerView
        binding.productsRecyclerView.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        // Setup Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.api_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.apiSelectionSpinner.adapter = adapter
        }

        // Spinner selection listener
        binding.apiSelectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val apiType = if (position == 0) {
                    ProductViewModel.ApiType.RETROFIT
                } else {
                    ProductViewModel.ApiType.GRAPHQL
                }
                viewModel.setApiType(apiType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Fetch button click listener
        binding.fetchProductsButton.setOnClickListener {
            viewModel.fetchProducts()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productsState.collect { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.productsRecyclerView.visibility = View.GONE
                        }
                        is ApiResponse.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.productsRecyclerView.visibility = View.VISIBLE
                            productAdapter.submitList(response.data)
                        }
                        is ApiResponse.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.productsRecyclerView.visibility = View.GONE
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.error_loading_products),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}
