package com.example.myapplication.domain.usecases

import com.example.myapplication.data.UserFakeRepository
import com.example.myapplication.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignInTest {

    private lateinit var signInUseCase: SignIn
    private lateinit var userFakeRepository: UserFakeRepository

    @Before
    fun setup() {
        userFakeRepository = UserFakeRepository()
        signInUseCase = SignIn(userFakeRepository)
    }

    @Test
    fun `sign in with empty email, returns error`() = runBlockingTest {

        val result = signInUseCase.invoke("", "hello123")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `sign in with empty password, returns error`() = runBlockingTest {
        val result = signInUseCase.invoke("oucifmohamed@gmail.com", "")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `sign in with bad email format, returns error`() = runBlockingTest {
        val result = signInUseCase.invoke("oucifhello@com","hello")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `sign in with existing account, returns true`() = runBlockingTest {
        val result = userFakeRepository.login("oucifmohamed8@gmail.com", "hello1234")

        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `sign in with not existing account, returns error`() = runBlockingTest {
        val result = userFakeRepository.login("example@gmail.com","hello")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }
}