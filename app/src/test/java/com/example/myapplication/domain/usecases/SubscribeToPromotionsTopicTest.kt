package com.example.myapplication.domain.usecases

import com.example.myapplication.data.UserFakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SubscribeToPromotionsTopicTest {

    private lateinit var subscribeToPromotionsTopic: SubscribeToPromotionsTopic
    private lateinit var userFakeRepository: UserFakeRepository

    @Before
    fun setup() {
        userFakeRepository = UserFakeRepository()
        subscribeToPromotionsTopic = SubscribeToPromotionsTopic(userFakeRepository)
    }

    @Test
    fun `subscribe to the promotions topic, returns true`() = runBlockingTest{
        subscribeToPromotionsTopic.invoke()

        assertThat(userFakeRepository.topicUsersList.size > 0).isEqualTo(true)
    }
}