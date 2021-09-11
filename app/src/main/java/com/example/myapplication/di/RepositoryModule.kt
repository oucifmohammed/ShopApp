package com.example.myapplication.di

import com.example.myapplication.data.util.CartProductDtoMapper
import com.example.myapplication.data.util.OrderDtoMapper
import com.example.myapplication.data.util.ProductDtoMapper
import com.example.myapplication.data.util.UserDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductMapper(): ProductDtoMapper = ProductDtoMapper()

    @Provides
    @Singleton
    fun provideUserMapper(): UserDtoMapper = UserDtoMapper()

    @Provides
    @Singleton
    fun provideCartProductMapper(): CartProductDtoMapper = CartProductDtoMapper()

    @Provides
    @Singleton
    fun providerOrderMapper(): OrderDtoMapper = OrderDtoMapper()
}