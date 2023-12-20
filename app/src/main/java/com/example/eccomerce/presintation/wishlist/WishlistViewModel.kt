package com.example.eccomerce.presintation.wishlist

import android.annotation.SuppressLint
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.domain.model.ProductQuery
import com.example.eccomerce.domain.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val productRepository: ProductRepository
) :ViewModel() {

    val loading = MutableLiveData(false)
    val error = MutableLiveData(false)
    val products = MediatorLiveData<PagingData<Product>>()
init {
    getProducts()
}
    fun getProducts()   {
        val query = ProductQuery(favorite = true)
        val products = productRepository.getProducts(query).cachedIn(viewModelScope)
        this@WishlistViewModel.products.addSource(products){
            this@WishlistViewModel.products.postValue(it)
        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun setLoadStates(states: CombinedLoadStates){
        val loading = states.source.refresh is LoadState.Loading
        this.loading.postValue(loading)
    }


}