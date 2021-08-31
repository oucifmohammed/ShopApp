package com.example.myapplication.presentation.viewmodels

import androidx.lifecycle.*
import com.example.myapplication.domain.usecases.DeleteFromCartProducts
import com.example.myapplication.domain.usecases.ListenToCartProducts
import com.example.myapplication.util.ProcessUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val deleteFromCartProducts: DeleteFromCartProducts,
    private val listenToCartProducts: ListenToCartProducts
): ViewModel(){

    private val _deleteCartProductResult = MutableLiveData<ProcessUiState>()
    val deleteCartProductResult: LiveData<ProcessUiState> = _deleteCartProductResult

    val userCartProducts = liveData(Dispatchers.IO) {
        listenToCartProducts.invoke().collect {
            emit(it)
        }
    }

    fun deleteCartProduct(productId: String) = viewModelScope.launch {

        val result = deleteFromCartProducts.invoke(productId)
        _deleteCartProductResult.value = result
    }
}