package com.example.eccomerce.data.repo

import android.graphics.Path.Direction
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.eccomerce.data.api.order.api.OrderApi
import com.example.eccomerce.data.api.order.dto.CartDto
import com.example.eccomerce.data.api.order.dto.CartRequest
import com.example.eccomerce.data.api.order.dto.Track
import com.example.eccomerce.data.api.order.paging.OrderPagingSource
import com.example.eccomerce.data.store.CartStore
import com.example.eccomerce.domain.model.Status
import com.example.eccomerce.domain.repo.OrderRepository
import com.example.eccomerce.utils.DirectionsJSONParser
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.json.JSONObject
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi,
    private val cartStore: CartStore
) : OrderRepository {
    override fun getBilling(promo: String?) = channelFlow {
        cartStore.getFlow().distinctUntilChanged().collectLatest { carts ->
            carts ?: return@collectLatest
            val request = CartRequest(
                cart = carts.map { CartDto(id = it.id, count = it.count) },
                promoCode = promo
            )
            val billing = orderApi.getBilling(request)
            send(billing)

        }
    }

    override suspend fun createOrder(promo: String?) {
        val carts = cartStore.get() ?: return
        val request = CartRequest(
            cart = carts.map { CartDto(id = it.id, count = it.count) },
            promoCode = promo
        )
        orderApi.createOrder(request)
        cartStore.clear()
    }

    override fun getOrders(status: Status) = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 10,
            initialLoadSize = 20,
            enablePlaceholders = false
        ),
        initialKey = 0,
        pagingSourceFactory = {
            OrderPagingSource(status, orderApi)
        }
    ).flow

    override suspend fun getTrack(id: Int) = orderApi.getTrack(id)
    override suspend fun getDirections(track: Track): List<List<HashMap<String, String>>> {

        val element = orderApi.getDirections(
            "${track.from.lat}, ${track.from.lon}",
            "${track.to.lat}, ${track.to.lon}"
        )
        val parser = DirectionsJSONParser()
        return parser.parse(JSONObject(element.toString()))
    }
}