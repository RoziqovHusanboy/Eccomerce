package com.example.eccomerce.domain.model

import android.os.Parcelable
import com.example.eccomerce.data.api.products.dto.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductQuery(
    val category: Category? = null,
    val search: String? = null,
    val range: Pair<Float, Float> = 0f to 10000f,
    val rating: Int? = null,
    val discount: Int? = null,
    val sort: List<Sort> = emptyList(),
    val favorite:Boolean=false
) : Parcelable

enum class Sort {
    discount, voucher, shipping, delivery
}
