package com.sonukg97.productbrowserapp.domain.usecase

import com.sonukg97.productbrowserapp.domain.model.Product
import com.sonukg97.productbrowserapp.domain.repository.ProductRepository

class GetProductDetailUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(id: Int): Result<Product> {
        return repository.getProductById(id)
    }
}
