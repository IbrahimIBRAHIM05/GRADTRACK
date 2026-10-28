
package com.example.gradtraker.ui.theme.supervisor

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SupervisorPreviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SupervisorPreviewData())

    val uiState: StateFlow<SupervisorPreviewData> =
        _uiState.asStateFlow()

    fun setData(data: SupervisorPreviewData) {
        _uiState.value = data
    }

    fun approveProposal(proposalId: String) {
        decideProposal(proposalId, approved = true)
    }

    fun rejectProposal(proposalId: String) {
        decideProposal(proposalId, approved = false)
    }

    fun decideProposal(proposalId: String, approved: Boolean) {
        val current = _uiState.value
        val proposal = current.proposals.firstOrNull { it.id == proposalId } ?: return
        if (!proposal.status.equals("Pending", ignoreCase = true) &&
            !proposal.status.equals("Pending Review", ignoreCase = true)
        ) {
            return
        }

        val updatedProposals = current.proposals.map { candidate ->
            if (candidate.id == proposalId) {
                candidate.copy(status = if (approved) "Approved" else "Rejected")
            } else {
                candidate
            }
        }

        _uiState.value = current.copy(
            pendingProposals = (current.pendingProposals - 1).coerceAtLeast(0),
            proposals = updatedProposals
        )
    }

    fun addProposalComment(proposalId: String, comment: String) {
        val normalizedComment = comment.trim()
        if (normalizedComment.isEmpty()) return

        val current = _uiState.value
        _uiState.value = current.copy(
            proposals = current.proposals.map { proposal ->
                if (proposal.id == proposalId) {
                    proposal.copy(comments = proposal.comments + normalizedComment)
                } else {
                    proposal
                }
            }
        )
    }
}
