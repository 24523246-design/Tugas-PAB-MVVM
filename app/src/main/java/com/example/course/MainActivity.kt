package com.example.course

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.course.data.TaskData
import com.example.course.ui.TaskUiState
import com.example.course.ui.TaskViewModel
import com.example.course.ui.theme.CourseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TaskApp()
                }
            }
        }
    }
}

@Composable
fun TaskApp(viewModel: TaskViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val isExpandedScreen = maxWidth >= 600.dp

            if (isExpandedScreen) {
                // Layout Berdampingan (Dual Pane) untuk Layar Lebar (Tablet / Landscape)
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        TaskListContent(
                            uiState = uiState,
                            onSearchQueryChange = viewModel::onSearchQueryChange,
                            onSelectTask = viewModel::onSelectTask,
                            onToggleCompletion = viewModel::onToggleTaskCompletion,
                            onOpenAddDialog = { viewModel.onOpenAddDialog(true) },
                            onDeleteTask = viewModel::onDeleteTask
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1.2f)
                            .fillMaxHeight()
                    ) {
                        if (uiState.selectedTask != null) {
                            TaskDetailContent(
                                task = uiState.selectedTask!!,
                                showBackButton = false,
                                onBack = { viewModel.onSelectTask(null) },
                                onToggleCompletion = { viewModel.onToggleTaskCompletion(it) },
                                onDelete = { viewModel.onDeleteTask(it) }
                            )
                        } else {
                            EmptyTaskDetailPlaceholder()
                        }
                    }
                }
            } else {
                // Layout Bertumpuk (Single Pane) untuk Layar Sempit (HP Portrait)
                BackHandler(enabled = uiState.selectedTask != null) {
                    viewModel.onSelectTask(null)
                }

                if (uiState.selectedTask == null) {
                    TaskListContent(
                        uiState = uiState,
                        onSearchQueryChange = viewModel::onSearchQueryChange,
                        onSelectTask = viewModel::onSelectTask,
                        onToggleCompletion = viewModel::onToggleTaskCompletion,
                        onOpenAddDialog = { viewModel.onOpenAddDialog(true) },
                        onDeleteTask = viewModel::onDeleteTask
                    )
                } else {
                    TaskDetailContent(
                        task = uiState.selectedTask!!,
                        showBackButton = true,
                        onBack = { viewModel.onSelectTask(null) },
                        onToggleCompletion = { viewModel.onToggleTaskCompletion(it) },
                        onDelete = { viewModel.onDeleteTask(it) }
                    )
                }
            }
        }
    }

    if (uiState.isAddDialogOpen) {
        AddTaskDialog(
            uiState = uiState,
            onTitleChange = viewModel::onInputTitleChange,
            onCourseNameChange = viewModel::onInputCourseNameChange,
            onDeadlineChange = viewModel::onInputDeadlineChange,
            onDescriptionChange = viewModel::onInputDescriptionChange,
            onDismiss = { viewModel.onOpenAddDialog(false) },
            onConfirm = viewModel::onAddTask
        )
    }
}

@Composable
fun TaskListContent(
    uiState: TaskUiState,
    onSearchQueryChange: (String) -> Unit,
    onSelectTask: (TaskData) -> Unit,
    onToggleCompletion: (TaskData) -> Unit,
    onOpenAddDialog: () -> Unit,
    onDeleteTask: (TaskData) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Tugas Kuliah",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Pencatatan & Pengelolaan Tugas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(onClick = onOpenAddDialog) {
                Text("+ Tambah")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Cari tugas atau matakuliah...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.filteredTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada tugas kuliah yang ditemukan.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = uiState.filteredTasks,
                    key = { it.id }
                ) { task ->
                    val isSelected = uiState.selectedTask?.id == task.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectTask(task) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = task.isCompleted,
                                onCheckedChange = { onToggleCompletion(task) }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = task.courseName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Tenggat: ${task.deadline}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(onClick = { onDeleteTask(task) }) {
                                Text(
                                    text = "✕",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskDetailContent(
    task: TaskData,
    showBackButton: Boolean,
    onBack: () -> Unit,
    onToggleCompletion: (TaskData) -> Unit,
    onDelete: (TaskData) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBackButton) {
                    OutlinedButton(onClick = onBack) {
                        Text("Kembali")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = { onDelete(task) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus Tugas")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineMedium,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Matakuliah: ${task.courseName}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tenggat Waktu: ${task.deadline}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { onToggleCompletion(task) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (task.isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (task.isCompleted) "Status: Selesai (Klik untuk Batal)" else "Status: Belum Selesai (Klik untuk Selesai)")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Deskripsi / Catatan Tugas:",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun EmptyTaskDetailPlaceholder() {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Pilih tugas di sebelah kiri untuk melihat detail.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AddTaskDialog(
    uiState: TaskUiState,
    onTitleChange: (String) -> Unit,
    onCourseNameChange: (String) -> Unit,
    onDeadlineChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Tugas Kuliah Baru") },
        text = {
            Column {
                if (uiState.validationError != null) {
                    Text(
                        text = uiState.validationError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = uiState.inputTitle,
                    onValueChange = onTitleChange,
                    label = { Text("Judul Tugas") },
                    isError = uiState.validationError != null && uiState.inputTitle.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.inputCourseName,
                    onValueChange = onCourseNameChange,
                    label = { Text("Nama Matakuliah") },
                    isError = uiState.validationError != null && uiState.inputCourseName.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.inputDeadline,
                    onValueChange = onDeadlineChange,
                    label = { Text("Tenggat Waktu (Contoh: 30 Okt 2025)") },
                    isError = uiState.validationError != null && uiState.inputDeadline.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.inputDescription,
                    onValueChange = onDescriptionChange,
                    label = { Text("Deskripsi / Catatan Tugas") },
                    isError = uiState.validationError != null && uiState.inputDescription.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
