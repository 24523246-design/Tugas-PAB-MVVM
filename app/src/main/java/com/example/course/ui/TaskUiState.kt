package com.example.course.ui

import com.example.course.data.TaskData
import com.example.course.data.initialTasks

data class TaskUiState(
    val allTasks: List<TaskData> = initialTasks,
    val searchQuery: String = "",
    val selectedTask: TaskData? = null,
    val isAddDialogOpen: Boolean = false,
    val inputTitle: String = "",
    val inputCourseName: String = "",
    val inputDeadline: String = "",
    val inputDescription: String = "",
    val validationError: String? = null
) {
    val filteredTasks: List<TaskData>
        get() = if (searchQuery.isBlank()) {
            allTasks
        } else {
            allTasks.filter { task ->
                task.title.contains(searchQuery, ignoreCase = true) ||
                        task.courseName.contains(searchQuery, ignoreCase = true)
            }
        }
}
