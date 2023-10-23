package com.example.eccomerce.data.api.auth.dto

import com.example.eccomerce.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserDto (
    @SerializedName("username")
    val username:String
){
    fun toUser() = User (
        username = username
    )
}
