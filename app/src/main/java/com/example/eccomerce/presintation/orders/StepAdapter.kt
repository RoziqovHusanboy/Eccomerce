package com.example.eccomerce.presintation.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.eccomerce.databinding.ItemStepsBinding
import com.example.eccomerce.domain.model.Step

class StepAdapter(private val steps: List<Step>) : RecyclerView.Adapter<StepAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemStepsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(step: Step, dividerVisible: Boolean) = with(binding) {
            icon.setImageResource(step.icon)
            title.text = root.context.getString(step.title)
            date.text = step.date
            divider.isVisible = dividerVisible
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemStepsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = steps.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(steps[position], dividerVisible = position < steps.size - 1)
    }


}