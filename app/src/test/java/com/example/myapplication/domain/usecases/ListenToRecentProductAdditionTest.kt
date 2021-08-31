package com.example.myapplication.domain.usecases

import com.example.myapplication.data.FakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListenToRecentProductAdditionTest {

    lateinit var fakeRepository: FakeRepository
    lateinit var listenToRecentProductAddition: ListenToRecentProductAddition

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        listenToRecentProductAddition = ListenToRecentProductAddition(fakeRepository)
    }

    @Test
    fun `testing flow`() = runBlocking{
        val result = listenToRecentProductAddition.invoke()

        val list = result.first()

        assertThat(list.data!![0]).isEqualTo(fakeRepository.productsList[0])
    }
}