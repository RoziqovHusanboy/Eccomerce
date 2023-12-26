package com.example.eccomerce.data.api.order.api

import com.example.eccomerce.data.api.order.dto.Billing
import com.example.eccomerce.data.api.order.dto.CartRequest
import com.example.eccomerce.data.api.order.dto.OrderDto
import com.example.eccomerce.data.api.order.dto.Track
 import com.example.eccomerce.domain.model.Status
import com.google.gson.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApi {

    @POST("orders/get-billing")
    suspend fun getBilling(@Body request: CartRequest): Billing

    @POST("orders")
    suspend fun createOrder(@Body request: CartRequest)

    @GET("orders")
    suspend fun getOrders(
        @Query("status") status: Status,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<OrderDto>

    @GET("orders/{id}/track")
    suspend fun getTrack(
        @Path("id")id:Int
    ):Track

    @GET("https://maps.googleapis.com/maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin")origin:String,
        @Query("destination")destination:String,
        @Query("waypoints") waypoints:String,
        @Query("key") key:String = "AIzaSyCsCkw_FsKk5y3R3T6xNHdFSh_JZHFLkdk"
    ): JsonElement

}