package com.example.eccomerce.data.api.auth.dto

import com.google.gson.annotations.SerializedName

data class AuthResponce (
    @SerializedName("user")
     val user: UserDto,
    @SerializedName("token")
    val token:String
)