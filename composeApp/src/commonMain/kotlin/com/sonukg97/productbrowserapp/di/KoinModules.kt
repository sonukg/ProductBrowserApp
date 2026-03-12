package com.sonukg97.productbrowserapp.di

import com.sonukg97.productbrowserapp.data.network.DummyJsonApi
import com.sonukg97.productbrowserapp.data.repository.ProductRepositoryImpl
import com.sonukg97.productbrowserapp.domain.repository.ProductRepository
import com.sonukg97.productbrowserapp.domain.usecase.GetCategoriesUseCase
import com.sonukg97.productbrowserapp.domain.usecase.GetProductDetailUseCase
import com.sonukg97.productbrowserapp.domain.usecase.GetProductsUseCase
import com.sonukg97.productbrowserapp.domain.usecase.SearchProductsUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import com.sonukg97.productbrowserapp.presentation.screens.detail.ProductDetailViewModel
import com.sonukg97.productbrowserapp.presentation.screens.list.ProductListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf


val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Ktor: $message")
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    single { DummyJsonApi(get()) }
}

val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
}
val useCaseModule = module {
    factoryOf(::GetProductsUseCase)
    factoryOf(::GetProductDetailUseCase)
    factoryOf(::SearchProductsUseCase)
    factoryOf(::GetCategoriesUseCase)
}

val viewModelModule = module {
    viewModelOf(::ProductListViewModel)
    viewModelOf(::ProductDetailViewModel)
}

val sharedModules = listOf(networkModule, repositoryModule, useCaseModule, viewModelModule)
