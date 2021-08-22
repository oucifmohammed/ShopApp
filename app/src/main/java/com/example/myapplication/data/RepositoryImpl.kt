package com.example.myapplication.data

import android.net.Uri
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
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val productMapper: ProductDtoMapper,
    private val userMapper: UserDtoMapper
): Repository {

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

                if(!querySnapshot.isEmpty) {
                    withContext(Dispatchers.Default) {
                        val list = mutableListOf<ProductDto>()

                        for(document in querySnapshot) {
                            list.add(document.toObject(ProductDto::class.java))
                        }

                        Resource.success(productMapper.toDomainList(list))
                    }
                }else {
                    Resource.error("There is no product with that name",null)
                }
            }
        } catch (e: Exception) {
            Resource.error(message = e.message!!, data = null)
        }
    }

    private suspend fun updateProfilePicture(uri: Uri) = withContext(Dispatchers.IO) {
        val user = users.document(auth.currentUser?.uid!!).get().await().toObject(UserDto::class.java)

        if(user?.photoUrl != DEFAULT_USER_IMAGE) {
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
        }catch (e: Exception) {
            ProcessUiState.Error(e.message!!)
        }
    }

    override suspend fun getUserAccount(): User {
        val userDto = users.document(auth.currentUser?.uid!!).get().await().toObject(UserDto::class.java)

        return userMapper.mapToDomainModel(userDto!!)
    }

    override suspend fun logOut() {
        withContext(Dispatchers.IO) {
            auth.signOut()
        }
    }
}