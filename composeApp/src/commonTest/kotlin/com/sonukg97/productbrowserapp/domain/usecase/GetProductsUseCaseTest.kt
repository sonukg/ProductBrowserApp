package com.sonukg97.productbrowserapp.domain.usecase

import com.sonukg97.productbrowserapp.domain.model.Product
import com.sonukg97.productbrowserapp.domain.repository.ProductRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetProductsUseCaseTest {

    private val mockProducts = listOf(
        Product(1, "Prod1", "Desc1", 10.0, 4.0, "Brand1", "cat1", "thumb1", emptyList()),
        Product(2, "Prod2", "Desc2", 20.0, 5.0, "Brand2", "cat2", "thumb2", emptyList())
    )

    private val mockRepository = object : ProductRepository {
        var getProductsCalled = false
        var getProductsByCategoryCalled = false
        var lastCategoryRequested: String? = null

        override suspend fun getProducts(skip: Int, limit: Int): Result<List<Product>> {
            getProductsCalled = true
            return Result.success(mockProducts)
        }

        override suspend fun getProductById(id: Int): Result<Product> {
            return Result.success(mockProducts.first())
        }

        override suspend fun searchProducts(query: String): Result<List<Product>> {
            return Result.success(emptyList())
        }

        override suspend fun getProductsByCategory(category: String): Result<List<Product>> {
            getProductsByCategoryCalled = true
            lastCategoryRequested = category
            return Result.success(mockProducts.filter { it.category == category })
        }

        override suspend fun getCategories(): Result<List<String>> {
            return Result.success(listOf("cat1", "cat2"))
        }
    }

    private val useCase = GetProductsUseCase(mockRepository)

    @Test
    fun testGetProductsWithoutCategory_callsGetProducts() = runTest {
        val result = useCase()
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertTrue(mockRepository.getProductsCalled)
    }

    @Test
    fun testGetProductsWithCategory_callsGetProductsByCategory() = runTest {
        val result = useCase("cat1")
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertTrue(mockRepository.getProductsByCategoryCalled)
        assertEquals("cat1", mockRepository.lastCategoryRequested)
    }
}
