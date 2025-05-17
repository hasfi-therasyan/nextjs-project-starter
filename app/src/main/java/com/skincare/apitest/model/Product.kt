package com.skincare.apitest.model

data class Product(
    val id: Int,
    val productName: String,
    val description: String,
    val price: Double,
    val imageUrl: String? = null // For storing the URL to fetch the image
) {
    fun getFormattedPrice(): String = String.format("$%.2f", price)
}

// Wrapper class for API response
sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}

// REST API Response model
data class ProductResponse(
    val products: List<Product>
)

// GraphQL Response model (matches schema)
data class GraphQLProduct(
    val id: Int,
    val productName: String,
    val description: String,
    val price: Double,
    val imageUrl: String?
) {
    fun toProduct(): Product = Product(
        id = id,
        productName = productName,
        description = description,
        price = price,
        imageUrl = imageUrl
    )
}
