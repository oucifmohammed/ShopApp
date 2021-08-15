package com.example.myapplication.di

import com.example.myapplication.data.util.ProductDtoMapper
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
    fun provideUserMapper(): ProductDtoMapper = ProductDtoMapper()
}