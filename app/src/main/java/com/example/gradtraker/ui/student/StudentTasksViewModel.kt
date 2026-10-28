package com.example.gradtraker.ui.student

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class StudentTask(
    val id: String,
    val title: String,
    val assignee: String,
    val dueDate: String,
    val status: String
)

class StudentTasksViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<StudentTask>>(emptyList())
    val tasks: StateFlow<List<StudentTask>> = _tasks.asStateFlow()

    fun addTask(title: String, assignee: String, dueDate: String) {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isEmpty()) return
        _tasks.value = _tasks.value + StudentTask(
            id = "task-${System.nanoTime()}",
            title = trimmedTitle,
            assignee = assignee.trim(),
            dueDate = dueDate.trim(),
            status = "To do"
        )
    }

    fun advanceStatus(taskId: String) {
        val nextStatus = mapOf("To do" to "In progress", "In progress" to "Completed", "Completed" to "To do")
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) task.copy(status = nextStatus[task.status] ?: "To do") else task
        }
    }
}
