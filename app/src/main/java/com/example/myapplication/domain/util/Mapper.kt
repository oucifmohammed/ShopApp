package com.example.myapplication.domain.util

interface Mapper<T,DomainModel> {

    fun mapToDomainModel(model: T): DomainModel
}