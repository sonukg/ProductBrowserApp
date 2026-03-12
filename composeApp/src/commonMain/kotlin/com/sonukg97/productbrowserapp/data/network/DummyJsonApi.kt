package com.sonukg97.productbrowserapp.data.network

import com.sonukg97.productbrowserapp.data.network.model.ProductDto
import com.sonukg97.productbrowserapp.data.network.model.ProductResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class DummyJsonApi(private val client: HttpClient) {
    
    companion object {
        private const val BASE_URL = "https://dummyjson.com"
    }

    suspend fun getProducts(skip: Int = 0, limit: Int = 30): ProductResponseDto {
        return client.get("$BASE_URL/products") {
            parameter("skip", skip)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getProductById(id: Int): ProductDto {
        return client.get("$BASE_URL/products/$id").body()
    }

    suspend fun searchProducts(query: String): ProductResponseDto {
        return client.get("$BASE_URL/products/search") {
            parameter("q", query)
        }.body()
    }

    suspend fun getProductsByCategory(category: String): ProductResponseDto {
        return client.get("$BASE_URL/products/category/$category").body()
    }

    suspend fun getCategories(): List<String> {
        return client.get("$BASE_URL/products/category-list").body()
    }
}
