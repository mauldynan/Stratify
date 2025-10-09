package com.example.workspave

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.workspave.databinding.ItemStatusOptionBinding

data class StatusOption(val name: String, val colorResId: Int)

class StatusAdapter(
    private val options: List<StatusOption>,
    private val onOptionClick: (StatusOption) -> Unit
) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    inner class StatusViewHolder(val binding: ItemStatusOptionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = ItemStatusOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val option = options[position]
        holder.binding.tvStatusName.text = option.name

        // Logika untuk memilih background dengan sudut yang benar
        val backgroundRes = when (position) {
            0 -> R.drawable.dropdown_top_item_background // Item pertama
            options.size - 1 -> R.drawable.dropdown_bottom_item_background // Item terakhir
            else -> R.drawable.dropdown_middle_item_background // Item di tengah
        }
        holder.itemView.setBackgroundResource(backgroundRes)
        // Menerapkan warna status yang sesuai
        holder.itemView.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, option.colorResId)

        holder.itemView.setOnClickListener {
            onOptionClick(option)
        }
    }

    override fun getItemCount(): Int = options.size
}