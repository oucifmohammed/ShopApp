package com.example.myapplication.domain.usecases

import com.example.myapplication.data.ProductFakeRepository
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.util.Constants
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddToCartProductsTest {

    lateinit var productFakeRepository: ProductFakeRepository
    lateinit var addToCartProducts: AddToCartProducts

    @Before
    fun setup() {
        productFakeRepository = ProductFakeRepository()
        addToCartProducts = AddToCartProducts(productFakeRepository)
    }

    @Test
    fun `add to cart products`() = runBlockingTest{

        val cartProduct = CartProduct(
            id = "3",
            parentProductId = "2",
            imageUrl = Constants.DEFAULT_USER_IMAGE,
            name = "Pants2",
            price = 2000.0,
            size = "L"
        )

        addToCartProducts.invoke(cartProduct)

        Truth.assertThat(productFakeRepository.cartProductList.find { it.id == cartProduct.id }).isNotEqualTo(null)
    }
}