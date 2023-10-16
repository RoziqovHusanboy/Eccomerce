package com.example.eccomerce.data.api.dto

import com.google.gson.annotations.SerializedName

data class SignInResponse (
    @SerializedName("user")
     val user:UserDto,
    @SerializedName("token")
    val token:String
)