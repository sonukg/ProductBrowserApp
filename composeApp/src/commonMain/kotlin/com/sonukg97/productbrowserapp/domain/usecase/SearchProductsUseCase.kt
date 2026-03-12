package com.sonukg97.productbrowserapp.domain.usecase

import com.sonukg97.productbrowserapp.domain.model.Product
import com.sonukg97.productbrowserapp.domain.repository.ProductRepository

class SearchProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(query: String): Result<List<Product>> {
        return repository.searchProducts(query)
    }
}
