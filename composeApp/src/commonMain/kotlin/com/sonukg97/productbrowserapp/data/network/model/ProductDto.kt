package com.sonukg97.productbrowserapp.data.network.model

import com.sonukg97.productbrowserapp.domain.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val brand: String? = null,
    val category: String,
    val thumbnail: String,
    val images: List<String>? = emptyList()
) {
    fun toDomain(): Product {
        return Product(
            id = id,
            title = title,
            description = description,
            price = price,
            rating = rating,
            brand = brand,
            category = category,
            thumbnail = thumbnail,
            images = images
        )
    }
}
