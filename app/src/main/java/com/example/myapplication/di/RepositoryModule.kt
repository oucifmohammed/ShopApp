package com.example.myapplication.di

import com.example.myapplication.data.OrderRepositoryImpl
import com.example.myapplication.data.ProductRepositoryImpl
import com.example.myapplication.data.UserRepositoryImpl
import com.example.myapplication.domain.OrderRepository
import com.example.myapplication.domain.ProductRepository
import com.example.myapplication.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    abstract fun bindProductRepository(repositoryImpl: ProductRepositoryImpl): ProductRepository

    @Binds
    abstract fun bindUserRepository(repositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindOrderRepository(repositoryImpl: OrderRepositoryImpl): OrderRepository
}