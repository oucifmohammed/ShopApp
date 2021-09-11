package com.example.myapplication.data

import com.example.myapplication.domain.ProductRepository
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.Constants.DEFAULT_USER_IMAGE
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class ProductFakeRepository : ProductRepository {

    private val usersList = mutableListOf<User>()

    val productsList = mutableListOf<Product>()

    val cartProductList = mutableListOf<CartProduct>()

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
                id = "2",
                userName = "Oucif Zakaria",
                email = "oucifzakaria@gmail.com",
                password = "hello123",
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
                promotionPrice = 1000f,
                sizes = listOf("S","M","L")
            )
        )

        productsList.add(
            Product(
                id = "2",
                name = "pants2",
                image = DEFAULT_USER_IMAGE,
                category = "Women",
                originalPrice = 2000f,
                promotionPrice = 0f,
                sizes = listOf("S","M","L")
            )
        )

        productsList.add(
            Product(
                id = "3",
                name = "t-shirt",
                image = DEFAULT_USER_IMAGE,
                category = "Men",
                originalPrice = 2500f,
                promotionPrice = 2100f,
                sizes = listOf("S","M","L","XL")
            )
        )

        productsList.add(
            Product(
                id = "4",
                name = "t-shirt2",
                image = DEFAULT_USER_IMAGE,
                category = "Men",
                originalPrice = 2500f,
                promotionPrice = 0f,
                sizes = listOf("S","M","L","XL")
            )
        )

        cartProductList.add(
            CartProduct(
                id = "1",
                parentProductId = "1",
                imageUrl = DEFAULT_USER_IMAGE,
                name = "T-shirt",
                price = 2500.0,
                size = "M"
            )
        )

        cartProductList.add(
            CartProduct(
                id = "2",
                parentProductId = "1",
                imageUrl = DEFAULT_USER_IMAGE,
                name = "Pants",
                price = 1000.0,
                size = "M"
            )
        )

    }

    init {
        feedTheSource()
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

    override suspend fun addToCartProducts(cartProduct: CartProduct): ProcessUiState {
        cartProductList.add(cartProduct)

        return ProcessUiState.Success()
    }

    override suspend fun deleteFromCartProducts(cartProductId: String): ProcessUiState {
        cartProductList.removeIf {
            it.id == cartProductId
        }

        return ProcessUiState.Success()
    }

    override suspend fun listenToCartProducts(): Flow<Resource<List<CartProduct>>> = callbackFlow{

        offer(Resource.success(cartProductList))

        awaitClose { this.close() }
    }
}