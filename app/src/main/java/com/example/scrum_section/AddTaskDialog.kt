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
import com.example.scrum_section.util.TaskStatus

class AddTaskDialog(private val onTaskAdded: () -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_task)

        val etName = dialog.findViewById<EditText>(R.id.etName)
        val etDeadline = dialog.findViewById<EditText>(R.id.etDeadline)
        val etDepartment = dialog.findViewById<EditText>(R.id.etDepartment)
        val btnAdd = dialog.findViewById<Button>(R.id.btnAdd)

        btnAdd.setOnClickListener {
            val name = etName.text.toString().trim()
            val deadline = etDeadline.text.toString().trim()
            val department = etDepartment.text.toString().trim()

            if (name.isNotEmpty()) {
                val newTask = Task(
                    id = TaskRepository.getTasks().size + 1,
                    name = name,
                    createdBy = "You",
                    deadline = deadline,
                    department = department,
                    status = TaskStatus.TODO
                )

                TaskRepository.addTask(newTask)
                Toast.makeText(requireContext(), "Task created succesfully!", Toast.LENGTH_SHORT).show()

                onTaskAdded()
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
