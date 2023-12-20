package com.example.eccomerce.presintation.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eccomerce.data.api.products.dto.Category
import com.example.eccomerce.data.repo.ProductRepositoryImpl
import com.example.eccomerce.domain.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel() {

    val loading = MutableLiveData(false)
    val error = MutableLiveData(false)
    val categories = MutableLiveData<List<Category>>()

    init {
        getCategories()
    }

    fun getCategories() = viewModelScope.launch {
        loading.postValue(true)
        error.postValue(false)
        try {
            val response = productRepository.getCategories()
            categories.postValue(response)
        }catch (e:Exception){
            error.postValue(true)
        }finally {
            loading.postValue(false)
        }
    }


}