package com.example.eccomerce.presintation.search.adapters

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eccomerce.R
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.databinding.ItemProductSearchBinding

class SearchProductViewHolder(private val binding: ItemProductSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        product: Product,
        onClick: (product: Product) -> Unit,
        like: (product: Product) -> Unit
    ) = with(binding) {
        Glide.with(root).load(product.image).into(image)
        name.text = product.title
        rating.text = String.format("%.1f", product.rating)
        ratingCount.text =
            root.context.getString(R.string.item_product_ratings_count, product.ratingCount)

        val currentPrice = product.price - (product.discount ?: 0.0)
        price.text = root.context.getString(R.string.item_product_price, currentPrice)
        oldPrice.text =
            root.context.getString(R.string.item_product_price_striked, product.price)
        oldPrice.isVisible = product.discount != null

        root.setOnClickListener {
            onClick(product)
        }
    }
}