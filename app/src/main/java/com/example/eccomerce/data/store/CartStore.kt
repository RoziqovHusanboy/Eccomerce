package com.example.eccomerce.data.store

import com.example.eccomerce.domain.model.Cart
import javax.inject.Inject

class CartStore @Inject constructor():BaseStore<Array<Cart>>("cart", Array<Cart>::class.java) {
}