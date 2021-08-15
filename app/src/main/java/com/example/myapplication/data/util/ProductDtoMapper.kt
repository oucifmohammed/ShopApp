package com.example.myapplication.data.util

import com.example.myapplication.data.models.ProductDto
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.util.Mapper

class ProductDtoMapper: Mapper<ProductDto,Product>{


    override fun mapToDomainModel(model: ProductDto): Product {
        return Product(
            id = model.id,
            name = model.name,
            image = model.image,
            category = model.category,
            originalPrice = model.originalPrice,
            promotionPrice = if(model.promotion) model.promotionPrice else 0f
        )
    }

    fun toDomainList(initial: List<ProductDto>): List<Product> {
        return initial.map {
            mapToDomainModel(it)
        }
    }
}