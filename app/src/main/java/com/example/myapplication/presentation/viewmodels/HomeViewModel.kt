package com.example.myapplication.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.usecases.SearchForProduct
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchForProduct: SearchForProduct
) : ViewModel() {

    val searchQuery = mutableStateOf("")

    private val _productList = MutableLiveData<Resource<List<Product>>>()
    val productList: LiveData<Resource<List<Product>>> = _productList

    fun searchProduct(productName: String) {
        viewModelScope.launch {

            _productList.value = Resource.loading(listOf())

            val result = searchForProduct.search(productName)

            _productList.value = result
        }
    }

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery.value = searchQuery
    }
}