package com.example.eccomerce.presintation.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eccomerce.R
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.databinding.ItemProductBinding
import com.example.eccomerce.presintation.categories.CategoriesAdapter
import com.example.eccomerce.presintation.products.ProductViewHolder
import com.example.eccomerce.presintation.products.ProductsAdapter
import kotlin.math.roundToInt

class VerticalAdapter(
    private val products: List<Product>,
    private val onclick: (product: Product) -> Unit,
    private val wishlist: (product: Product) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = products.size
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position],onclick,wishlist)
    }

}