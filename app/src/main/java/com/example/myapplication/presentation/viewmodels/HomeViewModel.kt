package com.example.myapplication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.usecases.*
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    private val getFavoriteProductsIds: GetFavoriteProductsIds,
    private val subscribeToPromotionsTopic: SubscribeToPromotionsTopic,
    private val defaultDispatcher: CoroutineDispatcher

) : ViewModel() {


    val userFavoriteProducts = liveData(defaultDispatcher) {
        getUserFavoriteProducts.invoke().collect {
            emit(it)
        }
    }

    val userRecentProducts = liveData(defaultDispatcher) {
        listenToRecentProductAddition.invoke().collect {
            emit(it)
        }
    }

    val productPromotions = liveData(defaultDispatcher) {
        listenToProductPromotions.invoke().collect {
            emit(it)
        }
    }

    val favoriteProductsIds = liveData(defaultDispatcher) {
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

    fun subscribeToPromotionsTopic() = viewModelScope.launch {
        subscribeToPromotionsTopic.invoke()
    }
}