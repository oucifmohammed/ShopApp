package com.example.myapplication.domain.usecases

import com.example.myapplication.data.FakeRepository
import com.example.myapplication.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignInTest {

    private lateinit var signInUseCase: SignIn
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        signInUseCase = SignIn(fakeRepository)
    }

    @Test
    fun `sign in with empty email, returns error`() = runBlockingTest {

        val result = signInUseCase.login("", "hello123")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `sign in with empty password, returns error`() = runBlockingTest {
        val result = signInUseCase.login("oucifmohamed@gmail.com", "")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `sign in with bad email format, returns error`() = runBlockingTest {
        val result = signInUseCase.login("oucifhello@com","hello")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `sign in with existing account, returns true`() = runBlockingTest {
        val result = fakeRepository.login("oucifmohamed8@gmail.com", "hello1234")

        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `sign in with not existing account, returns false`() = runBlockingTest {
        val result = fakeRepository.login("example@gmail.com","hello")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }
}