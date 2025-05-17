package com.skincare.apitest.network

import com.skincare.apitest.model.ProductResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>

    @GET("product/image/{id}")
    suspend fun getProductImage(@Path("id") productId: Int): Response<ResponseBody>

    companion object {
        const val BASE_URL = "http://localhost:5432/" // Update with your actual base URL
    }
}

// Retrofit client provider
object RetrofitClient {
    private val okHttpClient = okhttp3.OkHttpClient.Builder()
        .addInterceptor(okhttp3.logging.HttpLoggingInterceptor().apply {
            level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofit: retrofit2.Retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(ProductService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()

    val productService: ProductService = retrofit.create(ProductService::class.java)
}

// Apollo client provider
object ApolloClientProvider {
    private const val SERVER_URL = "http://localhost:5432/graphql" // Update with your actual GraphQL endpoint

    val apolloClient = com.apollographql.apollo3.ApolloClient.Builder()
        .serverUrl(SERVER_URL)
        .build()
}
