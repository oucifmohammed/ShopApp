package com.example.myapplication.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.usecases.AddToFavorites
import com.example.myapplication.domain.usecases.GetFavoriteProductsIds
import com.example.myapplication.domain.usecases.SearchByCategory
import com.example.myapplication.domain.usecases.SearchForProduct
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchForProduct: SearchForProduct,
    private val searchForProductsByCategory: SearchByCategory,
    private val addToFavorites: AddToFavorites,
    private val getFavoriteProductsIds: GetFavoriteProductsIds
): ViewModel(){

    val searchQuery = mutableStateOf("")
    val selectedCategory: MutableState<String?> = mutableStateOf(null)

    private val _productList = MutableLiveData<Resource<List<Product>>>()
    val productList: LiveData<Resource<List<Product>>> = _productList

    private val isLiked = mutableStateOf(false)

    val favoriteProductsIds = liveData(Dispatchers.IO) {
        getFavoriteProductsIds.invoke().collect {
            emit(it)
        }
    }

    fun searchProduct(productName: String) {
        viewModelScope.launch {

            _productList.value = Resource.loading(listOf())

            val result = searchForProduct.invoke(productName.trim())

            _productList.value = result
        }
    }

    fun searchByCategory() {
        viewModelScope.launch {
            _productList.value = Resource.loading(listOf())

            val result = if(searchQuery.value.isEmpty())
                searchForProductsByCategory.invoke(selectedCategory.value!!,null)
            else
                searchForProductsByCategory.invoke(selectedCategory.value!!,searchQuery.value)

            _productList.value = result
        }
    }

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery.value = searchQuery
    }

    fun onSelectedCategoryChanged(categoryValue: String) {
        selectedCategory.value = categoryValue
    }

    fun toggleLikeButton(product: Product) = viewModelScope.launch{

        val result = addToFavorites.invoke(product)

        isLiked.value = result.data!!
    }
}