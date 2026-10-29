package com.example.gradtraker.ui.theme.supervisor

import androidx.compose.foundation.background
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import java.util.Calendar
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

private data class SupervisorDestination(
    val title: String,
    val icon: String
)

private val supervisorDestinations = listOf(
    SupervisorDestination("Dashboard", "⌂"),
    SupervisorDestination("Proposal Reviews", "▤"),
    SupervisorDestination("Submission Reviews", "⇧"),
    SupervisorDestination("Feedback", "✦"),
    SupervisorDestination("Notifications", "◇"),
    SupervisorDestination("Profile & Settings", "⚙")
)

@Composable
fun SupervisorPreviewScreen(
    viewModel: SupervisorPreviewViewModel = viewModel(),
    onSwitchWorkspace: () -> Unit = {}
) {
    val data by viewModel.uiState.collectAsState()
    var routeHistory by rememberSaveable { mutableStateOf("Dashboard") }
    var selectedProposalId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedSubmissionId by rememberSaveable { mutableStateOf<String?>(null) }
    val saveableStateHolder = rememberSaveableStateHolder()
    val currentRoute = RouteHistory.current(routeHistory)
    val selectedDestination = when (currentRoute) {
        "Proposal Detail" -> "Proposal Reviews"
        "Submission Detail" -> "Submission Reviews"
        else -> currentRoute
    }
    val dashboardScrollState = rememberScrollState()
    val notificationViewModel: NotificationsViewModel = viewModel(key = "supervisor-notifications")

    fun selectTopLevel(route: String) {
        routeHistory = RouteHistory.selectTopLevel(route)
    }

    fun navigate(route: String) {
        routeHistory = RouteHistory.push(routeHistory, route)
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

    val greeting by produceState(initialValue = greetingForCurrentTime()) {
        while (true) {
            delay(60_000)
            value = greetingForCurrentTime()
        }
    }

    Scaffold(
        containerColor = PageBackground,
        topBar = {
            if (currentRoute == "Dashboard") {
                SupervisorTopBar(onSwitchWorkspace = onSwitchWorkspace)
            } else {
                GradTrackPageTopBar(
                    title = when (currentRoute) {
                        "Proposal Detail" -> "Proposal Details"
                        "Submission Detail" -> "Submission Details"
                        else -> currentRoute
                    },
                    onBack = ::back,
                    onSwitchWorkspace = onSwitchWorkspace
                )
            }
        },
        bottomBar = {
            GradTrackBottomNavigation(
                destinations = supervisorDestinations.map { GradTrackDestination(it.title, it.icon) },
                selectedTitle = selectedDestination,
                onSelect = { destination -> selectTopLevel(destination.title) }
            )
        }
    ) { innerPadding ->
        saveableStateHolder.SaveableStateProvider(currentRoute) {
            when (currentRoute) {
                "Dashboard" -> SupervisorDashboardContent(
                    data = data,
                    greeting = greeting,
                    onOpenProposals = { selectTopLevel("Proposal Reviews") },
                    onReviewProposal = { proposalId ->
                        selectedProposalId = proposalId
                        navigate("Proposal Detail")
                    },
                    scrollState = dashboardScrollState,
                    modifier = Modifier.padding(innerPadding)
                )
                "Proposal Reviews" -> SupervisorProposalReviewListScreen(
                    proposals = data.proposals.filter(::isPendingProposal),
                    onSelectProposal = { proposalId ->
                        selectedProposalId = proposalId
                        navigate("Proposal Detail")
                    },
                    modifier = Modifier.padding(innerPadding)
                )
                "Proposal Detail" -> {
                    val proposal = data.proposals.firstOrNull { it.id == selectedProposalId }
                    if (proposal == null) {
                        LaterPhaseDestination(
                            title = "Proposal unavailable",
                            message = "This proposal is no longer available in the current session.",
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        SupervisorProposalDetailScreen(
                            proposal = proposal,
                            onDecision = { approved -> viewModel.decideProposal(proposal.id, approved) },
                            onAddComment = { comment -> viewModel.addProposalComment(proposal.id, comment) },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                "Submission Reviews" -> SupervisorSubmissionReviewScreen(
                    submissions = data.submissions,
                    selectedSubmissionId = selectedSubmissionId,
                    onSelectSubmission = { selectedSubmissionId = it },
                    onOpenSelected = { if (selectedSubmissionId != null) navigate("Submission Detail") },
                    modifier = Modifier.padding(innerPadding)
                )
                "Submission Detail" -> {
                    val submission = data.submissions.firstOrNull { it.id == selectedSubmissionId }
                    if (submission == null) {
                        LaterPhaseDestination(
                            title = "Submission unavailable",
                            message = "This submission is no longer available in the current session.",
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        SupervisorSubmissionDetailScreen(
                            submission = submission,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
                "Notifications" -> NotificationsScreen(
                    viewModel = notificationViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
                "Feedback" -> SupervisorFeedbackDestination(
                    onOpenProposals = { selectTopLevel("Proposal Reviews") },
                    modifier = Modifier.padding(innerPadding)
                )
                else -> LaterPhaseDestination(
                    title = currentRoute,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun SupervisorTopBar(onSwitchWorkspace: () -> Unit) {
    Surface(color = Color.White, shadowElevation = 1.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Supervisor", color = NavyText, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.weight(1f))
            SupervisorAvatar(initials = "SP", size = 36.dp)
            Column(Modifier.padding(start = 10.dp)) {
                Text(
                    text = "Supervisor workspace",
                    color = SecondaryGreen,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            TextButton(onClick = onSwitchWorkspace) { Text("Switch", color = SecondaryGreen) }
        }
    }
}

@Composable
private fun SupervisorAvatar(initials: String, size: Dp) {
    Surface(
        color = LightGreen,
        shape = CircleShape,
        modifier = Modifier.size(size)
    ) {
        Text(
            text = initials,
            color = BrandGreen,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 9.dp, top = 10.dp)
        )
    }
}

private fun greetingForCurrentTime(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..21 -> "Good evening"
        else -> "Hello"
    }
}

@Composable
private fun SupervisorDashboardContent(
    data: SupervisorPreviewData,
    greeting: String,
    onOpenProposals: () -> Unit,
    onReviewProposal: (String) -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column {
            Text(
                text = "SUPERVISOR WORKSPACE",
                color = SecondaryGreen,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "$greeting, Supervisor",
                color = NavyText,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Review proposals and help teams stay on track.",
                color = SecondaryGreen,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Active projects", data.activeProjects, "Across this semester", Modifier.weight(1f))
                StatCard("Pending proposals", data.pendingProposals, "Awaiting your review", Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Submissions to review", data.submissionsToReview, "Need feedback", Modifier.fillMaxWidth())
            }
        }

        SectionCard(title = "Pending Project Proposals", trailing = {
            TextButton(onClick = onOpenProposals) { Text("View All", color = SecondaryGreen) }
        }) {
            val pendingProposals = data.proposals.filter(::isPendingProposal)
            if (pendingProposals.isEmpty()) {
                EmptyState(
                    icon = "▤",
                    title = "No pending proposals",
                    message = "Proposals will appear here when available."
                )
            } else {
                pendingProposals.forEachIndexed { index, proposal ->
                    if (index > 0) Spacer(Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = PageBackground)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = proposal.title,
                                    color = NavyText,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = { onReviewProposal(proposal.id) }) {
                                    Text("Review →", color = SecondaryGreen)
                                }
                            }
                            Text("${proposal.team}  ·  ${proposal.submittedDate}", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(6.dp))
                            StatusPill(proposal.status)
                        }
                    }
                }
            }
        }

        SectionCard(title = "Deadline Reminders") {
            if (data.deadlines.isEmpty()) {
                EmptyState(
                    icon = "◷",
                    title = "No upcoming deadlines",
                    message = "Upcoming deadlines will appear here."
                )
            } else {
                data.deadlines.forEach { deadline ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(deadline.title, color = SecondaryGreen, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(12.dp))
                        Text(deadline.date, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        SectionCard(title = "Recent Activity") {
            if (data.activities.isEmpty()) {
                EmptyState(
                    icon = "✦",
                    title = "No recent activity",
                    message = "Updates will appear here when available."
                )
            } else {
                data.activities.forEach { activity ->
                    Text(
                        text = activity.message,
                        color = SecondaryGreen,
                        modifier = Modifier.padding(vertical = 7.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: Int,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(title, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Text(
                text = value.toString(),
                color = NavyText,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(5.dp))
            Text(subtitle, color = SecondaryGreen, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = NavyText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                trailing?.invoke()
            }
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun StatusPill(status: String) {
    Surface(color = LightGreen, shape = RoundedCornerShape(50)) {
        Text(
            text = status,
            color = SecondaryGreen,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun EmptyState(icon: String, title: String, message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = LightGreen,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(icon, color = SecondaryGreen, style = MaterialTheme.typography.titleMedium)
            }
        }
        Column(Modifier.padding(start = 12.dp)) {
            Text(
                text = title,
                color = BrandGreen,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = message,
                color = SecondaryGreen,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun LaterPhaseDestination(
    title: String,
    message: String = "$title will be implemented in a later phase.",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = SecondaryGreen,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun SupervisorPreview() {
    MaterialTheme {
        SupervisorPreviewScreen(viewModel = SupervisorPreviewViewModel())
    }
}

private fun isPendingProposal(proposal: SupervisorProposal): Boolean =
    proposal.status.equals("Pending", ignoreCase = true) ||
        proposal.status.equals("Pending Review", ignoreCase = true)
