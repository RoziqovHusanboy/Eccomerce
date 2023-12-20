package com.example.eccomerce.presintation.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.eccomerce.databinding.ItemRecentBinding

class SearchAdapter(
    private val searches: List<String>,
    private val onClick: (search: String) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(search: String) {
            binding.search.text = search
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searches[position])
    }

    override fun getItemCount() = searches.size


}