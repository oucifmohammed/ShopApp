package com.example.myapplication.data

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RepositoryImpl : Repository {

    private val auth = FirebaseAuth.getInstance()
    private val users = FirebaseFirestore.getInstance().collection("Users")

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): Resource<String> {

        return try {
            withContext(Dispatchers.IO) {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid!!
                val user = User(id = uid, userName = username, password = password)
                users.document(uid).set(user).await()

                Resource.success(null,"Registration completed successfully")
            }
        } catch (e: Exception) {
            return Resource.error(e.message!!, null)
        }

    }
}