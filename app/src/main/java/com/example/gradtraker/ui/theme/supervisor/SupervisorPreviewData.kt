
package com.example.gradtracker.ui.theme.supervisor

data class SupervisorProposal(
    val id: String,
    val title: String,
    val team: String,
    val submittedDate: String,
    val status: String = "Pending"
)

data class SupervisorDeadline(
    val title: String,
    val date: String
)

data class SupervisorActivity(
    val id: String,
    val message: String
)

data class SupervisorPreviewData(
    val activeProjects: Int = 0,
    val pendingProposals: Int = 0,
    val submissionsToReview: Int = 0,
    val upcomingDeadlines: Int = 0,
    val proposals: List<SupervisorProposal> = emptyList(),
    val deadlines: List<SupervisorDeadline> = emptyList(),
    val activities: List<SupervisorActivity> = emptyList()
)
