package com.example.myapplication.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.usecases.DisplayProductDetails
import com.example.myapplication.domain.usecases.AddToCartProducts
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val displayProductDetails: DisplayProductDetails,
    private val addToCartProducts: AddToCartProducts
): ViewModel(){

    val selectedCategory: MutableState<String?> = mutableStateOf(null)

    val loading = mutableStateOf(false)
    val result: MutableState<Resource<Product>?> = mutableStateOf(null)

    private val _addToCartProductResult = MutableLiveData<ProcessUiState>()
    val addToCartProductResult: LiveData<ProcessUiState> = _addToCartProductResult

    fun displayProductDetails(productId: String) {
        viewModelScope.launch {
            loading.value = true

            val product = displayProductDetails.invoke(productId)

            loading.value = false
            result.value = product
        }
    }

    fun addToCartProduct(cartProduct: CartProduct) = viewModelScope.launch {
        val result = addToCartProducts.invoke(cartProduct)

        _addToCartProductResult.value = result
    }

    fun onSelectedCategoryChanged(categoryValue: String) {
        selectedCategory.value = categoryValue
    }
}