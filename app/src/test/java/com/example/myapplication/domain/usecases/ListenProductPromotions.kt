package com.example.myapplication.domain.usecases

import com.example.myapplication.data.ProductFakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListenProductPromotions {

    lateinit var productFakeRepository: ProductFakeRepository
    lateinit var listenToProductPromotions: ListenToProductPromotions

    @Before
    fun setup() {
        productFakeRepository = ProductFakeRepository()
        listenToProductPromotions = ListenToProductPromotions(productFakeRepository)
    }

    @Test
    fun `testing flow`() = runBlockingTest{

        val result = listenToProductPromotions.invoke()
        val list = result.first()

        assertThat(list.data!![0]).isEqualTo(productFakeRepository.productsList[0])
    }
}