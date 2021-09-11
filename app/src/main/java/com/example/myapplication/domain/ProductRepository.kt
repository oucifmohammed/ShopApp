package com.example.myapplication.domain

import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun searchProductsByName(name: String): Resource<List<Product>>

    suspend fun searchProductsByCategory(category: String,name: String?): Resource<List<Product>>

    suspend fun displayProductDetails(id: String): Resource<Product>

    suspend fun toggleLikeButton(product: Product): Resource<Boolean>

    suspend fun getFavoriteProductsIds(): Flow<List<String>>

    suspend fun getFavoriteProducts(): Flow<Resource<List<Product>>>

    suspend fun addProductToRecentList(productId: String)

    suspend fun listenToRecentProductAddition(): Flow<Resource<List<Product>>>

    suspend fun listenToProductPromotions(): Flow<Resource<List<Product>>>

    suspend fun addToCartProducts(cartProduct: CartProduct): ProcessUiState

    suspend fun deleteFromCartProducts(cartProductId: String): ProcessUiState

    suspend fun listenToCartProducts(): Flow<Resource<List<CartProduct>>>
}