package com.example.eccomerce.presintation.search

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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val loading = MutableLiveData(false)
    val products = MediatorLiveData<PagingData<Product>>()
    val query = MutableLiveData(ProductQuery())
    val searches = MutableLiveData<List<String>>()

init {

    getRecentSearches()
}
    private fun getProducts()  {
        val products = productRepository.getProducts(query.value!!)
        this@SearchViewModel.products.addSource(products) {
            this@SearchViewModel.products.postValue(it)
        }
    }

    fun setInitials(category: Category?,wishlist: Boolean) {
        query.postValue(query.value!!.copy(category = category, favorite = wishlist))
        getProducts()

    }

    fun setSearch(search: String) {
        query.postValue(query.value!!.copy(search = search))
        addRecent(search)
        getProducts()
    }

    fun setLoadState(states: CombinedLoadStates) {
        val loading = states.source.refresh is LoadState.Loading
        this.loading.postValue(loading)

    }

    private fun getRecentSearches() = viewModelScope.launch {
        productRepository.getRecentSearches().collect{
            searches.postValue(it)
        }
    }

    fun clearRecents()=viewModelScope.launch {
        productRepository.clearRecents()
    }
    fun addRecent(search:String)=viewModelScope.launch {
        productRepository.addRecent(search)
    }

    fun setQuery(query: ProductQuery){
        this.query.value =query
        getProducts()
    }
}