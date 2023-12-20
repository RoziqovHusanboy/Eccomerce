package com.example.eccomerce.presintation.cart

import SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eccomerce.data.api.order.dto.Billing
import com.example.eccomerce.domain.model.Cart
import com.example.eccomerce.domain.repo.OrderRepository
import com.example.eccomerce.domain.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    val carts = MutableLiveData<List<Cart>>()
    val event = SingleLiveEvent<Event>()
    val billing = MutableLiveData<Billing>()
    val billingLoading = MutableLiveData(false)
    val loading = MutableLiveData(false)
    init {
        getCarts()
        getBilling()
    }


    private fun getCarts() = viewModelScope.launch {
        productRepository.getCarts().collectLatest {
            carts.postValue(it)
        }

    }

    fun set(cart: Cart) = viewModelScope.launch {
        productRepository.setCart(cart)
    }

    fun clear() = viewModelScope.launch {
        productRepository.clearCart()
    }

    private var job:Job? =null
    fun getBilling(promo: String? = null) {
        job?.cancel()
        job = viewModelScope.launch {
            billingLoading.postValue(true)
            orderRepository.getBilling(promo).catch {
                event.postValue(Event.BillingError)
                billingLoading.postValue(false)
            }.collectLatest {
                billingLoading.postValue(false)
                billing.postValue(it)
            }
        }
    }
        fun createOrder(promo: String? = null) = viewModelScope.launch{
        loading.postValue(true)
            try {
                orderRepository.createOrder(promo)
                event.postValue(Event.orderCreated)
            }catch (e:Exception){
                event.postValue(Event.orderError)
            }finally {
                loading.postValue(false)
            }
        }
    sealed class Event {
        object BillingError : Event()
        object orderError:Event()
        object orderCreated:Event()
    }

}