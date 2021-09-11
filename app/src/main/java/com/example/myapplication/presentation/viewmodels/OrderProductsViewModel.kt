package com.example.myapplication.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.models.Order
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.usecases.GetOrderProducts
import com.example.myapplication.domain.usecases.GetUserOrders
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderProductsViewModel @Inject constructor(
    private val getOrderProducts: GetOrderProducts
): ViewModel(){

    private val _orderProductsResult = MutableLiveData<Resource<List<Product>>>()
    val orderProductsResult: LiveData<Resource<List<Product>>> = _orderProductsResult

    fun getProducts(orderId: String) = viewModelScope.launch {
        val result = getOrderProducts.invoke(orderId)
        _orderProductsResult.value = result
    }
}