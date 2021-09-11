package com.example.myapplication.domain.usecases

import com.example.myapplication.data.ProductFakeRepository
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Constants
import com.example.myapplication.util.Status
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteFromCartProductsTest {

    lateinit var productFakeRepository: ProductFakeRepository
    lateinit var deleteFromCartProducts: DeleteFromCartProducts

    @Before
    fun setup() {
        productFakeRepository = ProductFakeRepository()
        deleteFromCartProducts = DeleteFromCartProducts(productFakeRepository)
    }

    @Test
    fun `delete from cart products`() = runBlockingTest{

        val productId = "1"

        deleteFromCartProducts.invoke(productId)

        Truth.assertThat(productFakeRepository.cartProductList.find { it.id == productId }).isEqualTo(null)
    }
}