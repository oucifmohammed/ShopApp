package com.example.myapplication.domain.usecases

import com.example.myapplication.data.FakeRepository
import com.example.myapplication.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchByCategoryTest {

    lateinit var fakeRepository: FakeRepository
    lateinit var searchByCategory: SearchByCategory

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        searchByCategory = SearchByCategory(fakeRepository)
    }

    @Test
    fun `search for products in a category that is empty, returns error`() = runBlockingTest{

        val result = searchByCategory.invoke("Children",null)

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `search for products in a category that it's not empty, returns success`() = runBlockingTest {
        val result = searchByCategory.invoke("Women",null)

        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }
}