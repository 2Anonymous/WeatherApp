package com.sukdeb.weatherApp.data.dto.user

import com.sukdeb.weatherApp.domain.dataModels.user.UserModel

data class UserDto (
    val id: Int,
    val userName:String,
    val userEmail:String
)

fun UserDto.toDomain(): UserModel {
    return UserModel(
        userName = this.userName,
        userEmail = this.userEmail
    )
}