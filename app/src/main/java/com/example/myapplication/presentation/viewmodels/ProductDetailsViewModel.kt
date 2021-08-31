package com.example.myapplication.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.usecases.DisplayProductDetails
import com.example.myapplication.domain.usecases.ToggleAddButton
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val displayProductDetails: DisplayProductDetails,
    private val toggleAddButton: ToggleAddButton
): ViewModel(){

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

    fun addToCartProduct(productId: String) = viewModelScope.launch {
        val result = toggleAddButton.invoke(productId)

        _addToCartProductResult.value = result
    }
}