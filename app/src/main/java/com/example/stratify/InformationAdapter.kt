package com.example.stratify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.stratify.databinding.ItemInformationBinding
import java.text.SimpleDateFormat
import java.util.Locale

class InformationAdapter(
    private val items: List<InformationItem>,
    private val onActionClick: (InformationItem, Int) -> Unit,
    private val onTextChange: (InformationItem, String) -> Unit
) : RecyclerView.Adapter<InformationAdapter.InformationViewHolder>() {

    private var isDeleteModeActive: Boolean = false

    // Fungsi ini akan dipanggil dari Fragment untuk mengubah mode
    fun setDeleteMode(isActive: Boolean) {
        isDeleteModeActive = isActive
        notifyDataSetChanged()
    }

    inner class InformationViewHolder(val binding: ItemInformationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationViewHolder {
        val binding = ItemInformationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InformationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InformationViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvInfoDetails.text = item.details
        holder.binding.etInfoDetails.setText(item.details)

        holder.binding.tvInfoDetails.isVisible = !item.isEditing
        holder.binding.etInfoDetails.isVisible = item.isEditing

        // LOGIKA UTAMA ADA DI SINI
        if (isDeleteModeActive) {
            // --- JIKA MODE DELETE AKTIF ---
            holder.binding.tvSavedLabel.visibility = View.GONE
            holder.binding.tvInfoDate.visibility = View.GONE

            // Tampilkan ikon X jika item ini terpilih untuk dihapus
            if (item.isSelectedForDeletion) {
                holder.binding.ivActionIcon.setImageResource(R.drawable.ic_close_circle)
            } else {
                // Jika tidak terpilih, tampilkan ikon save/unsave seperti biasa
                if (item.isSaved) {
                    holder.binding.ivActionIcon.setImageResource(R.drawable.ic_check_circle)
                } else {
                    holder.binding.ivActionIcon.setImageResource(R.drawable.ic_radio_button_unchecked)
                }
            }
            // Klik pada ikon akan men-toggle status pilihan
            holder.binding.ivActionIcon.setOnClickListener {
                item.isSelectedForDeletion = !item.isSelectedForDeletion
                notifyItemChanged(position)
            }

        } else {
            // --- JIKA MODE DELETE TIDAK AKTIF (NORMAL) ---
            if (item.isSaved) {
                holder.binding.ivActionIcon.setImageResource(R.drawable.ic_check_circle)
                holder.binding.tvSavedLabel.visibility = View.VISIBLE
                holder.binding.tvInfoDate.visibility = View.VISIBLE
                item.savedDate?.let {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    holder.binding.tvInfoDate.text = sdf.format(it)
                }
            } else {
                holder.binding.ivActionIcon.setImageResource(R.drawable.ic_radio_button_unchecked)
                holder.binding.tvSavedLabel.visibility = View.GONE
                holder.binding.tvInfoDate.visibility = View.GONE
            }

            if (item.isEditing) {
                holder.binding.etInfoDetails.requestFocus()
                holder.binding.ivActionIcon.setImageResource(R.drawable.ic_check_circle)
            }

            holder.binding.ivActionIcon.setOnClickListener {
                onActionClick(item, position)
            }
        }

        // Listener untuk EditText tetap sama
        holder.binding.etInfoDetails.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onTextChange(item, textView.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}