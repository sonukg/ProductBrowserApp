package com.sonukg97.productbrowserapp.domain.usecase

import com.sonukg97.productbrowserapp.domain.model.Product
import com.sonukg97.productbrowserapp.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(category: String? = null, skip: Int = 0, limit: Int = 30): Result<List<Product>> {
        return if (category.isNullOrEmpty()) {
            repository.getProducts(skip, limit)
        } else {
            repository.getProductsByCategory(category)
        }
    }
}
