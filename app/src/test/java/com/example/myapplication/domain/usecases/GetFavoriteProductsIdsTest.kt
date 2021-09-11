package com.example.myapplication.domain.usecases

import com.example.myapplication.data.ProductFakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFavoriteProductsIdsTest {

    lateinit var productFakeRepository: ProductFakeRepository
    private lateinit var getUserFavoriteProductsUseCase: GetFavoriteProductsIds

    @Before
    fun setup() {
        productFakeRepository = ProductFakeRepository()
        getUserFavoriteProductsUseCase = GetFavoriteProductsIds(productFakeRepository)
    }

    @Test
    fun `testing flow`() = runBlockingTest{
        val result = productFakeRepository.getFavoriteProductsIds()

        val firstId = result.first()

        assertThat(firstId[0]).isEqualTo("1")
    }
}