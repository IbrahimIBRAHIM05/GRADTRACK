package com.example.gradtraker.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gradtraker.navigation.RouteHistory
import com.example.gradtraker.ui.notifications.NotificationsScreen
import com.example.gradtraker.ui.notifications.NotificationsViewModel
import kotlinx.coroutines.launch

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
    StudentDestination("Milestones", "◷"),
    StudentDestination("Tasks", "✓"),
    StudentDestination("Submissions", "⇧"),
    StudentDestination("Notifications", "◇"),
    StudentDestination("Profile & Settings", "⚙")
)

@Composable
fun StudentWorkspaceScreen(onSwitchWorkspace: () -> Unit) {
    var routeHistory by rememberSaveable { mutableStateOf("Dashboard") }
    val currentRoute = RouteHistory.current(routeHistory)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val notificationViewModel: NotificationsViewModel = viewModel(key = "student-notifications")

    fun navigate(route: String) {
        routeHistory = RouteHistory.push(routeHistory, route)
    }

    fun back() {
        routeHistory = RouteHistory.pop(routeHistory)
    }

    BackHandler(enabled = RouteHistory.canGoBack(routeHistory) && !drawerState.isOpen) {
        back()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = PageBackground, modifier = Modifier.fillMaxHeight()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Surface(color = SecondaryGreen, shape = RoundedCornerShape(12.dp), modifier = Modifier.size(42.dp)) {
                            Text("G", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp, top = 6.dp))
                        }
                        Column(Modifier.padding(start = 12.dp)) {
                            Text("GradTrack", color = BrandGreen, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("Graduation Project Management", color = SecondaryGreen, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                    studentDestinations.forEach { destination ->
                        NavigationDrawerItem(
                            label = { Text(destination.title) },
                            icon = { Text(destination.icon, style = MaterialTheme.typography.titleMedium) },
                            selected = currentRoute == destination.title,
                            onClick = {
                                navigate(destination.title)
                                coroutineScope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(vertical = 3.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = LightGreen,
                                unselectedContainerColor = Color.Transparent,
                                selectedTextColor = BrandGreen,
                                unselectedTextColor = SecondaryGreen,
                                selectedIconColor = BrandGreen,
                                unselectedIconColor = SecondaryGreen
                            )
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onSwitchWorkspace)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        StudentAvatar()
                        Column(Modifier.padding(start = 10.dp)) {
                            Text("Student", color = NavyText, fontWeight = FontWeight.SemiBold)
                            Text("Switch to Supervisor workspace", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = PageBackground,
            topBar = {
                Surface(color = Color.White, shadowElevation = 1.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color.Transparent,
                            shape = CircleShape,
                            modifier = Modifier.size(44.dp).clickable { coroutineScope.launch { drawerState.open() } }
                        ) {
                            Text("☰", color = BrandGreen, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(start = 11.dp, top = 7.dp))
                        }
                        Spacer(Modifier.weight(1f))
                        StudentAvatar()
                        Column(Modifier.padding(start = 10.dp)) {
                            Text("Student", color = NavyText, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            Text("Student workspace", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        ) { innerPadding ->
            when (currentRoute) {
                "Tasks" -> StudentTasksScreen(
                    viewModel = viewModel(),
                    onBack = ::back,
                    modifier = Modifier.padding(innerPadding)
                )
                "Notifications" -> NotificationsScreen(
                    title = "Notifications",
                    viewModel = notificationViewModel,
                    onBack = ::back,
                    modifier = Modifier.padding(innerPadding)
                )
                "Profile & Settings" -> StudentProfileScreen(
                    viewModel = viewModel(),
                    onBack = ::back,
                    modifier = Modifier.padding(innerPadding)
                )
                else -> StudentFutureDestination(
                    title = currentRoute,
                    onBack = ::back,
                    modifier = Modifier.padding(innerPadding)
                )
            }
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
private fun StudentFutureDestination(title: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    val message = when (title) {
        "Milestones" -> "Milestones will be implemented in a later phase."
        else -> "$title is not implemented because no screen requirements or reference are available yet."
    }
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(PageBackground).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = onBack) { Text("← Back", color = SecondaryGreen) }
        Text(title, color = NavyText, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))
        Text(message, color = SecondaryGreen, style = MaterialTheme.typography.bodyLarge)
    }
}
