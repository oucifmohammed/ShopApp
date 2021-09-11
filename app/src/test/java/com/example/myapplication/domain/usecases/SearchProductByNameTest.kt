package com.example.myapplication.domain.usecases

import com.example.myapplication.data.ProductFakeRepository
import com.example.myapplication.util.Status
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SearchProductByNameTest {

    private lateinit var searchForProductUseCase: SearchForProduct
    private lateinit var productFakeRepository: ProductFakeRepository

    @Before
    fun setup() {

        productFakeRepository = ProductFakeRepository()
        searchForProductUseCase = SearchForProduct(productFakeRepository)
    }

    @Test
    fun `search for product with empty query, returns error`() = runBlockingTest {

        val result = searchForProductUseCase.invoke("")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `search for an existing product, returns success`() = runBlockingTest {
        val result = searchForProductUseCase.invoke("pants")

        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `search for a not existing product, returns error`() = runBlockingTest {
        val result = searchForProductUseCase.invoke("my name")

        assertThat(result.status).isEqualTo(Status.ERROR)
    }
}