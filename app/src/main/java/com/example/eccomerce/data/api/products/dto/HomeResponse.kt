package com.example.eccomerce.data.api.products.dto


import com.example.eccomerce.data.api.auth.dto.UserDto
import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("banners")
    val banners: List<Banner>,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("sections")
    val sections: List<Section>,
    @SerializedName("user")
    val user: UserDto,
    @SerializedName("notification_count")
    val notification_count:Int
)