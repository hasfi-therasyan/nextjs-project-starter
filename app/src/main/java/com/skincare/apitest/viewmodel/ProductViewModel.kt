package com.skincare.apitest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skincare.apitest.model.ApiResponse
import com.skincare.apitest.model.Product
import com.skincare.apitest.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _productsState = MutableStateFlow<ApiResponse<List<Product>>>(ApiResponse.Loading)
    val productsState: StateFlow<ApiResponse<List<Product>>> = _productsState

    private val _selectedApi = MutableStateFlow(ApiType.RETROFIT)
    val selectedApi: StateFlow<ApiType> = _selectedApi

    fun setApiType(type: ApiType) {
        _selectedApi.value = type
    }

    fun fetchProducts() {
        viewModelScope.launch {
            when (selectedApi.value) {
                ApiType.RETROFIT -> {
                    repository.getProductsRest().collect { response ->
                        _productsState.value = response
                    }
                }
                ApiType.GRAPHQL -> {
                    repository.getProductsGraphQL().collect { response ->
                        _productsState.value = response
                    }
                }
            }
        }
    }

    suspend fun getProductImage(productId: Int): ApiResponse<ByteArray> {
        return when (selectedApi.value) {
            ApiType.RETROFIT -> repository.getProductImageRest(productId)
            ApiType.GRAPHQL -> repository.getProductImageGraphQL(productId)
        }
    }

    enum class ApiType {
        RETROFIT,
        GRAPHQL
    }
}
