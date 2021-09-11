package com.example.myapplication.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.myapplication.domain.models.Order
import com.example.myapplication.domain.usecases.AddOrder
import com.example.myapplication.domain.usecases.DeleteFromCartProducts
import com.example.myapplication.domain.usecases.ListenToCartProducts
import com.example.myapplication.util.ProcessUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val deleteFromCartProducts: DeleteFromCartProducts,
    private val listenToCartProducts: ListenToCartProducts,
    private val addOrder: AddOrder
) : ViewModel() {

    private val _deleteCartProductResult = MutableLiveData<ProcessUiState>()

    val userCartProducts = liveData(Dispatchers.IO) {
        listenToCartProducts.invoke().collect {
            emit(it)
        }
    }

    private val _makeOrderResult = MutableLiveData<ProcessUiState>()
    val makeOrderResult: LiveData<ProcessUiState> = _makeOrderResult
    private val _makeOrderLoadingState = mutableStateOf(false)
    val makeOrderLoadingState: State<Boolean> = _makeOrderLoadingState
    val openDialog = mutableStateOf(false)
    fun deleteCartProduct(productId: String) = viewModelScope.launch {

        val result = deleteFromCartProducts.invoke(productId)
        _deleteCartProductResult.value = result
    }

    fun makeOrder() = viewModelScope.launch {

        val order = Order(
            id = UUID.randomUUID().toString(),
            totalPrice = calculateProductsTotalPrice() + 500.0
        )

        _makeOrderLoadingState.value = true

        val result = addOrder.invoke(order)

        _makeOrderLoadingState.value = false
        _makeOrderResult.value = result

        openDialog.value = true
    }

    fun calculateProductsTotalPrice(): Double {
        var total = 0.0

        userCartProducts.value?.data?.let {
            it.forEach {
                total += it.price
            }
        } ?: return total

        return total
    }
}