package com.example.gradtracker.ui.theme.supervisor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview


private val PageBackground = Color(0xFFF8F4EA)
private val BrandGreen = Color(0xFF25463B)
private val SecondaryGreen = Color(0xFF4F7A64)

@Composable
fun SupervisorPreviewScreen(
    viewModel: SupervisorPreviewViewModel = viewModel()
) {
    val data by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "GradTrack",
                style = MaterialTheme.typography.titleLarge,
                color = BrandGreen
            )

            Text(
                text = "Graduation Project Management",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Column {
            Text(
                text = "SUPERVISOR WORKSPACE",
                color = SecondaryGreen
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Good morning, Dr. Ahmed",
                style = MaterialTheme.typography.headlineSmall,
                color = BrandGreen
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Review proposals and help teams stay on track.",
                color = SecondaryGreen
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                "Active projects",
                data.activeProjects.toString(),
                Modifier.weight(1f)
            )

            StatCard(
                "Pending proposals",
                data.pendingProposals.toString(),
                Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                "Submissions to review",
                data.submissionsToReview.toString(),
                Modifier.weight(1f)
            )

            StatCard(
                "Upcoming deadlines",
                data.upcomingDeadlines.toString(),
                Modifier.weight(1f)
            )
        }

        EmptySection(
            title = "Pending Project Proposals",
            isEmpty = data.proposals.isEmpty(),
            message = "No pending proposals"
        )

        EmptySection(
            title = "Deadline Reminders",
            isEmpty = data.deadlines.isEmpty(),
            message = "No upcoming deadlines"
        )

        EmptySection(
            title = "Recent Activity",
            isEmpty = data.activities.isEmpty(),
            message = "No recent activity"
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                color = SecondaryGreen,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = value,
                color = BrandGreen,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
private fun EmptySection(
    title: String,
    isEmpty: Boolean,
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = BrandGreen
            )

            Spacer(Modifier.height(14.dp))

            if (isEmpty) {
                Text(
                    text = message,
                    color = SecondaryGreen
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 390)
@Composable
private fun SupervisorPreview() {
    MaterialTheme {
        SupervisorPreviewScreen(
            viewModel = SupervisorPreviewViewModel()
        )
    }
}
