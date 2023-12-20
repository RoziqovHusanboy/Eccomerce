package com.example.eccomerce.presintation.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.data.api.products.dto.Section
import com.example.eccomerce.data.api.products.dto.SectionType
import com.example.eccomerce.databinding.ItemSectionHorizontalyBinding
import com.example.eccomerce.databinding.ItemSectionVerticalBinding

class HomeSectionAdapter(
    private val sections: List<Section>,
    private val showAll: (section: Section) -> Unit,
    private val onClickProduct: (product: Product) -> Unit,
    private val wishlist : (product: Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HorizontalViewHolder(private val binding: ItemSectionHorizontalyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(section: Section) = with(binding) {
            title.text = section.title
            showAll.setOnClickListener { this@HomeSectionAdapter.showAll(section) }
            products.adapter = HorizontalAdapter(section.products, onClickProduct, wishlist)
        }
    }

    inner class VerticalViewHolder(private val binding: ItemSectionVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(section: Section) = with(binding) {
            title.text = section.title
            showAll.setOnClickListener { this@HomeSectionAdapter.showAll(section) }
            products.adapter = VerticalAdapter(section.products, onClickProduct, wishlist)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (SectionType.values()[viewType]) {
            SectionType.horizontal -> HorizontalViewHolder(
                ItemSectionHorizontalyBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )

            SectionType.vertical -> VerticalViewHolder(
                ItemSectionVerticalBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount() = sections.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HorizontalViewHolder -> holder.bind(sections[position])
            is VerticalViewHolder -> holder.bind(sections[position])

        }
    }

    override fun getItemViewType(position: Int) = sections[position].type.ordinal

}