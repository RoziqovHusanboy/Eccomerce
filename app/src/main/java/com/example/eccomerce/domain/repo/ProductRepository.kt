package com.example.eccomerce.domain.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.eccomerce.data.api.products.dto.Category
import com.example.eccomerce.data.api.products.dto.Detail
import com.example.eccomerce.data.api.products.dto.HomeResponse
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.domain.model.Cart
import com.example.eccomerce.domain.model.ProductQuery
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getHome():HomeResponse
    suspend fun getCategories():List<Category>
  fun getProducts(query: ProductQuery):LiveData<PagingData<Product>>
      fun getRecentSearches():Flow<List<String>>
    suspend  fun clearRecents()
    suspend fun addRecent(search:String)
    suspend fun getProduct(id:String):Detail
    suspend fun toggleWishlist(productId:String,wishlist :Boolean)


    suspend fun setCart(cart: Cart)
     fun getCarts():Flow<List<Cart>>
    suspend fun clearCart()

}