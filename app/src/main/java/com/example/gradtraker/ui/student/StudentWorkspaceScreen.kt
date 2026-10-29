package com.example.gradtraker.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gradtraker.navigation.RouteHistory
import com.example.gradtraker.navigation.GradTrackBottomNavigation
import com.example.gradtraker.navigation.GradTrackDestination
import com.example.gradtraker.navigation.GradTrackPageTopBar
import com.example.gradtraker.ui.notifications.NotificationsScreen
import com.example.gradtraker.ui.notifications.NotificationsViewModel

private val PageBackground = Color(0xFFF8F4EA)
private val BrandGreen = Color(0xFF25463B)
private val SecondaryGreen = Color(0xFF4F7A64)
private val LightGreen = Color(0xFFE6EEDF)
private val NavyText = Color(0xFF27344C)

private data class StudentDestination(val title: String, val icon: String)

private val studentDestinations = listOf(
    StudentDestination("Dashboard", "⌂"),
    StudentDestination("My Project", "□"),
    StudentDestination("My Team", "♧"),
    StudentDestination("Tasks", "✓"),
    StudentDestination("Submissions", "⇧"),
    StudentDestination("Notifications", "◇"),
    StudentDestination("Profile & Settings", "⚙")
)

@Composable
fun StudentWorkspaceScreen(onSwitchWorkspace: () -> Unit) {
    var routeHistory by rememberSaveable { mutableStateOf("Dashboard") }
    val currentRoute = RouteHistory.current(routeHistory)
    val selectedTopLevel = if (currentRoute == "Task Details") RouteHistory.parent(routeHistory) else currentRoute
    var selectedTaskId by rememberSaveable { mutableStateOf<String?>(null) }
    val saveableStateHolder = rememberSaveableStateHolder()
    val tasksViewModel: StudentTasksViewModel = viewModel()
    val notificationViewModel: NotificationsViewModel = viewModel(key = "student-notifications")

    fun selectTopLevel(route: String) {
        routeHistory = RouteHistory.selectTopLevel(route)
    }

    fun navigate(route: String) {
        routeHistory = RouteHistory.push(routeHistory, route)
    }

    fun openTask(taskId: String) {
        if (tasksViewModel.tasks.value.none { it.id == taskId }) return
        selectedTaskId = taskId
        tasksViewModel.openTask(taskId)
        navigate("Task Details")
    }

    fun back() {
        routeHistory = if (RouteHistory.canGoBack(routeHistory)) {
            RouteHistory.pop(routeHistory)
        } else {
            RouteHistory.selectTopLevel("Dashboard")
        }
    }

    BackHandler(enabled = currentRoute != "Dashboard") {
        back()
    }

    Scaffold(
        containerColor = PageBackground,
        topBar = {
            if (currentRoute == "Dashboard") {
                StudentTopBar(onSwitchWorkspace = onSwitchWorkspace)
            } else {
                GradTrackPageTopBar(
                    title = if (currentRoute == "Task Details") "Task Details" else currentRoute,
                    onBack = ::back,
                    onSwitchWorkspace = onSwitchWorkspace
                )
            }
        },
        bottomBar = {
            GradTrackBottomNavigation(
                destinations = studentDestinations.map { GradTrackDestination(it.title, it.icon) },
                selectedTitle = selectedTopLevel,
                onSelect = { destination -> selectTopLevel(destination.title) }
            )
        }
    ) { innerPadding ->
        saveableStateHolder.SaveableStateProvider(currentRoute) {
            when (currentRoute) {
                "Tasks" -> StudentTasksScreen(
                    viewModel = tasksViewModel,
                    onOpenTask = ::openTask,
                    modifier = Modifier.padding(innerPadding)
                )
                "Task Details" -> {
                    val task = tasksViewModel.tasks.value.firstOrNull { it.id == selectedTaskId }
                    if (task == null) {
                        StudentFutureDestination("Task unavailable", Modifier.padding(innerPadding))
                    } else {
                        StudentTaskDetailScreen(task, Modifier.padding(innerPadding))
                    }
                }
                "Notifications" -> NotificationsScreen(
                    viewModel = notificationViewModel,
                    onNotificationSelected = { notification -> notification.taskId?.let(::openTask) },
                    modifier = Modifier.padding(innerPadding)
                )
                "Profile & Settings" -> StudentProfileScreen(
                    viewModel = viewModel(),
                    modifier = Modifier.padding(innerPadding)
                )
                else -> StudentFutureDestination(
                    title = currentRoute,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
@Composable
private fun StudentTopBar(onSwitchWorkspace: () -> Unit) {
    Surface(color = Color.White, shadowElevation = 1.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StudentAvatar()
            Column(Modifier.padding(start = 10.dp)) {
                Text("Student", color = NavyText, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text("Student workspace", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onSwitchWorkspace) { Text("Switch", color = SecondaryGreen) }
        }
    }
}

@Composable
private fun StudentAvatar() {
    Surface(color = LightGreen, shape = CircleShape, modifier = Modifier.size(36.dp)) {
        Text("S", color = BrandGreen, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 13.dp, top = 10.dp))
    }
}

@Composable
private fun StudentFutureDestination(title: String, modifier: Modifier = Modifier) {
    val message = "$title is not implemented because no screen requirements or reference are available yet."
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(PageBackground).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (title == "Dashboard") {
            Text(title, color = NavyText, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(12.dp))
        Text(message, color = SecondaryGreen, style = MaterialTheme.typography.bodyLarge)
    }
}
