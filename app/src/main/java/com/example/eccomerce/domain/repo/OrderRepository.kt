package com.example.eccomerce.domain.repo

import androidx.paging.PagingData
import com.example.eccomerce.data.api.order.dto.Billing
import com.example.eccomerce.data.api.order.dto.Track
import com.example.eccomerce.domain.model.Cart
import com.example.eccomerce.domain.model.Order
import com.example.eccomerce.domain.model.Status
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    fun getBilling( promo: String? = null): Flow<Billing>
   suspend fun createOrder(promo: String?=null)
   fun getOrders(status: Status): Flow<PagingData<Order>>
   suspend fun getTrack(id:Int):Track
   suspend fun getDirections(track: Track):List<List<HashMap<String,String>>>

}