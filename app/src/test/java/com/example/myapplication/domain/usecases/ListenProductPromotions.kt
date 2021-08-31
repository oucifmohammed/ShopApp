package com.example.myapplication.domain.usecases

import com.example.myapplication.data.FakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListenProductPromotions {

    lateinit var fakeRepository: FakeRepository
    lateinit var listenToProductPromotions: ListenToProductPromotions

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        listenToProductPromotions = ListenToProductPromotions(fakeRepository)
    }

    @Test
    fun `testing flow`() = runBlockingTest{

        val result = listenToProductPromotions.invoke()
        val list = result.first()

        assertThat(list.data!![0]).isEqualTo(fakeRepository.productsList[0])
    }
}