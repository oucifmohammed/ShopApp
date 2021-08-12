package com.example.myapplication.domain.usecases

import com.example.myapplication.data.FakeRepository
import com.example.myapplication.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateAccountTest {

    private lateinit var createAccountUseCase: CreateAccount
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        createAccountUseCase = CreateAccount(fakeRepository)
    }

    @Test
    fun `create account with empty full name, returns error`() = runBlockingTest {

        val result = createAccountUseCase.register(
            "email@gmail.com", "",
            "hello", "hello"
        )

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `create account with empty email, returns error`() = runBlockingTest {
        val result = createAccountUseCase.register(
            "", "Oucif",
            "hello", "hello"
        )

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `create account with confirm password different than password, returns error`() =
        runBlockingTest {
            val result = createAccountUseCase.register(
                "oucifmohamed8@gmail.com", "Oucif",
                "hello", "hello123"
            )

            assertThat(result.status).isEqualTo(Status.ERROR)
        }

    @Test
    fun `create account with bad email format, returns error`() = runBlockingTest {

        val result = createAccountUseCase.register(
            "crick@.web.com", "Oucif",
            "hello123", "hello123"
        )

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `create account with email that already exists, returns error`() = runBlockingTest {
        val result = fakeRepository.register("oucifmohamed8@gmail.com",
            "oucif mohammed", "hello123")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `create account with email that does not exists, returns success`() = runBlockingTest {
        val result = fakeRepository.register("oucifmohamed@outlook.fr",
            "oucif mohammed", "hello123")

        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }
}