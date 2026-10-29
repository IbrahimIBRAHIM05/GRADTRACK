package com.example.gradtraker.ui.theme.supervisor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val PageBackground = Color(0xFFF8F4EA)
private val BrandGreen = Color(0xFF25463B)
private val SecondaryGreen = Color(0xFF4F7A64)
private val LightGreen = Color(0xFFE6EEDF)
private val NavyText = Color(0xFF27344C)

@Composable
fun SupervisorProposalReviewListScreen(
    proposals: List<SupervisorProposal>,
    onSelectProposal: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PageBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text("Review submitted project proposals.", color = SecondaryGreen, style = MaterialTheme.typography.bodyMedium)
        }
        if (proposals.isEmpty()) {
            WorkflowCard(title = "Pending Project Proposals") {
                EmptyWorkflowState(
                    title = "No proposals to review",
                    message = "Proposals will appear here when they are available for review."
                )
            }
        } else {
            proposals.forEach { proposal ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectProposal(proposal.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                proposal.title,
                                color = NavyText,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            StatusBadge(proposal.status)
                        }
                        Text("Team: ${proposal.team}", color = SecondaryGreen)
                        Text("Submitted: ${proposal.submittedDate}", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                        TextButton(onClick = { onSelectProposal(proposal.id) }) {
                            Text("Review proposal →", color = BrandGreen)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SupervisorProposalDetailScreen(
    proposal: SupervisorProposal,
    onDecision: (approved: Boolean) -> Unit,
    onAddComment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable(proposal.id) { mutableStateOf(0) }
    var pendingDecision by remember { mutableStateOf<Boolean?>(null) }
    var decisionNotice by rememberSaveable(proposal.id) { mutableStateOf<String?>(null) }
    var commentText by rememberSaveable(proposal.id) { mutableStateOf("") }
    var commentNotice by rememberSaveable(proposal.id) { mutableStateOf<String?>(null) }
    val canDecide = proposal.status.equals("Pending", true) || proposal.status.equals("Pending Review", true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PageBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        WorkflowCard(title = proposal.title) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (proposal.category.isNotBlank()) Text(proposal.category, color = SecondaryGreen)
                Spacer(Modifier.weight(1f))
                StatusBadge(proposal.status)
            }
            Spacer(Modifier.height(8.dp))
            Text("Team: ${proposal.team}", color = SecondaryGreen)
            if (proposal.submittedDate.isNotBlank()) {
                Text("Submitted: ${proposal.submittedDate}", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
            }
        }

        WorkflowCard(title = "Project Information") {
            PrimaryTabRow(selectedTabIndex = selectedTab, containerColor = Color.White) {
                listOf("Project Details", "Team Members", "Documents").forEachIndexed { index, label ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(label) }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            when (selectedTab) {
                0 -> ProposalProjectDetails(proposal)
                1 -> ProposalTeamDetails(proposal.teamMembers)
                else -> ProposalDocuments(proposal.documents)
            }
        }

        WorkflowCard(title = "Team Information") {
            Text(proposal.team.ifBlank { "Team information not provided." }, color = SecondaryGreen)
            if (proposal.teamMembers.isEmpty()) {
                Text("No team member details provided.", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
            } else {
                proposal.teamMembers.forEach { member ->
                    Column(Modifier.padding(top = 8.dp)) {
                        Text(member.name, color = NavyText, fontWeight = FontWeight.Medium)
                        Text(member.role, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        WorkflowCard(title = "Review Decision") {
            if (canDecide) {
                Button(
                    onClick = { pendingDecision = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
                ) { Text("Approve Project") }
                OutlinedButton(
                    onClick = { pendingDecision = false },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Reject Proposal", color = BrandGreen) }
            } else {
                Text("Decision: ${proposal.status}", color = BrandGreen, fontWeight = FontWeight.SemiBold)
            }
            decisionNotice?.let { Text(it, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall) }
            Text(
                "Decisions are held in memory for this session only; they are not permanently saved.",
                color = SecondaryGreen,
                style = MaterialTheme.typography.bodySmall
            )
        }

        WorkflowCard(title = "Feedback / Comments") {
            if (proposal.comments.isEmpty()) {
                Text("No comments have been added.", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
            } else {
                proposal.comments.forEach { comment ->
                    Text(comment, color = NavyText, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
            OutlinedTextField(
                value = commentText,
                onValueChange = { commentText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Feedback / comments") },
                placeholder = { Text("Enter feedback for this proposal") },
                minLines = 3
            )
            Button(
                onClick = {
                    if (commentText.isNotBlank()) {
                        onAddComment(commentText)
                        commentText = ""
                        commentNotice = "Comment added for this session only; it has not been sent or permanently saved."
                    }
                },
                enabled = commentText.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
            ) { Text("Add Feedback") }
            commentNotice?.let { Text(it, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall) }
        }
    }

    pendingDecision?.let { approved ->
        AlertDialog(
            onDismissRequest = { pendingDecision = null },
            title = { Text(if (approved) "Approve this project?" else "Reject this proposal?") },
            text = {
                Text(
                    "This decision will update the current session only. It will not be permanently saved."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDecision(approved)
                        decisionNotice = if (approved) {
                            "Approved for this session only. No permanent save was made."
                        } else {
                            "Rejected for this session only. No permanent save was made."
                        }
                        pendingDecision = null
                    }
                ) { Text(if (approved) "Approve Project" else "Reject Proposal") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDecision = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun ProposalProjectDetails(proposal: SupervisorProposal) {
    if (proposal.description.isBlank() && proposal.category.isBlank() && proposal.targetUsers.isBlank() &&
        proposal.estimatedDuration.isBlank() && proposal.objectives.isEmpty() &&
        proposal.technologies.isEmpty() && proposal.additionalNotes.isBlank()
    ) {
        EmptyWorkflowState("Project details not provided", "Details will appear when proposal information is available.")
        return
    }
    if (proposal.category.isNotBlank()) DetailField("Category", proposal.category)
    if (proposal.targetUsers.isNotBlank()) DetailField("Target users", proposal.targetUsers)
    if (proposal.estimatedDuration.isNotBlank()) DetailField("Estimated duration", proposal.estimatedDuration)
    if (proposal.description.isNotBlank()) DetailField("Description", proposal.description)
    if (proposal.objectives.isNotEmpty()) {
        Text("Objectives", color = NavyText, fontWeight = FontWeight.SemiBold)
        proposal.objectives.forEach { Text("• $it", color = SecondaryGreen) }
    }
    if (proposal.technologies.isNotEmpty()) {
        Text("Technologies & tools", color = NavyText, fontWeight = FontWeight.SemiBold)
        Text(proposal.technologies.joinToString(" · "), color = SecondaryGreen)
    }
    if (proposal.additionalNotes.isNotBlank()) DetailField("Additional notes", proposal.additionalNotes)
}

@Composable
private fun ProposalTeamDetails(members: List<SupervisorTeamMember>) {
    if (members.isEmpty()) {
        EmptyWorkflowState("No team members listed", "Team member information was not provided.")
    } else {
        members.forEach { member ->
            Column(Modifier.padding(vertical = 6.dp)) {
                Text(member.name, color = NavyText, fontWeight = FontWeight.SemiBold)
                Text(member.role, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun ProposalDocuments(documents: List<String>) {
    if (documents.isEmpty()) {
        EmptyWorkflowState("No documents attached", "Documents will be listed here when provided.")
    } else {
        documents.forEach { document -> Text(document, color = SecondaryGreen, modifier = Modifier.padding(vertical = 6.dp)) }
    }
}

@Composable
fun SupervisorSubmissionReviewScreen(
    submissions: List<SupervisorSubmission>,
    selectedSubmissionId: String?,
    onSelectSubmission: (String) -> Unit,
    onOpenSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val awaitingReview = submissions.count { it.status.equals("Review", true) || it.status.equals("Awaiting review", true) }
    val reviewed = submissions.count { it.status.equals("Approved", true) || it.status.equals("Reviewed", true) }
    val revisions = submissions.count { it.status.equals("Revision", true) || it.status.equals("Revision required", true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PageBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text("Review recent project submissions.", color = SecondaryGreen)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SummaryCard("Awaiting review", awaitingReview, Modifier.weight(1f))
            SummaryCard("Reviewed", reviewed, Modifier.weight(1f))
            SummaryCard("Need revisions", revisions, Modifier.weight(1f))
        }
        WorkflowCard(title = "Recent Submissions") {
            if (submissions.isEmpty()) {
                EmptyWorkflowState("No submissions to review", "Submissions will appear here when available.")
            } else {
                submissions.forEach { submission ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelectSubmission(submission.id) },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedSubmissionId == submission.id) LightGreen else Color.White
                        )
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(submission.title, color = NavyText, fontWeight = FontWeight.SemiBold)
                            Text("${submission.team} · ${submission.type}", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                            Text("${submission.submittedDate} · ${submission.status}", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
        Button(
            onClick = onOpenSelected,
            enabled = selectedSubmissionId != null,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
        ) { Text("Open selected submission") }
    }
}

@Composable
fun SupervisorSubmissionDetailScreen(
    submission: SupervisorSubmission,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PageBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        WorkflowCard(title = submission.title) {
            DetailField("Team", submission.team)
            DetailField("Type", submission.type)
            DetailField("Submitted", submission.submittedDate)
            DetailField("Status", submission.status)
        }
        Text(
            "Only the submission information provided to the app is shown here.",
            color = SecondaryGreen,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SupervisorFeedbackDestination(
    onOpenProposals: () -> Unit,
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
        Spacer(Modifier.height(10.dp))
        Text(
            "Feedback is entered from a proposal's review screen. No separate feedback list is shown in the available references.",
            color = SecondaryGreen,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onOpenProposals) { Text("Open Proposal Reviews", color = BrandGreen) }
    }
}

@Composable
private fun WorkflowCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, color = NavyText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}

@Composable
private fun EmptyWorkflowState(title: String, message: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Surface(color = LightGreen, shape = RoundedCornerShape(10.dp)) {
            Box(Modifier.padding(10.dp)) { Text("—", color = SecondaryGreen) }
        }
        Column(Modifier.padding(start = 12.dp)) {
            Text(title, color = BrandGreen, fontWeight = FontWeight.SemiBold)
            Text(message, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    Surface(color = LightGreen, shape = RoundedCornerShape(50)) {
        Text(status, color = SecondaryGreen, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column {
        Text(label, color = SecondaryGreen, style = MaterialTheme.typography.labelMedium)
        Text(value.ifBlank { "Not provided" }, color = NavyText, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SummaryCard(title: String, value: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(value.toString(), color = NavyText, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Text(title, color = SecondaryGreen, style = MaterialTheme.typography.labelSmall)
        }
    }
}
