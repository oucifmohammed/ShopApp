package com.example.myapplication.domain

import android.net.Uri
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun register(
        email: String,
        username: String,
        password: String
    ): ProcessUiState

    suspend fun login(
        email: String,
        password: String
    ): ProcessUiState

    suspend fun searchProductsByName(name: String): Resource<List<Product>>

    suspend fun editProfile(username: String, email: String, uri: Uri?): ProcessUiState

    suspend fun getUserAccount(): User

    suspend fun logOut()

    suspend fun searchProductsByCategory(category: String,name: String?): Resource<List<Product>>

    suspend fun displayProductDetails(id: String): Resource<Product>

    suspend fun toggleLikeButton(product: Product): Resource<Boolean>

    suspend fun getFavoriteProductsIds(): Flow<List<String>>

    suspend fun getFavoriteProducts(): Flow<Resource<List<Product>>>

    suspend fun addProductToRecentList(productId: String)

    suspend fun listenToRecentProductAddition(): Flow<Resource<List<Product>>>

    suspend fun listenToProductPromotions(): Flow<Resource<List<Product>>>

    suspend fun addToCartProducts(productId: String): ProcessUiState

    suspend fun deleteFromCartProducts(productId: String): ProcessUiState

    suspend fun listenToCartProducts(): Flow<Resource<List<Product>>>
}