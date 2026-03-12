package com.sonukg97.productbrowserapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.sonukg97.productbrowserapp.presentation.screens.detail.ProductDetailScreen
import com.sonukg97.productbrowserapp.presentation.screens.list.ProductListScreen
import kotlinx.serialization.Serializable
import org.koin.compose.KoinContext

@Serializable
object ProductListRoute

@Serializable
data class ProductDetailRoute(val id: Int)

@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .crossfade(true)
            .build()
    }

    MaterialTheme {
        org.koin.compose.KoinApplication(application = {
            modules(com.sonukg97.productbrowserapp.di.sharedModules)
        }) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = ProductListRoute
            ) {
                composable<ProductListRoute> {
                    ProductListScreen(
                        onProductClick = { productId ->
                            navController.navigate(ProductDetailRoute(id = productId))
                        }
                    )
                }
                composable<ProductDetailRoute> { backStackEntry ->
                    val detailRoute: ProductDetailRoute = backStackEntry.toRoute()
                    ProductDetailScreen(
                        productId = detailRoute.id,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}