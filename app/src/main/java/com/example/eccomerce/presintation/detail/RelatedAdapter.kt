package com.example.eccomerce.presintation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eccomerce.R
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.databinding.ItemProductRelatedBinding

class RelatedAdapter(
    private val products: List<Product>,
    private val onClick: (product: Product) -> Unit
) : RecyclerView.Adapter<RelatedAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProductRelatedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) = with(binding) {
            Glide.with(root).load(product.image).into(image)

            name.text = product.title


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemProductRelatedBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )


    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

}