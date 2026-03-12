package com.sonukg97.productbrowserapp.data.repository

import com.sonukg97.productbrowserapp.data.network.DummyJsonApi
import com.sonukg97.productbrowserapp.domain.model.Product
import com.sonukg97.productbrowserapp.domain.repository.ProductRepository
import kotlinx.coroutines.CancellationException

class ProductRepositoryImpl(
    private val api: DummyJsonApi
) : ProductRepository {

    
    private val productCache = mutableMapOf<Int, Product>()
    private var categoriesCache = mutableListOf<String>()

    override suspend fun getProducts(skip: Int, limit: Int): Result<List<Product>> {
        return try {
            val response = api.getProducts(skip, limit)
            val products = response.products.map { it.toDomain() }
            products.forEach { productCache[it.id] = it }
            Result.success(products)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: Int): Result<Product> {
        return try {
            productCache[id]?.let {
                return Result.success(it)
            }
            val response = api.getProductById(id)
            val product = response.toDomain()
            productCache[product.id] = product
            Result.success(product)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val response = api.searchProducts(query)
            val products = response.products.map { it.toDomain() }
            Result.success(products)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    override suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val response = api.getProductsByCategory(category)
            val products = response.products.map { it.toDomain() }
            Result.success(products)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    override suspend fun getCategories(): Result<List<String>> {
        return try {
            if (categoriesCache.isNotEmpty()) {
                return Result.success(categoriesCache)
            }
            val response = api.getCategories()
            categoriesCache.clear()
            categoriesCache.addAll(response)
            Result.success(response)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }
}
