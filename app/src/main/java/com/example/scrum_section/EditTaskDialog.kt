package com.example.scrum_section

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.scrum_section.data.TaskRepository
import com.example.scrum_section.model.Task

class EditTaskDialog(private val task: Task, private val onUpdated: () -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_task)

        val etName = dialog.findViewById<EditText>(R.id.etName)
        val etDeadline = dialog.findViewById<EditText>(R.id.etDeadline)
        val etDepartment = dialog.findViewById<EditText>(R.id.etDepartment)
        val btnAdd = dialog.findViewById<Button>(R.id.btnAdd)

        etName.setText(task.name)
        etDeadline.setText(task.deadline)
        etDepartment.setText(task.department)
        btnAdd.text = "Update"

        btnAdd.setOnClickListener {
            val newName = etName.text.toString().trim()
            val newDeadline = etDeadline.text.toString().trim()
            val newDept = etDepartment.text.toString().trim()

            if (newName.isNotEmpty()) {
                task.name = newName
                task.deadline = newDeadline
                task.department = newDept

                TaskRepository.updateTask(task)
                Toast.makeText(requireContext(), "Task updated succesfully!", Toast.LENGTH_SHORT).show()

                onUpdated()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Task's name not allowed empty!", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }
}
