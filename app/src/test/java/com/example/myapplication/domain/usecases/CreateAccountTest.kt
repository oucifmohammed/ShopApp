package com.example.myapplication.domain.usecases

import com.example.myapplication.data.FakeRepository
import com.example.myapplication.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateAccountTest {

    private lateinit var createAccountUseCase: CreateAccount

    @Before
    fun setup() {
        createAccountUseCase = CreateAccount(FakeRepository())
    }

    @Test
    fun `create account with empty full name, returns error`() = runBlockingTest {

        val result = createAccountUseCase.register(
            "email@gmail.com", "",
            "hello", "hello"
        )

        assertThat(result.status).isEqualTo(Resource.Status.ERROR)
    }

    @Test
    fun `create account with empty email, returns error`() = runBlockingTest {
        val result = createAccountUseCase.register(
            "", "Oucif",
            "hello", "hello"
        )

        assertThat(result.status).isEqualTo(Resource.Status.ERROR)
    }

    @Test
    fun `create account with confirm password different than password, returns error`() =
        runBlockingTest {
            val result = createAccountUseCase.register(
                "oucifmohamed8@gmail.com", "Oucif",
                "hello", "hello123"
            )

            assertThat(result.status).isEqualTo(Resource.Status.ERROR)
        }

    @Test
    fun `create account with bad email format, returns error`() = runBlockingTest {

        val result = createAccountUseCase.register(
            "oucifhello", "Oucif",
            "hello", "hello123"
        )

        assertThat(result.status).isEqualTo(Resource.Status.ERROR)
    }
}