package com.example.eccomerce.data.api.products.dto


import com.google.gson.annotations.SerializedName

data class Section(
    @SerializedName("id")
    val id: String,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: SectionType
)