package com.example.eccomerce.presintation.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomerce.R
import com.example.eccomerce.databinding.ItemStatusBinding
import com.example.eccomerce.domain.model.Status

class StatusAdapter(
    var selected: Status,
    var onSelected: (status: Status) -> Unit
) : RecyclerView.Adapter<StatusAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(status: Status) = with(binding) {

            val pair = when (status) {
                Status.all -> R.string.status_all to R.color.white
                Status.delivering -> R.string.status_delivering to R.color.yellow
                Status.completed -> R.string.status_completed to R.color.green
                Status.cancelled -> R.string.status_canceled to R.color.red
            }
            title.text = root.context.getString(pair.first)
            
            indicator.isVisible = status != Status.all
            indicator.setColorFilter(ContextCompat.getColor(root.context, pair.second))

            val dividerColor = if (selected == status) R.color.orange else R.color.gray_1
            divider.dividerColor = ContextCompat.getColor(root.context, dividerColor)

            root.setOnClickListener {
                onSelected(status)
                val old = selected
                selected = status
                notifyItemChanged(old.ordinal)
                notifyItemChanged(status.ordinal)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = Status.values().size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(Status.values()[position])
    }
}