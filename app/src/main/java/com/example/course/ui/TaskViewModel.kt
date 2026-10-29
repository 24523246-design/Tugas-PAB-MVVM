package com.example.course.ui

import androidx.lifecycle.ViewModel
import com.example.course.data.TaskData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class TaskViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { currentState ->
            currentState.copy(searchQuery = newQuery)
        }
    }

    fun onSelectTask(task: TaskData?) {
        _uiState.update { currentState ->
            currentState.copy(selectedTask = task)
        }
    }

    fun onToggleTaskCompletion(task: TaskData) {
        _uiState.update { currentState ->
            val updatedList = currentState.allTasks.map {
                if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
            }
            val updatedSelected = if (currentState.selectedTask?.id == task.id) {
                currentState.selectedTask.copy(isCompleted = !currentState.selectedTask.isCompleted)
            } else {
                currentState.selectedTask
            }
            currentState.copy(
                allTasks = updatedList,
                selectedTask = updatedSelected
            )
        }
    }

    fun onOpenAddDialog(open: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isAddDialogOpen = open,
                inputTitle = "",
                inputCourseName = "",
                inputDeadline = "",
                inputDescription = "",
                validationError = null
            )
        }
    }

    fun onInputTitleChange(title: String) {
        _uiState.update { currentState ->
            currentState.copy(
                inputTitle = title,
                validationError = if (title.isNotBlank()) null else currentState.validationError
            )
        }
    }

    fun onInputCourseNameChange(courseName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                inputCourseName = courseName,
                validationError = if (courseName.isNotBlank()) null else currentState.validationError
            )
        }
    }

    fun onInputDeadlineChange(deadline: String) {
        _uiState.update { currentState ->
            currentState.copy(
                inputDeadline = deadline,
                validationError = if (deadline.isNotBlank()) null else currentState.validationError
            )
        }
    }

    fun onInputDescriptionChange(description: String) {
        _uiState.update { currentState ->
            currentState.copy(
                inputDescription = description,
                validationError = if (description.isNotBlank()) null else currentState.validationError
            )
        }
    }

    fun onAddTask() {
        val currentState = _uiState.value
        val title = currentState.inputTitle.trim()
        val courseName = currentState.inputCourseName.trim()
        val deadline = currentState.inputDeadline.trim()
        val description = currentState.inputDescription.trim()

        if (title.isBlank() || courseName.isBlank() || deadline.isBlank() || description.isBlank()) {
            _uiState.update { state ->
                state.copy(validationError = "Judul, matakuliah, tenggat, dan deskripsi wajib diisi!")
            }
            return
        }

        val newTask = TaskData(
            id = UUID.randomUUID().toString(),
            title = title,
            courseName = courseName,
            deadline = deadline,
            description = description,
            isCompleted = false
        )

        _uiState.update { state ->
            state.copy(
                allTasks = listOf(newTask) + state.allTasks,
                selectedTask = newTask,
                isAddDialogOpen = false,
                inputTitle = "",
                inputCourseName = "",
                inputDeadline = "",
                inputDescription = "",
                validationError = null
            )
        }
    }

    fun onDeleteTask(task: TaskData) {
        _uiState.update { state ->
            val updatedList = state.allTasks.filterNot { it.id == task.id }
            val newSelected = if (state.selectedTask?.id == task.id) null else state.selectedTask
            state.copy(
                allTasks = updatedList,
                selectedTask = newSelected
            )
        }
    }
}
