package com.example.eccomerce.data.api.order.dto


import com.google.gson.annotations.SerializedName

data class CartRequest(
    @SerializedName("cart")
    val cart: List<CartDto>,
    @SerializedName("promo_code")
    val promoCode: String?
)