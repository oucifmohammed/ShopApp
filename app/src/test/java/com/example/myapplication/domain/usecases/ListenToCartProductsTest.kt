package com.example.myapplication.domain.usecases

import com.example.myapplication.data.FakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListenToCartProductsTest {

    lateinit var fakeRepository: FakeRepository
    lateinit var listenToCartProducts: ListenToCartProducts

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        listenToCartProducts = ListenToCartProducts(fakeRepository)
    }

    @Test
    fun `testing flow`() = runBlockingTest{

        val result = listenToCartProducts.invoke()

        val list = result.first()

        assertThat(list.data!![0]).isEqualTo(fakeRepository.productsList[1])
    }
}