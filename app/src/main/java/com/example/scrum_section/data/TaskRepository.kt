package com.example.scrum_section.data

import com.example.scrum_section.model.Task
import com.example.scrum_section.util.TaskStatus

object TaskRepository {
    private val tasks = mutableListOf<Task>()

    fun getTasks(): List<Task> = tasks

    fun addTask(task: Task) {
        tasks.add(task)
    }

    // Versi lama (biarin tetap ada kalau ada kode lain yang masih pakai index)
    fun updateTask(index: Int, updatedTask: Task) {
        tasks[index] = updatedTask
    }

    // 🔹 Versi baru (biar kompatibel sama adapter kamu

    fun updateTask(updatedTask: Task) {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            tasks[index] = updatedTask
        }
    }


    fun getTasksByStatus(status: TaskStatus): List<Task> {
        return if (status == TaskStatus.ALL) {
            tasks
        } else {
            tasks.filter { it.status == status }
        }
    }

    // Contoh data awal
    init {
        tasks.add(Task(1, "Design UI", "Reza", "7 Oktober 2025", "Frontend", TaskStatus.TODO))
        tasks.add(Task(2, "Setup Database", "Hansen", "8 Oktober 2025", "Backend", TaskStatus.IN_PROGRESS))
        tasks.add(Task(3, "Integrate API", "Reza", "9 Oktober 2025", "Backend", TaskStatus.TO_VERIFY))
        tasks.add(Task(4, "Testing", "Hansen", "10 Oktober 2025", "QA", TaskStatus.DONE))
    }
}
