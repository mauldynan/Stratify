package com.example.scrum_section

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.scrum_section.model.Task
import com.example.scrum_section.R

class TaskDetailDialog(private val task: Task) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        // Hilangkan title bar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_task_detail)

        // Ambil view
        val tvName = dialog.findViewById<TextView>(R.id.tvName)
        val tvDeadline = dialog.findViewById<TextView>(R.id.tvDeadline)
        val tvDepartment = dialog.findViewById<TextView>(R.id.tvDepartment)
        val tvDescription = dialog.findViewById<TextView>(R.id.tvDescription)
        val btnClose = dialog.findViewById<TextView>(R.id.btnClose)
        val underlineView = dialog.findViewById<View>(R.id.underlineView)

        // Isi data
        tvName.text = task.name
        tvDeadline.text = task.deadline
        tvDepartment.text = task.department
        tvDescription.text =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."

        // Biar garis di bawah judul sesuai panjang teks
        tvName.post {
            val params = underlineView.layoutParams
            params.width = tvName.width
            underlineView.layoutParams = params
        }

        // Tombol Close
        btnClose.setOnClickListener { dismiss() }

        // Atur ukuran & tampilan dialog
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }
}
