package com.example.myapplication.domain.usecases

import com.example.myapplication.data.FakeRepository
import com.example.myapplication.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class EditProfileTest {

    private lateinit var editProfileUseCase: EditProfile
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        editProfileUseCase = EditProfile(fakeRepository)
    }

    @Test
    fun `edit profile with empty username, returns error`() = runBlockingTest{

        val result = editProfileUseCase.invoke("","oucifmohamed8@gmail.com",null)

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `edit profile with empty email, returns error`() = runBlockingTest {
        val result = editProfileUseCase.invoke("Mohammed","",null)

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `edit profile with bad email format, returns error`() = runBlockingTest {
        val result = editProfileUseCase.invoke("Mohamed","email@.com",null)

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `edit profile with a new username or new email, returns success`() = runBlockingTest {
        val result = editProfileUseCase.invoke("Mohamed","oucifmohamed8@gmail.com",null)

        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }
}