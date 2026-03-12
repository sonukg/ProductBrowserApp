package com.sonukg97.productbrowserapp.domain.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val brand: String?,
    val category: String,
    val thumbnail: String,
    val images: List<String>? = emptyList()
)
