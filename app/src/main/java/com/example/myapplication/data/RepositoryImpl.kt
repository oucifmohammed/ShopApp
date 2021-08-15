package com.example.myapplication.data

import com.example.myapplication.data.models.ProductDto
import com.example.myapplication.data.models.UserDto
import com.example.myapplication.data.util.ProductDtoMapper
import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.RegistrationState
import com.example.myapplication.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val mapper: ProductDtoMapper
): Repository {

    private val auth = FirebaseAuth.getInstance()
    private val users = FirebaseFirestore.getInstance().collection("Users")
    private val products = FirebaseFirestore.getInstance().collection("products")

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): RegistrationState {

        return try {
            withContext(Dispatchers.IO) {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid!!
                val user = UserDto(id = uid, userName = username, password = password)
                users.document(uid).set(user).await()

                RegistrationState.Success("Registration completed successfully")
            }
        } catch (e: Exception) {
            return RegistrationState.Error(e.message!!)
        }

    }

    override suspend fun login(email: String, password: String): RegistrationState {
        return try {
            withContext(Dispatchers.IO) {
                auth.signInWithEmailAndPassword(email, password).await()
                RegistrationState.Success("login completed successfully")
            }
        } catch (e: Exception) {
            RegistrationState.Error(e.message!!)
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

                        Resource.success(mapper.toDomainList(list))
                    }
                }else {
                    Resource.error("There is no product with that name",null)
                }
            }
        } catch (e: Exception) {
            Resource.error(message = e.message!!, data = null)
        }
    }
}