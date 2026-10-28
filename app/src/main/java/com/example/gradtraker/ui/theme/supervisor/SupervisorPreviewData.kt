
package com.example.gradtraker.ui.theme.supervisor

data class SupervisorProposal(
    val id: String,
    val title: String,
    val team: String,
    val submittedDate: String,
    val status: String,
    val category: String = "",
    val targetUsers: String = "",
    val estimatedDuration: String = "",
    val description: String = "",
    val objectives: List<String> = emptyList(),
    val technologies: List<String> = emptyList(),
    val additionalNotes: String = "",
    val teamMembers: List<SupervisorTeamMember> = emptyList(),
    val documents: List<String> = emptyList(),
    val comments: List<String> = emptyList()
)

data class SupervisorTeamMember(
    val name: String,
    val role: String
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
    val activities: List<SupervisorActivity> = emptyList(),
    val submissions: List<SupervisorSubmission> = emptyList()
)

data class SupervisorSubmission(
    val id: String,
    val title: String,
    val team: String,
    val type: String,
    val submittedDate: String,
    val status: String
)
