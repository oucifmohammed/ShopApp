package com.example.myapplication.di

import com.example.myapplication.data.RepositoryImpl
import com.example.myapplication.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModule {


    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository

}