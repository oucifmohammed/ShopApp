package com.example.myapplication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserFavoriteProducts: GetUserFavoriteProducts,
    private val addToFavorites: AddToFavorites,
    private val addToRecentProduct: AddToRecentProduct,
    private val listenToRecentProductAddition: ListenToRecentProductAddition,
    private val listenToProductPromotions: ListenToProductPromotions,
    private val getFavoriteProductsIds: GetFavoriteProductsIds

) : ViewModel() {


    val userFavoriteProducts = liveData(Dispatchers.IO) {

        getUserFavoriteProducts.invoke().collect {
            emit(it)
        }
    }

    val userRecentProducts = liveData(Dispatchers.IO) {
        listenToRecentProductAddition.invoke().collect {
            emit(it)
        }
    }

    val productPromotions = liveData(Dispatchers.IO) {
        listenToProductPromotions.invoke().collect {
            emit(it)
        }
    }

    val favoriteProductsIds = liveData(Dispatchers.IO) {
        getFavoriteProductsIds.invoke().collect() {
            emit(it)
        }
    }

    fun toggleLikeButton(product: Product) = viewModelScope.launch{
        addToFavorites.invoke(product)
    }

    fun addToRecentList(productId: String) = viewModelScope.launch{
        addToRecentProduct.invoke(productId)
    }
}