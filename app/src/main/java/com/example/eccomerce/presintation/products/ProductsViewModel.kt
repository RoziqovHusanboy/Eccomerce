package com.example.eccomerce.presintation.products

import android.annotation.SuppressLint
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.example.eccomerce.data.api.products.dto.Category
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.domain.model.ProductQuery
import com.example.eccomerce.domain.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    val loading = MutableLiveData(false)
    val error = MutableLiveData(false)
    val products = MediatorLiveData<PagingData<Product>>()
    val category = MutableLiveData<Category>()


    fun setCategory(category: Category) {
        this.category.postValue(category)
        getProducts()
    }

      fun getProducts()   {
        val query = ProductQuery(category = category.value)
       val products = productRepository.getProducts(query)
        this@ProductsViewModel.products.addSource(products){
            this@ProductsViewModel.products.postValue(it)
        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun setLoadStates(states:CombinedLoadStates){
    val loading = states.source.refresh is LoadState.Loading
        this.loading.postValue(loading)
    }

    fun toggleWishlist(product: Product) = viewModelScope.launch {
        try {
            productRepository.toggleWishlist(product.id,product.wishlist)
        }catch (e:Exception){

        }
    }
}