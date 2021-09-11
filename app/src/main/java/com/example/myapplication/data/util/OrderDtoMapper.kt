package com.example.myapplication.data.util

import com.example.myapplication.data.models.OrderDto
import com.example.myapplication.data.models.ProductDto
import com.example.myapplication.domain.models.Order
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.util.Mapper

class OrderDtoMapper: Mapper<OrderDto,Order>{

    override fun mapToDomainModel(model: OrderDto): Order {
        return Order(
            id = model.id,
            userId = model.userId,
            orderNumber = model.orderNumber,
            orderProductsList = model.orderProductsList,
            totalPrice = model.totalPrice
        )
    }

    override fun mapFromDomainModel(model: Order): OrderDto {
        return OrderDto(
            id = model.id,
            userId = model.userId,
            orderNumber = model.orderNumber,
            orderProductsList = model.orderProductsList,
            totalPrice = model.totalPrice
        )
    }

    fun toDomainList(initial: List<OrderDto>): List<Order> {
        return initial.map {
            mapToDomainModel(it)
        }
    }
}