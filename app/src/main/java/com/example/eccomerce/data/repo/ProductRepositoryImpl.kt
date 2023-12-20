package com.example.eccomerce.data.repo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.eccomerce.data.api.products.ProductApi
import com.example.eccomerce.data.api.products.dto.HomeResponse
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.data.api.products.paging.ProductPagingSource
import com.example.eccomerce.data.store.CartStore
import com.example.eccomerce.data.store.SearchStore
import com.example.eccomerce.data.store.UserStore
import com.example.eccomerce.domain.model.Cart
import com.example.eccomerce.domain.model.ProductQuery
import com.example.eccomerce.domain.repo.ProductRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
    private val userStore: UserStore,
    private val searchStore: SearchStore,
    private val cartStore: CartStore
) : ProductRepository {
    override suspend fun getHome(): HomeResponse {
        val response = productApi.getHome()
        userStore.set(response.user)
        return response
    }

    override suspend fun getCategories() = productApi.getCategories()
    override fun getProducts(query: ProductQuery): LiveData<PagingData<Product>> {


        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            initialKey = 0,
            pagingSourceFactory = {
                ProductPagingSource(productApi, query)
            }
        ).liveData

    }

    override fun getRecentSearches() = searchStore.getFlow().map { it?.toList() ?: emptyList() }

    override suspend fun clearRecents() {
        searchStore.clear()
    }

    override suspend fun addRecent(search: String) {
        val recents = (searchStore.get() ?: emptyArray()).toMutableList()
        recents.remove(search)
        recents.add(0, search)
        searchStore.set(recents.toTypedArray())
    }

    override suspend fun getProduct(id: String) = productApi.getProduct(id)
    override suspend fun toggleWishlist(productId: String, wishlist: Boolean) {
        productApi.toggleWishlist(productId, wishlist)
    }

    override suspend fun setCart(cart: Cart) {
        val carts = (cartStore.get() ?: emptyArray())
            .toList()
            .filterNot { it.id == cart.id }
            .toMutableList()
        if (cart.count > 0) {
            carts.add(cart)
        }

        cartStore.set(carts.toTypedArray())


    }

    override fun getCarts()=cartStore.getFlow().map { it?.toList()?: emptyList() }

    override suspend fun clearCart() = cartStore.clear()


}