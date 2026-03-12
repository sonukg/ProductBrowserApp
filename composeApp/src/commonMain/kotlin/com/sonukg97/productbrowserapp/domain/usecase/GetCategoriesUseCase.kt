package com.sonukg97.productbrowserapp.domain.usecase

import com.sonukg97.productbrowserapp.domain.repository.ProductRepository

class GetCategoriesUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(): Result<List<String>> {
        return repository.getCategories()
    }
}
