package com.example.eccomerce.data.repo


import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.eccomerce.data.api.order.api.OrderApi
import com.example.eccomerce.data.api.order.dto.CartDto
import com.example.eccomerce.data.api.order.dto.CartRequest
import com.example.eccomerce.data.api.order.dto.Track
import com.example.eccomerce.data.api.order.paging.OrderPagingSource
import com.example.eccomerce.data.socket.WebsocketClient
import com.example.eccomerce.data.store.CartStore
import com.example.eccomerce.domain.model.Status
import com.example.eccomerce.domain.repo.OrderRepository
import com.example.eccomerce.utils.DirectionsJSONParser
import com.example.eccomerce.utils.toLatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject
import java.net.URI

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

    private fun getDriverLocation(server: String) = callbackFlow<String> {
        val client = WebsocketClient(URI(server)) {
            trySend(it)
        }
        client.connect()
        awaitClose {
            client.close()
        }
    }

    override fun getDirections(track: Track) = channelFlow {
        getDriverLocation(track.server).collectLatest {
            val element = orderApi.getDirections(
                "${track.from.lat},${track.from.lon}",
                "${track.to.lat},${track.to.lon}",
                it
            )
            val parser = DirectionsJSONParser()

            send(it.toLatLng() to parser.parse(JSONObject(element.toString())))
        }


    }

}