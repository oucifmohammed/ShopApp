package com.example.myapplication.domain.usecases

import com.example.myapplication.data.UserFakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LogOutTest {

    private lateinit var logout: LogOut
    private lateinit var userFakeRepository: UserFakeRepository

    @Before
    fun setup() {
        userFakeRepository = UserFakeRepository()
        logout = LogOut(userFakeRepository)
    }

    @Test
    fun `logout from the app, returns true`() = runBlockingTest {
        userFakeRepository.logOut()

        val result = userFakeRepository.loggedInUsersList.find { user ->
            user.id == "1"
        }

        assertThat(result).isEqualTo(null)
    }
}