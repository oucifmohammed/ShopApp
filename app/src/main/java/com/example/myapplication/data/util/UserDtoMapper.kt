package com.example.myapplication.data.util

import com.example.myapplication.data.models.UserDto
import com.example.myapplication.domain.models.User
import com.example.myapplication.domain.util.Mapper

class UserDtoMapper : Mapper<UserDto, User> {

    override fun mapToDomainModel(model: UserDto): User {
        return User(
            id = model.id,
            userName = model.userName,
            email = model.email,
            password = model.password,
            photoUrl = model.photoUrl,
            favoriteProducts = model.favoriteProducts,
            recentProducts = model.recentProducts,
            cartProducts = model.cartProducts
        )
    }

    override fun mapFromDomainModel(model: User): UserDto {
        TODO("Not yet implemented")
    }

}