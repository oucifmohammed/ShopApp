package com.example.myapplication.data

import android.net.Uri
import com.example.myapplication.data.models.UserDto
import com.example.myapplication.data.util.UserDtoMapper
import com.example.myapplication.domain.UserRepository
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.Constants
import com.example.myapplication.util.ProcessUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userMapper: UserDtoMapper,
) : UserRepository{

    private val users = FirebaseFirestore.getInstance().collection("Users")
    private val auth = FirebaseAuth.getInstance()
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

    private suspend fun updateProfilePicture(uri: Uri) = withContext(Dispatchers.IO) {
        val user =
            users.document(auth.currentUser?.uid!!).get().await().toObject(UserDto::class.java)

        if (user?.photoUrl != Constants.DEFAULT_USER_IMAGE) {
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

    override suspend fun subscribeToPromotionTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC).await()
    }
}