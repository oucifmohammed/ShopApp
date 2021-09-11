package com.example.myapplication.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.MainCoroutineScopeRule
import com.example.myapplication.data.ProductFakeRepository
import com.example.myapplication.domain.usecases.DeleteFromCartProducts
import com.example.myapplication.domain.usecases.ListenToCartProducts
import com.example.myapplication.getOrAwaitValue
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CartViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()

    private lateinit var  deleteFromCartProducts: DeleteFromCartProducts
    private lateinit var listenToCartProducts: ListenToCartProducts
    private lateinit var viewModel: CartViewModel
    private lateinit var fakeProductRepository: ProductFakeRepository

    @Before
    fun setup() {
        fakeProductRepository = ProductFakeRepository()
        deleteFromCartProducts = DeleteFromCartProducts(fakeProductRepository)
        listenToCartProducts = ListenToCartProducts(fakeProductRepository)

        viewModel = CartViewModel(deleteFromCartProducts,listenToCartProducts)
    }

    @Test
    fun `listen to user favorite products`()  = coroutineRule.runBlockingTest{

        val result = viewModel.userCartProducts.getOrAwaitValue()

        Truth.assertThat(result.data!!).isNotNull()
    }
}