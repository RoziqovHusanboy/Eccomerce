package com.example.eccomerce.presintation.filter

import SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eccomerce.data.api.products.dto.Category
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.domain.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FilterViewModel @Inject constructor(
    private val productRepository: ProductRepository
):ViewModel() {

    val categories = MutableLiveData<List<Category>>()
    val events = SingleLiveEvent<Event>()

    fun getCategories() = viewModelScope.launch {
        try {
            val result = productRepository.getCategories()
            categories.postValue(result)
        }catch (e:Exception){
            events.postValue( Event.CategoriesError)
        }

    }

    sealed class Event{
        object CategoriesError:Event()
    }
}