package com.sonukg97.productbrowserapp.presentation.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonukg97.productbrowserapp.domain.model.Product
import com.sonukg97.productbrowserapp.domain.usecase.GetCategoriesUseCase
import com.sonukg97.productbrowserapp.domain.usecase.GetProductsUseCase
import com.sonukg97.productbrowserapp.domain.usecase.SearchProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductListUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String? = null
)

class ProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            // Load categories
            getCategoriesUseCase().onSuccess { cats ->
                _uiState.update { it.copy(categories = cats) }
            }
            
            // Load initial products
            loadProducts()
        }
    }

    private suspend fun loadProducts(category: String? = null) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        getProductsUseCase(category = category).fold(
            onSuccess = { products ->
                _uiState.update { it.copy(isLoading = false, products = products) }
            },
            onFailure = { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message) }
            }
        )
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query, selectedCategory = null) }
        if (query.length >= 3) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, error = null) }
                searchProductsUseCase(query).fold(
                    onSuccess = { products ->
                        _uiState.update { it.copy(isLoading = false, products = products) }
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(isLoading = false, error = error.message) }
                    }
                )
            }
        } else if (query.isEmpty()) {
            viewModelScope.launch { loadProducts() }
        }
    }

    fun onCategorySelected(category: String?) {
        _uiState.update { it.copy(selectedCategory = category, searchQuery = "") }
        viewModelScope.launch {
            loadProducts(category)
        }
    }
}
