package com.example.myapplication.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.MainCoroutineScopeRule
import com.example.myapplication.data.ProductFakeRepository
import com.example.myapplication.data.UserFakeRepository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.usecases.*
import com.example.myapplication.getOrAwaitValue
import com.example.myapplication.util.Constants
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var productFakeRepository: ProductFakeRepository
    private lateinit var userFakeRepository: UserFakeRepository
    private lateinit var getUserFavoriteProducts: GetUserFavoriteProducts
    private lateinit var addToFavorites: AddToFavorites
    private lateinit var addToRecentProduct: AddToRecentProduct
    private lateinit var listenToRecentProductAddition: ListenToRecentProductAddition
    private lateinit var listenToProductPromotions: ListenToProductPromotions
    private lateinit var getFavoriteProductsIds: GetFavoriteProductsIds
    private lateinit var subscribeToPromotionsTopic: SubscribeToPromotionsTopic

    @Before
    fun setup() {
        productFakeRepository = ProductFakeRepository()
        userFakeRepository = UserFakeRepository()
        getUserFavoriteProducts = GetUserFavoriteProducts(productFakeRepository)
        addToFavorites = AddToFavorites(productFakeRepository)
        addToRecentProduct = AddToRecentProduct(productFakeRepository)
        listenToRecentProductAddition = ListenToRecentProductAddition(productFakeRepository)
        listenToProductPromotions = ListenToProductPromotions(productFakeRepository)
        getFavoriteProductsIds = GetFavoriteProductsIds(productFakeRepository)
        subscribeToPromotionsTopic = SubscribeToPromotionsTopic(userFakeRepository)

        viewModel = HomeViewModel(
            getUserFavoriteProducts,
            addToFavorites,
            addToRecentProduct,
            listenToRecentProductAddition,
            listenToProductPromotions,
            getFavoriteProductsIds,
            subscribeToPromotionsTopic,
            coroutineRule.dispatcher
        )
    }

    @Test
    fun `listen to user favorite products`()  = coroutineRule.runBlockingTest{

        val result = viewModel.userFavoriteProducts.getOrAwaitValue()

        assertThat(result.data!!).isNotNull()
    }

    @Test
    fun `listen to user recent products`() = coroutineRule.runBlockingTest {

        val result = viewModel.userRecentProducts.getOrAwaitValue()

        assertThat(result.data!!).isNotNull()
    }

    @Test
    fun `listen to product promotions list`() = coroutineRule.runBlockingTest{
        val result = viewModel.productPromotions.getOrAwaitValue()

        assertThat(result.data!!).isNotNull()
    }

    @Test
    fun `listen to favorite products ids`() = coroutineRule.runBlockingTest{
        val result = viewModel.favoriteProductsIds.getOrAwaitValue()

        assertThat(result).isNotNull()
    }
}