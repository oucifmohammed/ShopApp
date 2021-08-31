package com.example.myapplication.domain.usecases

import com.example.myapplication.data.FakeRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class GetFavoriteProductsTest {

    lateinit var fakeRepository: FakeRepository
    lateinit var getUserFavoriteProducts: GetUserFavoriteProducts

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        getUserFavoriteProducts = GetUserFavoriteProducts(fakeRepository)
    }

    @Test
    fun `testing flow`() = runBlockingTest{
        val result = fakeRepository.getFavoriteProducts()

        val list = result.first()

        Truth.assertThat(list.data!![0]).isEqualTo(fakeRepository.productsList[0])
    }
}