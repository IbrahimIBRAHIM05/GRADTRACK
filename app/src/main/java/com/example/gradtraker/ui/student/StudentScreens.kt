package com.example.gradtraker.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

private val PageBackground = Color(0xFFF8F4EA)
private val BrandGreen = Color(0xFF25463B)
private val SecondaryGreen = Color(0xFF4F7A64)
private val LightGreen = Color(0xFFE6EEDF)
private val NavyText = Color(0xFF27344C)

@Composable
fun StudentTasksScreen(
    viewModel: StudentTasksViewModel,
    onOpenTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    var showAddTask by rememberSaveable { mutableStateOf(false) }
    var addTaskMessage by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PageBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Keep your team work organized.", color = SecondaryGreen)
            }
            Button(
                onClick = { showAddTask = true },
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
            ) { Text("+ Add task") }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StudentTaskSummary("Total tasks", tasks.size, Modifier.weight(1f))
            StudentTaskSummary("In progress", tasks.count { it.status == StudentTaskStatus.IN_PROGRESS }, Modifier.weight(1f))
            StudentTaskSummary("Done", tasks.count { it.status == StudentTaskStatus.DONE }, Modifier.weight(1f))
        }

        StudentPanel(title = "Team task board") {
            if (tasks.isEmpty()) {
                StudentEmptyState("No tasks yet", "Tasks created for this session will appear here.")
            } else {
                tasks.forEach { task ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenTask(task.id) }
                            .padding(vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(task.title, color = NavyText, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            TaskStatusBadge(task.status)
                        }
                        Spacer(Modifier.height(4.dp))
                        val assigneeText = task.assignee.ifBlank { "Unassigned" }
                        val dueText = task.dueDate.ifBlank { "No due date" }
                        Text("Assigned to $assigneeText · $dueText", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                        Text("Open task details.", color = SecondaryGreen, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
        addTaskMessage?.let { Text(it, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall) }
    }

    if (showAddTask) {
        AddTaskDialog(
            onDismiss = { showAddTask = false },
            onAdd = { title, assignee, dueDate ->
                when (val result = viewModel.addTask(title, assignee, dueDate)) {
                    is StudentTaskCreationResult.Created -> {
                        addTaskMessage = "Task added for this session only; it is not permanently saved."
                        showAddTask = false
                    }
                    StudentTaskCreationResult.MissingTitle -> addTaskMessage = "Enter a task title."
                    StudentTaskCreationResult.MissingDueDate -> addTaskMessage = "A due date is required."
                }
            }
        )
    }
}

@Composable
private fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String, String, String) -> Unit) {
    var title by rememberSaveable { mutableStateOf("") }
    var assignee by rememberSaveable { mutableStateOf("") }
    var dueDate by rememberSaveable { mutableStateOf("") }
    var validationMessage by rememberSaveable { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it; validationMessage = null }, label = { Text("Task title") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(assignee, { assignee = it }, label = { Text("Assignee (optional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(
                    dueDate,
                    { dueDate = it; validationMessage = null },
                    label = { Text("Due date *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = validationMessage == "A due date is required."
                )
                validationMessage?.let { Text(it, color = Color(0xFFB3261E), style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isBlank()) validationMessage = "Enter a task title."
                    else if (dueDate.isBlank()) validationMessage = "A due date is required."
                    else onAdd(title, assignee, dueDate)
                }
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun StudentProfileScreen(
    viewModel: StudentProfileViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var currentPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var passwordMessage by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PageBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column {
            Text("Keep your graduation project organized and moving forward.", color = SecondaryGreen)
        }

        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = LightGreen, shape = CircleShape, modifier = Modifier.size(52.dp)) {
                    Text(
                        text = initialsOrPlaceholder(state.profile.fullName),
                        color = BrandGreen,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 14.dp, top = 14.dp)
                    )
                }
                Column(Modifier.padding(start = 12.dp).weight(1f)) {
                    Text(state.profile.fullName.ifBlank { "Student" }, color = NavyText, fontWeight = FontWeight.SemiBold)
                    Text(state.profile.email.ifBlank { "Email not provided" }, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                    Surface(color = LightGreen, shape = RoundedCornerShape(50), modifier = Modifier.padding(top = 6.dp)) {
                        Text("STUDENT", color = SecondaryGreen, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp))
                    }
                }
                if (!state.isEditing) {
                    TextButton(onClick = viewModel::beginEditing) { Text("Edit profile", color = BrandGreen) }
                }
            }
        }

        StudentPanel(title = "Personal information") {
            ProfileValue("Full name", state.draft.fullName, state.isEditing) { text ->
                viewModel.updateDraft { it.copy(fullName = text) }
            }
            ProfileValue("University ID", state.draft.universityId, state.isEditing) { text ->
                viewModel.updateDraft { it.copy(universityId = text) }
            }
            ProfileValue("Email address", state.draft.email, state.isEditing) { text ->
                viewModel.updateDraft { it.copy(email = text) }
            }
            ProfileValue("Department", state.draft.department, state.isEditing) { text ->
                viewModel.updateDraft { it.copy(department = text) }
            }
            ProfileValue("Academic year", state.draft.academicYear, state.isEditing) { text ->
                viewModel.updateDraft { it.copy(academicYear = text) }
            }
            if (state.isEditing) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = viewModel::saveForSession, colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)) {
                        Text("Save for this session")
                    }
                    OutlinedButton(onClick = viewModel::cancelEditing) { Text("Cancel", color = BrandGreen) }
                }
            }
            state.message?.let { Text(it, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall) }
        }

        StudentPanel(title = "Security") {
            Text("Password changes are not connected to an account service.", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
            OutlinedTextField(
                currentPassword,
                { currentPassword = it; passwordMessage = null },
                label = { Text("Current password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                newPassword,
                { newPassword = it; passwordMessage = null },
                label = { Text("New password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Button(
                onClick = {
                    passwordMessage = "Password was not changed: no account service is connected."
                    currentPassword = ""
                    newPassword = ""
                },
                enabled = currentPassword.isNotBlank() && newPassword.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
            ) { Text("Update password") }
            passwordMessage?.let { Text(it, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Composable
private fun ProfileValue(
    label: String,
    text: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    if (isEditing) {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    } else {
        Column(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
            Text(label, color = SecondaryGreen, style = MaterialTheme.typography.labelMedium)
            Text(text.ifBlank { "Not provided" }, color = NavyText)
        }
    }
}

@Composable
private fun StudentPanel(title: String, content: @Composable () -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, color = NavyText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}

@Composable
private fun StudentTaskSummary(title: String, count: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(10.dp)) {
            Text(count.toString(), color = NavyText, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Text(title, color = SecondaryGreen, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun TaskStatusBadge(status: String) {
    Surface(color = LightGreen, shape = RoundedCornerShape(50)) {
        Text(status, color = SecondaryGreen, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
    }
}

@Composable
fun StudentTaskDetailScreen(task: StudentTask, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PageBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StudentPanel(title = task.title) {
            ProfileValue("Due date", task.dueDate, false) {}
            ProfileValue("Assignee", task.assignee.ifBlank { "Unassigned" }, false) {}
            TaskStatusBadge(task.status)
        }
        Text(
            "Task changes are held in memory for this session. Task submission is not connected in this app.",
            color = SecondaryGreen,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun StudentEmptyState(title: String, message: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Surface(color = LightGreen, shape = RoundedCornerShape(10.dp), modifier = Modifier.size(40.dp)) {
            androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) { Text("—", color = SecondaryGreen) }
        }
        Column(Modifier.padding(start = 12.dp)) {
            Text(title, color = BrandGreen, fontWeight = FontWeight.SemiBold)
            Text(message, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun initialsOrPlaceholder(name: String): String =
    name.trim().split(Regex("\\s+")).filter(String::isNotBlank).take(2).joinToString("") { it.first().uppercase() }.ifBlank { "S" }
