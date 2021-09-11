package com.example.myapplication.domain.usecases

import com.example.myapplication.data.ProductFakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListenToRecentProductAdditionTest {

    lateinit var productFakeRepository: ProductFakeRepository
    lateinit var listenToRecentProductAddition: ListenToRecentProductAddition

    @Before
    fun setup() {
        productFakeRepository = ProductFakeRepository()
        listenToRecentProductAddition = ListenToRecentProductAddition(productFakeRepository)
    }

    @Test
    fun `testing flow`() = runBlocking{
        val result = listenToRecentProductAddition.invoke()

        val list = result.first()

        assertThat(list.data!![0]).isEqualTo(productFakeRepository.productsList[0])
    }
}