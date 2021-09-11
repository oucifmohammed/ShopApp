package com.example.myapplication.data.util

import com.example.myapplication.data.models.CartProductDto
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.domain.util.Mapper

class CartProductDtoMapper: Mapper<CartProductDto,CartProduct>{
    override fun mapToDomainModel(model: CartProductDto): CartProduct {
        return CartProduct(
            id = model.id,
            parentProductId = model.parentProductId,
            imageUrl = model.imageUrl,
            name = model.name,
            size = model.size,
            price = model.price,
        )
    }

    override fun mapFromDomainModel(model: CartProduct): CartProductDto {
        return CartProductDto(
            id = model.id,
            parentProductId = model.parentProductId,
            imageUrl = model.imageUrl,
            name = model.name,
            size = model.size,
            price = model.price,
        )
    }

    fun toDomainList(initial: List<CartProductDto>): List<CartProduct>{
        return initial.map {
            mapToDomainModel(it)
        }
    }
}