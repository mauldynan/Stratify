package com.example.workspave

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.workspave.databinding.ItemWorkspaceBinding

class WorkspaceAdapter(
    private val items: List<Workspace>,
    private val onItemClick: (Workspace) -> Unit
) : RecyclerView.Adapter<WorkspaceAdapter.WorkspaceViewHolder>() {

    inner class WorkspaceViewHolder(val binding: ItemWorkspaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkspaceViewHolder {
        val binding = ItemWorkspaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkspaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkspaceViewHolder, position: Int) {
        val workspace = items[position]
        holder.binding.tvTaskName.text = workspace.name
        holder.binding.tvCreatorName.text = "by ${workspace.creatorName}"
        holder.binding.ivProfile.setImageResource(R.drawable.ic_profile_placeholder)

        // Logika baru untuk status
        holder.binding.tvStatus.text = workspace.status
        val colorRes = when (workspace.status) {
            "In Progress" -> R.color.status_inprogress
            "To Verify" -> R.color.status_toverify
            "Done" -> R.color.status_done
            else -> R.color.status_todo
        }
        holder.binding.tvStatus.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, colorRes)

        holder.itemView.setOnClickListener {
            onItemClick(workspace)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}