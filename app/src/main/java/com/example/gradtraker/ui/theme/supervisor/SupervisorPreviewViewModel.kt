
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
        val current = _uiState.value

        val updatedProposals = current.proposals.filterNot {
            it.id == proposalId
        }

        if (updatedProposals.size == current.proposals.size) {
            return
        }

        _uiState.value = current.copy(
            pendingProposals = updatedProposals.size,
            proposals = updatedProposals
        )
    }
}
