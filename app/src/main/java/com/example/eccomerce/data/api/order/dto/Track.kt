package com.example.eccomerce.data.api.order.dto


import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("driver")
    val driver: Driver,
    @SerializedName("from")
    val from: Location,
    @SerializedName("to")
    val to: Location
)