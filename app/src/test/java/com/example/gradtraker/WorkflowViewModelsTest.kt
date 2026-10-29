package com.example.gradtraker

import com.example.gradtraker.ui.notifications.NotificationEntry
import com.example.gradtraker.ui.notifications.NotificationsViewModel
import com.example.gradtraker.ui.student.StudentProfileViewModel
import com.example.gradtraker.ui.student.StudentTaskCreationResult
import com.example.gradtraker.ui.student.StudentTaskStatus
import com.example.gradtraker.ui.student.TaskSubmissionOutcome
import com.example.gradtraker.ui.student.StudentTasksViewModel
import com.example.gradtraker.ui.theme.supervisor.SupervisorPreviewData
import com.example.gradtraker.ui.theme.supervisor.SupervisorPreviewViewModel
import com.example.gradtraker.ui.theme.supervisor.SupervisorProposal
import com.example.gradtraker.navigation.RouteHistory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkflowViewModelsTest {

    @Test
    fun supervisorDashboardStartsWithoutBusinessData() {
        val viewModel = SupervisorPreviewViewModel()

        assertEquals(SupervisorPreviewData(), viewModel.uiState.value)
    }

    @Test
    fun proposalDecisionChangesPendingItemOnlyOnce() {
        val pendingProposal = SupervisorProposal(
            id = "proposal-1",
            title = "Proposal",
            team = "Team",
            submittedDate = "",
            status = "Pending"
        )
        val alreadyApproved = pendingProposal.copy(id = "proposal-2", status = "Approved")
        val viewModel = SupervisorPreviewViewModel()
        viewModel.setData(
            SupervisorPreviewData(
                pendingProposals = 1,
                proposals = listOf(pendingProposal, alreadyApproved)
            )
        )

        viewModel.rejectProposal("proposal-1")
        viewModel.approveProposal("proposal-1")

        assertEquals("Rejected", viewModel.uiState.value.proposals.first().status)
        assertEquals("Approved", viewModel.uiState.value.proposals.last().status)
        assertEquals(0, viewModel.uiState.value.pendingProposals)
    }

    @Test
    fun proposalCommentsAreSessionStateAndIgnoreBlankInput() {
        val proposal = SupervisorProposal("proposal-1", "Proposal", "Team", "", "Pending")
        val viewModel = SupervisorPreviewViewModel()
        viewModel.setData(SupervisorPreviewData(proposals = listOf(proposal)))

        viewModel.addProposalComment(proposal.id, "  ")
        viewModel.addProposalComment(proposal.id, "  Please clarify the objective.  ")

        assertEquals(listOf("Please clarify the objective."), viewModel.uiState.value.proposals.single().comments)
    }

    @Test
    fun studentTaskRequiresDueDateAndStartsToDo() {
        val viewModel = StudentTasksViewModel()
        val missingDueDate = viewModel.addTask("Write report", "", "  ")
        assertEquals(StudentTaskCreationResult.MissingDueDate, missingDueDate)
        assertTrue(viewModel.tasks.value.isEmpty())

        val missingTitle = viewModel.addTask("  ", "", "2026-11-01")
        assertEquals(StudentTaskCreationResult.MissingTitle, missingTitle)
        assertTrue(viewModel.tasks.value.isEmpty())

        val created = viewModel.addTask("Write report", "", "2026-11-01")
        assertTrue(created is StudentTaskCreationResult.Created)
        assertEquals(StudentTaskStatus.TO_DO, viewModel.tasks.value.single().status)
    }

    @Test
    fun openingToDoTaskStartsWorkAndReopeningDoneDoesNotResetIt() {
        val viewModel = StudentTasksViewModel()
        assertTrue(viewModel.javaClass.methods.none { it.name == "advanceStatus" || it.name == "setTaskStatus" })
        val task = (viewModel.addTask("Write report", "", "2026-11-01") as StudentTaskCreationResult.Created).task

        viewModel.openTask(task.id)
        assertEquals(StudentTaskStatus.IN_PROGRESS, viewModel.tasks.value.single().status)

        viewModel.onSubmissionResult(task.id, TaskSubmissionOutcome.SUCCEEDED)
        viewModel.openTask(task.id)
        assertEquals(StudentTaskStatus.DONE, viewModel.tasks.value.single().status)
    }

    @Test
    fun onlySuccessfulSubmissionCanMarkAnInProgressTaskDone() {
        val viewModel = StudentTasksViewModel()
        val task = (viewModel.addTask("Write report", "", "2026-11-01") as StudentTaskCreationResult.Created).task
        viewModel.openTask(task.id)

        viewModel.onSubmissionResult(task.id, TaskSubmissionOutcome.FAILED)
        assertEquals(StudentTaskStatus.IN_PROGRESS, viewModel.tasks.value.single().status)
        viewModel.onSubmissionResult(task.id, TaskSubmissionOutcome.CANCELLED)
        assertEquals(StudentTaskStatus.IN_PROGRESS, viewModel.tasks.value.single().status)
        viewModel.onSubmissionResult(task.id, TaskSubmissionOutcome.SUCCEEDED)
        assertEquals(StudentTaskStatus.DONE, viewModel.tasks.value.single().status)
    }

    @Test
    fun selectingTopLevelDestinationClearsDetailHistoryAndDashboardDoesNotDuplicate() {
        assertEquals("Dashboard", RouteHistory.push("Dashboard", "Dashboard"))
        val detail = RouteHistory.push("Dashboard", "Submission Details")
        val switched = RouteHistory.selectTopLevel("Notifications")
        val dashboard = RouteHistory.selectTopLevel("Dashboard")

        assertEquals("Dashboard", dashboard)
        assertFalse(RouteHistory.canGoBack(dashboard))
        assertEquals("Proposal Reviews", RouteHistory.parent("Proposal Reviews\u001FProposal Details"))
    }

    @Test
    fun profileChangesAreHeldInViewModelForCurrentSession() {
        val viewModel = StudentProfileViewModel()
        viewModel.beginEditing()
        viewModel.updateDraft { it.copy(fullName = "Student") }
        viewModel.saveForSession()

        assertEquals("Student", viewModel.uiState.value.profile.fullName)
        assertFalse(viewModel.uiState.value.isEditing)
        assertTrue(viewModel.uiState.value.message.orEmpty().contains("session only"))
    }

    @Test
    fun notificationActionsOnlyMarkProvidedItemsRead() {
        val viewModel = NotificationsViewModel()
        viewModel.setNotifications(
            listOf(
                NotificationEntry("one", "Update", "Message", "", "Task"),
                NotificationEntry("two", "Update", "Message", "", "Proposal", isRead = true)
            )
        )

        viewModel.markRead("one")

        assertTrue(viewModel.notifications.value.all { it.isRead })
        viewModel.setNotifications(emptyList())
        viewModel.markAllRead()
        assertTrue(viewModel.notifications.value.isEmpty())
    }
}
