package com.example.myapplication.data

import android.net.Uri
import android.util.Log
import com.example.myapplication.data.models.ProductDto
import com.example.myapplication.data.models.UserDto
import com.example.myapplication.data.util.ProductDtoMapper
import com.example.myapplication.data.util.UserDtoMapper
import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.Constants.DEFAULT_USER_IMAGE
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val productMapper: ProductDtoMapper,
    private val userMapper: UserDtoMapper
) : Repository {

    private val auth = FirebaseAuth.getInstance()
    private val users = FirebaseFirestore.getInstance().collection("Users")
    private val products = FirebaseFirestore.getInstance().collection("products")
    private val storage = FirebaseStorage.getInstance()

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): ProcessUiState {

        return try {
            withContext(Dispatchers.IO) {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid!!
                val user = UserDto(id = uid, userName = username, password = password)
                users.document(uid).set(user).await()

                ProcessUiState.Success("Registration completed successfully")
            }
        } catch (e: Exception) {
            return ProcessUiState.Error(e.message!!)
        }

    }

    override suspend fun login(email: String, password: String): ProcessUiState {
        return try {
            withContext(Dispatchers.IO) {
                auth.signInWithEmailAndPassword(email, password).await()
                ProcessUiState.Success("login completed successfully")
            }
        } catch (e: Exception) {
            ProcessUiState.Error(e.message!!)
        }
    }

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

    private suspend fun updateProfilePicture(uri: Uri) = withContext(Dispatchers.IO) {
        val user =
            users.document(auth.currentUser?.uid!!).get().await().toObject(UserDto::class.java)

        if (user?.photoUrl != DEFAULT_USER_IMAGE) {
            storage.getReferenceFromUrl(user?.photoUrl!!).delete().await()
        }

        storage.reference.child("userImages/${user.id}").putFile(uri)
            .await().metadata?.reference?.downloadUrl?.await()
    }

    override suspend fun editProfile(username: String, email: String, uri: Uri?): ProcessUiState {

        return try {
            val userId = auth.currentUser?.uid

            val imageUrl = uri?.let {
                updateProfilePicture(it).toString()
            }

            val map = mutableMapOf(
                "userName" to username,
                "email" to email
            )

            imageUrl?.let {
                map["photoUrl"] = it
            }

            withContext(Dispatchers.IO) {
                users.document(userId!!).update(map.toMap()).await()
            }

            ProcessUiState.Success("Profile updated successfully")
        } catch (e: Exception) {
            ProcessUiState.Error(e.message!!)
        }
    }

    override suspend fun getUserAccount(): User {
        val userDto =
            users.document(auth.currentUser?.uid!!).get().await().toObject(UserDto::class.java)

        return userMapper.mapToDomainModel(userDto!!)
    }

    override suspend fun logOut() {
        withContext(Dispatchers.IO) {
            auth.signOut()
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

    override suspend fun addToCartProducts(productId: String): ProcessUiState =
        withContext(Dispatchers.IO) {
            try {

                val user = users.document(auth.currentUser?.uid!!)
                val userCartProducts =
                    users.document(auth.currentUser?.uid!!).get().await()
                        .toObject(UserDto::class.java)?.cartProducts ?: listOf()

                user.update(
                    "cartProducts",
                    userCartProducts + productId
                ).await()

                ProcessUiState.Success("Product has been added successfully")

            } catch (e: Exception) {
                ProcessUiState.Error(e.message!!)
            }
        }

    override suspend fun deleteFromCartProducts(productId: String) = withContext(Dispatchers.IO) {
        try {

            val user = users.document(auth.currentUser?.uid!!)
            val userCartProducts =
                users.document(auth.currentUser?.uid!!).get().await()
                    .toObject(UserDto::class.java)?.cartProducts ?: listOf()

            user.update(
                "cartProducts",
                userCartProducts - productId
            ).await()

            ProcessUiState.Success("Product has been deleted successfully")

        } catch (e: Exception) {
            ProcessUiState.Error(e.message!!)
        }
    }

    override suspend fun listenToCartProducts(): Flow<Resource<List<Product>>> = callbackFlow {

        //        try {
        val user = users.document(auth.currentUser?.uid!!)

        val subscription = user.addSnapshotListener { snapshot, _ ->

            if (snapshot!!.exists()) {

                products.get().addOnCompleteListener {
                    val querySnapshot = it.result

                    users.document(auth.currentUser?.uid!!).get()
                        .addOnCompleteListener { user ->

                            val userCartProducts =
                                user.result!!.toObject(UserDto::class.java)!!.cartProducts

                            if (userCartProducts.isEmpty()) {
                                offer(
                                    Resource.error(
                                        "You don't have cart products yet",
                                        data = listOf()
                                    )
                                )

                                return@addOnCompleteListener
                            }

                            val finalQuerySnapshot = querySnapshot!!.filter { document ->
                                document.get("id") in userCartProducts
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
}