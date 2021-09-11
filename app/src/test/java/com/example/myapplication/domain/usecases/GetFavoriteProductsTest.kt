package com.example.myapplication.domain.usecases

import com.example.myapplication.data.ProductFakeRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class GetFavoriteProductsTest {

    lateinit var productFakeRepository: ProductFakeRepository
    lateinit var getUserFavoriteProducts: GetUserFavoriteProducts

    @Before
    fun setup() {
        productFakeRepository = ProductFakeRepository()
        getUserFavoriteProducts = GetUserFavoriteProducts(productFakeRepository)
    }

    @Test
    fun `testing flow`() = runBlockingTest{
        val result = productFakeRepository.getFavoriteProducts()

        val list = result.first()

        Truth.assertThat(list.data!![0]).isEqualTo(productFakeRepository.productsList[0])
    }
}