package com.example.eccomerce.data.api.products.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.eccomerce.data.api.products.ProductApi
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.domain.model.ProductQuery

class ProductPagingSource(
    private val productApi: ProductApi,
    private val query: ProductQuery
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val key = params.key ?: 0
            val products = productApi.getProducts(
                categoryId = query.category?.id,
                page = key,
                size = params.loadSize,
                search = query.search,
                from = query.range.first,
                to = query.range.second,
                rating = query.rating,
                discount = query.discount,
                favorite = query.favorite,
                sort = query.sort.joinToString()

            )
            LoadResult.Page(
                products,
                prevKey = params.key?.let { it - 1 }?.takeIf { it > 0 },
                nextKey = if (products.isNotEmpty()) key + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}