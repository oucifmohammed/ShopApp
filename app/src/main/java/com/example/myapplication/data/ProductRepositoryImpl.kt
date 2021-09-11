package com.example.myapplication.data

import android.util.Log
import com.example.myapplication.data.models.*
import com.example.myapplication.data.util.CartProductDtoMapper
import com.example.myapplication.data.util.ProductDtoMapper
import com.example.myapplication.domain.ProductRepository
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Constants.TOPIC
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productMapper: ProductDtoMapper,
    private val cartProductDtoMapper: CartProductDtoMapper,
    private val api: NotificationApi
) : ProductRepository {

    private val auth = FirebaseAuth.getInstance()
    private val users = FirebaseFirestore.getInstance().collection("Users")
    private val products = FirebaseFirestore.getInstance().collection("products")
    private val userCartProducts =
        users.document(auth.currentUser?.uid!!).collection("cartProducts")

    override suspend fun searchProductsByName(name: String): Resource<List<Product>> {
        return try {
            withContext(Dispatchers.IO) {
                val querySnapshot = products.whereGreaterThanOrEqualTo("name", name)
                    .whereLessThanOrEqualTo("name", name + '\uf8ff').get().await()

                if (!querySnapshot.isEmpty) {
                    withContext(Dispatchers.Default) {
                        val list = mutableListOf<ProductDto>()

                        for (document in querySnapshot) {
                            list.add(document.toObject(ProductDto::class.java))
                        }

                        Resource.success(productMapper.toDomainList(list))
                    }
                } else {
                    Resource.error("There is no product with that name", null)
                }
            }
        } catch (e: Exception) {
            Resource.error(message = e.message!!, data = null)
        }
    }

    override suspend fun searchProductsByCategory(
        category: String,
        name: String?
    ): Resource<List<Product>> {
        return try {
            withContext(Dispatchers.IO) {

                val querySnapshot = products.whereEqualTo("category", category).get().await()

                if (!querySnapshot.isEmpty) {
                    withContext(Dispatchers.Default) {
                        val list = mutableListOf<ProductDto>()

                        for (document in querySnapshot) {
                            list.add(document.toObject(ProductDto::class.java))
                        }

                        val result = name?.let {
                            list.filter {
                                it.name == name
                            }
                        }

                        Resource.success(productMapper.toDomainList(result ?: list))
                    }
                } else {
                    Resource.error("We don't have product for this category yet", null)
                }
            }
        } catch (e: Exception) {
            Resource.error(message = e.message!!, data = null)
        }
    }

    override suspend fun displayProductDetails(id: String): Resource<Product> {
        return try {
            withContext(Dispatchers.IO) {
                val product = products.document(id).get().await().toObject(ProductDto::class.java)
                Resource.success(productMapper.mapToDomainModel(product!!))

            }
        } catch (e: Exception) {
            Resource.error(message = e.message!!, data = null)
        }
    }

    override suspend fun toggleLikeButton(product: Product) = withContext(Dispatchers.IO) {

        try {
            var isLiked = false

            val productId = productMapper.mapFromDomainModel(product).id
            val user = users.document(auth.currentUser?.uid!!)
            val userFavorites =
                users.document(auth.currentUser?.uid!!).get().await()
                    .toObject(UserDto::class.java)?.favoriteProducts ?: listOf()


            userFavorites - productId
            user.update(
                "favoriteProducts", if (productId in userFavorites) userFavorites - productId else {
                    isLiked = true
                    userFavorites + productId
                }
            ).await()

            Resource.success(isLiked)

        } catch (e: Exception) {
            Resource.error(e.message!!, null)
        }

    }

    @ExperimentalCoroutinesApi
    override suspend fun getFavoriteProductsIds(): Flow<List<String>> = callbackFlow {
        val user = users.document(auth.currentUser?.uid!!)

        val subscription = user.addSnapshotListener { snapshot, _ ->

            if (snapshot!!.exists()) {
                val favoriteProductsIds = snapshot.get("favoriteProducts") as List<String>
                offer(favoriteProductsIds)
            }
        }

        awaitClose { subscription.remove() }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getFavoriteProducts(): Flow<Resource<List<Product>>> = callbackFlow {
//        try {
        val user = users.document(auth.currentUser?.uid!!)

        val subscription = user.addSnapshotListener { snapshot, _ ->

            if (snapshot!!.exists()) {

                products.get().addOnCompleteListener {
                    val querySnapshot = it.result

                    users.document(auth.currentUser?.uid!!).get()
                        .addOnCompleteListener { user ->

                            val userFavoritesProducts =
                                user.result!!.toObject(UserDto::class.java)!!.favoriteProducts

                            if (userFavoritesProducts.isEmpty()) {
                                offer(
                                    Resource.error(
                                        "You don't have favorite products yet",
                                        data = listOf()
                                    )
                                )

                                return@addOnCompleteListener
                            }

                            val finalQuerySnapshot = querySnapshot!!.filter { document ->
                                document.get("id") in userFavoritesProducts
                            }

                            val list = mutableListOf<ProductDto>()

                            for (document in finalQuerySnapshot) {
                                list.add(document.toObject(ProductDto::class.java))
                            }

                            offer(Resource.success(productMapper.toDomainList(list)))
                        }
                }

            }

        }

        awaitClose { subscription.remove() }

//        } catch (e: Exception) {
//            Resource.error(e.message!!, null)
//        }
    }

    override suspend fun addProductToRecentList(productId: String) = withContext(Dispatchers.IO) {

        try {

            val user = users.document(auth.currentUser?.uid!!)
            val userRecentProducts =
                users.document(auth.currentUser?.uid!!).get().await()
                    .toObject(UserDto::class.java)?.recentProducts ?: listOf()

            if (productId !in userRecentProducts) {
                user.update(
                    "recentProducts",
                    userRecentProducts + productId

                ).await()
            } else {
                return@withContext
            }

        } catch (e: Exception) {
            Log.e("ERROR", e.message!!)
        }
    }

    override suspend fun listenToRecentProductAddition(): Flow<Resource<List<Product>>> =
        callbackFlow {

            val user = users.document(auth.currentUser?.uid!!)

            val subscription = user.addSnapshotListener { snapshot, _ ->

                if (snapshot!!.exists()) {

                    val recentProductsIds = snapshot.get("recentProducts") as List<String>

                    if (recentProductsIds.isEmpty()) {
                        offer(
                            Resource.error(
                                "You did not access to any product yet",
                                data = listOf()
                            )
                        )

                        return@addSnapshotListener
                    }

                    products.get().addOnCompleteListener {
                        val finalQuerySnapshot = it.result!!.filter { document ->
                            document.get("id") in recentProductsIds
                        }

                        val list = mutableListOf<ProductDto>()

                        for (document in finalQuerySnapshot) {
                            list.add(document.toObject(ProductDto::class.java))
                        }

                        offer(Resource.success(productMapper.toDomainList(list)))
                    }
                }
            }

            awaitClose { subscription.remove() }
        }

    override suspend fun listenToProductPromotions(): Flow<Resource<List<Product>>> = callbackFlow {

        val _products = products

        val subscription = _products.addSnapshotListener { snapshot, _ ->


            val list = mutableListOf<ProductDto>()

            for (document in snapshot!!) {

                if (document["promotion"] == true) {
                    list.add(document.toObject(ProductDto::class.java))
                }
            }

            val notificationList = mutableListOf<ProductDto>()
            for (dc in snapshot.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.MODIFIED -> {
                        if (dc.document["promotion"] == true) {
                            notificationList.add(dc.document.toObject(ProductDto::class.java))
                        }
                    }
                    DocumentChange.Type.ADDED -> {/* Do nothing*/
                    }
                    DocumentChange.Type.REMOVED -> {/* Do nothing*/
                    }
                }
            }

            if (notificationList.size > 0) {

                sendNotification(
                    PushNotification(
                        NotificationData(
                            title = "Promotions",
                            message = if (notificationList.size > 1) "We have ${notificationList.size} new promotions." else "We have one new promotion"
                        ),
                        to = TOPIC
                    )
                )
            }

            if (list.isEmpty()) {
                offer(
                    Resource.error(
                        "There is no promotion for now",
                        data = listOf()
                    )
                )

                return@addSnapshotListener
            }


            offer(Resource.success(productMapper.toDomainList(list)))

        }

        awaitClose { subscription.remove() }

    }

    private fun sendNotification(pushNotification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.postNotification(pushNotification)

                if (response.isSuccessful) {
                    Log.d("REPO", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("REPO", response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e("REPO", e.toString())
            }
        }

    override suspend fun addToCartProducts(cartProduct: CartProduct): ProcessUiState =
        withContext(Dispatchers.IO) {
            try {

                val productDto = cartProductDtoMapper.mapFromDomainModel(cartProduct)

                userCartProducts.add(productDto).await()

                ProcessUiState.Success("Product has been added successfully")

            } catch (e: Exception) {
                ProcessUiState.Error(e.message!!)
            }
        }

    override suspend fun deleteFromCartProducts(cartProductId: String) =
        withContext(Dispatchers.IO) {
            try {

                val querySnapshot = userCartProducts.whereEqualTo("id", cartProductId).get().await()

                val document = querySnapshot.first()

                document.reference.delete().await()

                ProcessUiState.Success("Product has been deleted successfully")

            } catch (e: Exception) {
                ProcessUiState.Error(e.message!!)
            }
        }

    override suspend fun listenToCartProducts(): Flow<Resource<List<CartProduct>>> = callbackFlow {

        //        try {

        val subscription = userCartProducts.addSnapshotListener { snapshot, _ ->

            if (snapshot!!.isEmpty) {
                offer(
                    Resource.error(
                        "You don't have cart products yet",
                        data = listOf()
                    )
                )
                return@addSnapshotListener
            }

            val list = mutableListOf<CartProductDto>()

            products.get().addOnCompleteListener { querySnapshot ->

                val queryResult = querySnapshot.result!!.documents

                for (document in snapshot) {

                    val cartProduct = document.toObject(CartProductDto::class.java)

                    val parentProduct = queryResult.find {
                        it["id"] == cartProduct.parentProductId
                    }!!.toObject(ProductDto::class.java)

                    if(parentProduct!!.promotion) {
                        cartProduct.price = parentProduct.promotionPrice.toDouble()
                    }else {
                        cartProduct.price = parentProduct.originalPrice.toDouble()
                    }

                    list.add(cartProduct)
                }

                val userCartProductsList = cartProductDtoMapper.toDomainList(list)

                offer(Resource.success(userCartProductsList))
            }

        }

        awaitClose { subscription.remove() }

//        } catch (e: Exception) {
//            Resource.error(e.message!!, null)
//        }
    }
}