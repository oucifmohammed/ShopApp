package com.example.myapplication.domain.usecases

import com.example.myapplication.data.FakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetFavoriteProductsIdsTest {

    lateinit var fakeRepository: FakeRepository
    private lateinit var getUserFavoriteProductsUseCase: GetFavoriteProductsIds

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        getUserFavoriteProductsUseCase = GetFavoriteProductsIds(fakeRepository)
    }

    @Test
    fun `testing flow`() = runBlockingTest{
        val result = fakeRepository.getFavoriteProductsIds()

        val firstId = result.first()

        assertThat(firstId[0]).isEqualTo("1")
    }
}