package com.example.myapplication.domain.usecases

import com.example.myapplication.data.UserFakeRepository
import com.example.myapplication.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateAccountTest {

    private lateinit var createAccountUseCase: CreateAccount
    private lateinit var userFakeRepository: UserFakeRepository

    @Before
    fun setup() {
        userFakeRepository = UserFakeRepository()
        createAccountUseCase = CreateAccount(userFakeRepository)
    }

    @Test
    fun `create account with empty full name, returns error`() = runBlockingTest {

        val result = createAccountUseCase.invoke(
            "email@gmail.com", "",
            "hello", "hello"
        )

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `create account with empty email, returns error`() = runBlockingTest {
        val result = createAccountUseCase.invoke(
            "", "Oucif",
            "hello", "hello"
        )

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `create account with confirm password different than password, returns error`() =
        runBlockingTest {
            val result = createAccountUseCase.invoke(
                "oucifmohamed8@gmail.com", "Oucif",
                "hello", "hello123"
            )

            assertThat(result.status).isEqualTo(Status.ERROR)
        }

    @Test
    fun `create account with bad email format, returns error`() = runBlockingTest {

        val result = createAccountUseCase.invoke(
            "crick@.web.com", "Oucif",
            "hello123", "hello123"
        )

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `create account with email that already exists, returns error`() = runBlockingTest {
        val result = userFakeRepository.register("oucifmohamed8@gmail.com",
            "oucif mohammed", "hello123")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `create account with email that does not exists, returns success`() = runBlockingTest {
        val result = userFakeRepository.register("oucifmohamed@outlook.fr",
            "oucif mohammed", "hello123")

        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }
}