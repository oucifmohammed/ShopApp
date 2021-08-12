package com.example.myapplication.data

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.RegistrationState
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
    ): RegistrationState {

        return try {
            withContext(Dispatchers.IO) {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid!!
                val user = User(id = uid, userName = username, password = password)
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
}