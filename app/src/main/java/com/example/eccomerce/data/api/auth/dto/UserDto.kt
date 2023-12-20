package com.example.eccomerce.data.api.auth.dto

import com.example.eccomerce.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserDto (
    @SerializedName("username")
    val username:String,
    @SerializedName("avatar")
    val avatar:String?,
    @SerializedName("email")
    val email:String,
    @SerializedName("firstName")
    val firstName:String?,
    @SerializedName("lastName")
    val lastName:String?,
){
    fun toUser() = User (
        username = username,
        avatar = avatar,
        email = email,
        firstName = firstName,
        lastName = lastName
    )
}
