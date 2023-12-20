package com.example.eccomerce.data.api.products

import com.example.eccomerce.data.api.products.dto.Category
import com.example.eccomerce.data.api.products.dto.Detail
import com.example.eccomerce.data.api.products.dto.HomeResponse
import com.example.eccomerce.data.api.products.dto.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("home")
    suspend fun getHome():HomeResponse

    @GET("categories")
    suspend fun getCategories():List<Category>

    @GET("products")
    suspend fun getProducts(
        @Query("category_id")categoryId:String?,
        @Query("search")search:String?,
        @Query("from")from:Float?,
        @Query("to")to :Float?,
        @Query("rating")rating:Int?,
        @Query("discount")discount:Int?,
        @Query("sort")sort:String?,
        @Query("page")page:Int,
        @Query("favorite") favorite:Boolean?,
        @Query("size")size:Int
    ):List<Product>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id")id:String
    ):Detail

    @GET("products/{id}/toggle-wishlist")
    suspend fun toggleWishlist(
        @Path("id")id:String,
        @Query("wishlist")wishlist:Boolean
    )

}