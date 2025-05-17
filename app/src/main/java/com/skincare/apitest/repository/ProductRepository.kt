package com.skincare.apitest.repository

import com.skincare.apitest.GetProductsQuery
import com.skincare.apitest.model.ApiResponse
import com.skincare.apitest.model.Product
import com.skincare.apitest.network.ApolloClientProvider
import com.skincare.apitest.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRepository {
    private val productService = RetrofitClient.productService
    private val apolloClient = ApolloClientProvider.apolloClient

    // Fetch products using Retrofit (REST)
    fun getProductsRest(): Flow<ApiResponse<List<Product>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = productService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    emit(ApiResponse.Success(productResponse.products))
                } ?: emit(ApiResponse.Error("Empty response body"))
            } else {
                emit(ApiResponse.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    // Fetch products using Apollo (GraphQL)
    fun getProductsGraphQL(): Flow<ApiResponse<List<Product>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = apolloClient.query(GetProductsQuery()).execute()
            if (response.hasErrors()) {
                emit(ApiResponse.Error(response.errors?.firstOrNull()?.message ?: "GraphQL Error"))
            } else {
                val products = response.data?.products?.map { product ->
                    Product(
                        id = product.id,
                        productName = product.productName,
                        description = product.description,
                        price = product.price,
                        imageUrl = product.imageUrl
                    )
                } ?: emptyList()
                emit(ApiResponse.Success(products))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    // Get product image using Retrofit
    suspend fun getProductImageRest(productId: Int): ApiResponse<ByteArray> {
        return try {
            val response = productService.getProductImage(productId)
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    ApiResponse.Success(responseBody.bytes())
                } ?: ApiResponse.Error("Empty image response")
            } else {
                ApiResponse.Error("Error fetching image: ${response.code()}")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Unknown error occurred")
        }
    }

    // Get product image using GraphQL
    suspend fun getProductImageGraphQL(productId: Int): ApiResponse<ByteArray> {
        return try {
            val response = apolloClient.query(GetProductsQuery()).execute()
            if (response.hasErrors()) {
                ApiResponse.Error(response.errors?.firstOrNull()?.message ?: "GraphQL Error")
            } else {
                // Implementation depends on how the image is returned from GraphQL
                // This is a placeholder implementation
                ApiResponse.Error("GraphQL image fetching not implemented")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Unknown error occurred")
        }
    }
}
