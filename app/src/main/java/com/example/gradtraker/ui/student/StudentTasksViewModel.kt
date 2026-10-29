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

sealed interface StudentTaskCreationResult {
    data class Created(val task: StudentTask) : StudentTaskCreationResult
    data object MissingTitle : StudentTaskCreationResult
    data object MissingDueDate : StudentTaskCreationResult
}

enum class TaskSubmissionOutcome {
    SUCCEEDED,
    FAILED,
    CANCELLED
}

object StudentTaskStatus {
    const val TO_DO = "To Do"
    const val IN_PROGRESS = "In Progress"
    const val DONE = "Done"
}

class StudentTasksViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<StudentTask>>(emptyList())
    val tasks: StateFlow<List<StudentTask>> = _tasks.asStateFlow()

    fun addTask(title: String, assignee: String, dueDate: String): StudentTaskCreationResult {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isEmpty()) return StudentTaskCreationResult.MissingTitle
        val trimmedDueDate = dueDate.trim()
        if (trimmedDueDate.isEmpty()) return StudentTaskCreationResult.MissingDueDate

        val task = StudentTask(
            id = "task-${System.nanoTime()}",
            title = trimmedTitle,
            assignee = assignee.trim(),
            dueDate = trimmedDueDate,
            status = StudentTaskStatus.TO_DO
        )
        _tasks.value = _tasks.value + task
        return StudentTaskCreationResult.Created(task)
    }

    fun openTask(taskId: String) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId && task.status == StudentTaskStatus.TO_DO) {
                task.copy(status = StudentTaskStatus.IN_PROGRESS)
            } else {
                task
            }
        }
    }

    /** Apply a status change only from the result of the existing submission workflow. */
    fun onSubmissionResult(taskId: String, outcome: TaskSubmissionOutcome) {
        if (outcome != TaskSubmissionOutcome.SUCCEEDED) return
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId && task.status == StudentTaskStatus.IN_PROGRESS) {
                task.copy(status = StudentTaskStatus.DONE)
            } else {
                task
            }
        }
    }
}
