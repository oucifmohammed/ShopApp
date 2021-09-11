package com.example.myapplication.domain.usecases

import com.example.myapplication.data.ProductFakeRepository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Constants
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddToFavoritesTest {

    lateinit var productFakeRepository: ProductFakeRepository
    lateinit var addToFavorites: AddToFavorites

    @Before
    fun setup() {
        productFakeRepository = ProductFakeRepository()
        addToFavorites = AddToFavorites(productFakeRepository)
    }


    @Test
    fun `toggle like button for the first time on a product, returns true`() = runBlockingTest {

        val product = Product(
            id = "2",
            name = "pants2",
            image = Constants.DEFAULT_USER_IMAGE,
            category = "Women",
            originalPrice = 2000f,
            promotionPrice = 0f,
            sizes = listOf("S","L")
        )

        val result = addToFavorites.invoke(product)

        assertThat(result.data).isEqualTo(true)
    }

    @Test
    fun `toggle like button after already liking the product, returns false`() = runBlockingTest{

        val product = Product(
            id = "1",
            name = "pants",
            image = Constants.DEFAULT_USER_IMAGE,
            category = "Women",
            originalPrice = 2000f,
            promotionPrice = 0f,
            sizes = listOf("S","M","L","XL")
        )

        val result = addToFavorites.invoke(product)

        assertThat(result.data).isEqualTo(false)
    }
}