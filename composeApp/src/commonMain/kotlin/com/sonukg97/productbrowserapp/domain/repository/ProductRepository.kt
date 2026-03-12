package com.sonukg97.productbrowserapp.domain.repository

import com.sonukg97.productbrowserapp.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(skip: Int = 0, limit: Int = 30): Result<List<Product>>
    suspend fun getProductById(id: Int): Result<Product>
    suspend fun searchProducts(query: String): Result<List<Product>>
    suspend fun getProductsByCategory(category: String): Result<List<Product>>
    suspend fun getCategories(): Result<List<String>>
}
