package com.example.myapplication.data

import android.net.Uri
import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.Constants.DEFAULT_USER_IMAGE
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import java.util.*

@ExperimentalCoroutinesApi
class FakeRepository : Repository {

    val usersList = mutableListOf<User>()

    val productsList = mutableListOf<Product>()

    private fun feedTheSource() {

        usersList.add(
            User(
                "1",
                "Oucif Mohammed",
                "oucifmohamed8@gmail.com",
                "hello1234",
                photoUrl = DEFAULT_USER_IMAGE,
                favoriteProducts = listOf("1","2"),
                recentProducts = listOf("1","2"),
                cartProducts = listOf("2")
            )
        )

        usersList.add(
            User(
                "2",
                "Oucif Zakaria",
                "oucifzakaria@gmail.com",
                "hello123",
                photoUrl = DEFAULT_USER_IMAGE,
                favoriteProducts = listOf(),
                recentProducts = listOf(),
                cartProducts = listOf()
            )
        )

        productsList.add(
            Product(
                id = "1",
                name = "pants",
                image = DEFAULT_USER_IMAGE,
                category = "Women",
                originalPrice = 2000f,
                promotionPrice = 1000f
            )
        )

        productsList.add(
            Product(
                id = "2",
                name = "pants2",
                image = DEFAULT_USER_IMAGE,
                category = "Women",
                originalPrice = 2000f,
                promotionPrice = 0f
            )
        )

        productsList.add(
            Product(
                id = "3",
                name = "t-shirt",
                image = DEFAULT_USER_IMAGE,
                category = "Men",
                originalPrice = 2500f,
                promotionPrice = 2100f
            )
        )

        productsList.add(
            Product(
                id = "4",
                name = "t-shirt2",
                image = DEFAULT_USER_IMAGE,
                category = "Men",
                originalPrice = 2500f,
                promotionPrice = 0f
            )
        )
    }

    init {
        feedTheSource()
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): ProcessUiState {

        val result = usersList.find { user ->
            user.email == email
        }

        result?.let {
            return ProcessUiState.Error("This account already exists")
        }

        val user = User(
            id = UUID.randomUUID().toString(),
            userName = username,
            password = password,
            email = "",
            photoUrl = DEFAULT_USER_IMAGE,
            favoriteProducts = listOf(),
            recentProducts = listOf(),
            cartProducts = listOf()
        )
        usersList.add(user)

        return ProcessUiState.Success("Registration completed successfully")
    }

    override suspend fun login(email: String, password: String): ProcessUiState {

        val result = usersList.find { user ->
            user.email == email && user.password == password
        }

        return result?.let { ProcessUiState.Success("logged in successfully") }
            ?: ProcessUiState.Error("You don't have an account yet")
    }

    override suspend fun searchProductsByName(name: String): Resource<List<Product>> {
        val result = productsList.filter { product ->
            product.name.equals(name, ignoreCase = true)
        }

        return if(result.isEmpty())
            Resource.error("There is no product with that name", null)
        else
            Resource.success(result)
    }

    override suspend fun editProfile(username: String, email: String, uri: Uri?): ProcessUiState {
        usersList[0].userName = "Mohamed"

        return ProcessUiState.Success()
    }

    override suspend fun getUserAccount(): User {
        return usersList[0]
    }

    override suspend fun logOut() {
        TODO("Not yet implemented")
    }

    override suspend fun searchProductsByCategory(
        category: String,
        name: String?
    ): Resource<List<Product>> {
        val result = productsList.filter { product ->
            product.category == category
        }

        return if(result.isEmpty())
            Resource.error("We don't have products in this category yet",null)
        else
            Resource.success(result)
    }

    override suspend fun displayProductDetails(id: String): Resource<Product> {

        val result = productsList.find { product ->
            product.id == id
        }

        return Resource.success(result!!)
    }

    override suspend fun toggleLikeButton(product: Product): Resource<Boolean> {

        return if(usersList[0].favoriteProducts.contains(product.id)) {
            usersList[0].favoriteProducts.minus(product.id)
            Resource.success(false)
        } else {
            usersList[0].favoriteProducts.plus(product.id)
            Resource.success(true)
        }

    }

    override suspend fun getFavoriteProductsIds(): Flow<List<String>> = callbackFlow {
        offer(usersList[0].favoriteProducts)

        awaitClose { this.close() }
    }

    override suspend fun getFavoriteProducts(): Flow<Resource<List<Product>>> = callbackFlow{

        val result = productsList.filter {
            it.id in usersList[0].favoriteProducts
        }

        offer(Resource.success(result))

        awaitClose { this.close() }
    }

    override suspend fun addProductToRecentList(productId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun listenToRecentProductAddition(): Flow<Resource<List<Product>>> = callbackFlow{
        val result = productsList.filter {
            it.id in usersList[0].recentProducts
        }

        offer(Resource.success(result))

        awaitClose { this.close() }
    }

    override suspend fun listenToProductPromotions(): Flow<Resource<List<Product>>> = callbackFlow{
        val result = productsList.filter {
            it.promotionPrice != 0f
        }

        offer(Resource.success(result))

        awaitClose { this.close() }
    }

    override suspend fun addToCartProducts(productId: String): ProcessUiState {
        usersList[0].cartProducts.plus(productId)

        return ProcessUiState.Success()
    }

    override suspend fun deleteFromCartProducts(productId: String): ProcessUiState {
        usersList[0].cartProducts.minus(productId)

        return ProcessUiState.Success()
    }

    override suspend fun listenToCartProducts(): Flow<Resource<List<Product>>> = callbackFlow{

        val result = productsList.filter {
            it.id in usersList[0].cartProducts
        }

        offer(Resource.success(result))

        awaitClose { this.close() }
    }
}